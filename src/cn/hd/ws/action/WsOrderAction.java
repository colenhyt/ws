package cn.hd.ws.action;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import net.sf.json.JSONObject;
import cn.hd.base.BaseAction;
import cn.hd.base.BaseService;
import cn.hd.base.Message;
import cn.hd.ws.dao.EcsGoods;
import cn.hd.ws.dao.EcsGoodsService;
import cn.hd.ws.dao.EcsOrderInfo;
import cn.hd.ws.dao.EcsOrderService;
import cn.hd.ws.dao.EcsUserService;
import cn.hd.wx.WxUserInfo;

import com.tencent.business.UnifiedOrderBusiness;
import com.tencent.common.Configure;
import com.tencent.common.Util;
import com.tencent.protocol.order_protocol.JSChooseWXPayReqData;
import com.tencent.protocol.unifiedorder_protocol.UnifiedOrderReqData;
import com.tencent.protocol.unifiedorder_protocol.UnifiedOrderResData;

public class WsOrderAction extends BaseAction {
	private EcsOrderInfo wxorder;
	private EcsOrderService ecsorderService;
	private EcsUserService ecsuserService;
	
	public EcsUserService getEcsuserService() {
		return ecsuserService;
	}

	public void setEcsuserService(EcsUserService ecsuserService) {
		this.ecsuserService = ecsuserService;
	}

	public EcsOrderService getEcsorderService() {
		return ecsorderService;
	}

	public void setEcsorderService(EcsOrderService ecsorderService) {
		this.ecsorderService = ecsorderService;
	}

	public WsOrderAction(){
		init("ecsorderService","ecsuserService");
	}
	
	public EcsOrderInfo getEcsOrderInfo() {
		return wxorder;
	}

	public void setEcsOrderInfo(EcsOrderInfo wxorder) {
		this.wxorder = wxorder;
	}
	
	private boolean queryWxpay(EcsOrderInfo order,String openid){
    	UnifiedOrderBusiness bus = null;
    	InputStream in = getHttpSession().getServletContext().getResourceAsStream(Configure.getCertLocalPath());
    	Configure.setIn(in);
		try {
			bus = new UnifiedOrderBusiness();
			int intTotalFee = (int)(order.getGoodsAmount().floatValue()*100);	//单位是分
			intTotalFee = 1;
			String spbill_create_ip = getIpAddress();
			//spbill_create_ip = "192.168.11.1";
	    	UnifiedOrderReqData  reqdata = new UnifiedOrderReqData(openid,"NCTG goods",order.getOrderSn(),intTotalFee,spbill_create_ip);
	    	UnifiedOrderResData rst = new UnifiedOrderResData();
//	    	rst.setResult_code("SUCCESS");
	    	rst = bus.run(reqdata);
    		order.setReturnCode(rst.getReturn_code());
    		order.setReturnMsg(rst.getReturn_msg());
    		order.setResultCode(rst.getResult_code());
	    	if (rst.isSuccess()){
	    		order.setPrepayId(rst.getPrepay_id());
	    		order.setCodeUrl(rst.getCode_url());
	    		return true;
	    	}else {
	    		order.setErrCode(rst.getErr_code());
	    		order.setErrCodeDes(rst.getErr_code_des());
	    	}
	    } catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return false;
	}
	
	public String order(){
		Util.log("1");
		String userinfo = this.getHttpRequest().getParameter("userinfo");
		if (userinfo==null||userinfo.equalsIgnoreCase("null")){
			writeMsg(RetMsg.MSG_UserInfoMissing);	
			return null;
		}		
		JSONObject jsonobj = JSONObject.fromObject(userinfo);
		WxUserInfo info = (WxUserInfo)JSONObject.toBean(jsonobj, WxUserInfo.class);
		if (info.getOpenid()==null||info.getOpenid().length()<=0){
			writeMsg(RetMsg.MSG_UserInfoMissing);	
			return null;			
		}
		
		float totalFee = 0;
		String strgoods = this.getHttpRequest().getParameter("goods");
//		if (strgoods==null){
//			writeMsg(RetMsg.MSG_NoAnyGoods);	
//			return null;			
//		}
//		List<EcsGoods> items = BaseService.jsonToBeanList(strgoods, EcsGoods.class);
//		String nogoods = null;
//		EcsGoodsService  goodsService = new EcsGoodsService();
//		for (int i=0;i<items.size();i++){
//			EcsGoods item = items.get(i);
//			EcsGoods item2 = goodsService.find(item.getGoodsId());
//			if (item2==null){
//				nogoods = item.getGoodsName();
//				break;
//			}
//			item.setGoodsSn(item2.getGoodsSn());
//			item.setIsReal(item2.getIsReal());
//			item.setShopPrice(item2.getShopPrice());
//			item.setGoodsId(item2.getGoodsId());
//			item.setGoodsName(item2.getGoodsName());
//			
//			totalFee += item.getShopPrice().floatValue() * item.getGoodsNumber();
//		}
//		if (nogoods!=null){
//			writeMsg2(RetMsg.MSG_GoodsNotFound,nogoods);
//			return null;
//		}else if (totalFee<=0){
//			writeMsg(RetMsg.MSG_OrderAmountInvalid);	
//			return null;			
//		}
		//获取userid:
		int userId = ecsuserService.findUserIdOrAdd(info);
		if (userId==-1){
			writeMsg(RetMsg.MSG_UserNotFound);	
			return null;
		}

		String paytype = this.getHttpRequest().getParameter("paytype");
		String contact = this.getHttpRequest().getParameter("contact");
		String phone = this.getHttpRequest().getParameter("phone");
		String remark = this.getHttpRequest().getParameter("remark");
		String province = this.getHttpRequest().getParameter("province");
		String city = this.getHttpRequest().getParameter("city");
		String address = this.getHttpRequest().getParameter("address");
		
		EcsOrderInfo orderInfo = new EcsOrderInfo();
		orderInfo.setOrderSn(DataManager.getInstance().assignOrderSn());
		orderInfo.setUserId(userId);
		orderInfo.setProvince(Short.valueOf(province));
		orderInfo.setCity(Short.valueOf(city));
		orderInfo.setAddress(address);
		orderInfo.setGoodsAmount(BigDecimal.valueOf(totalFee));
		orderInfo.setConsignee(contact);
		orderInfo.setMobile(phone);
		orderInfo.setOrderStatus(false);
		orderInfo.setShippingStatus(false);
		orderInfo.setPayId(Integer.valueOf(paytype).byteValue());
		orderInfo.setPayStatus(false);
		orderInfo.setPayNote(remark);

		boolean ret = false;
		
		//统一下单,向微信申请prepay_id:
		ret = queryWxpay(orderInfo,info.getOpenid());
		if (ret==false||orderInfo.getPrepayId()==null||orderInfo.getPrepayId().length()<=0)	{//统一下单请求失败:
			orderInfo.setOrderStatus(false);
			ecsorderService.add(orderInfo);
			writeMsg2(RetMsg.MSG_PrepayReqFail,orderInfo.getErrCodeDes());
			Util.log("统一下单申请prepay_id失败");
			return null;
		}
		
		//校验地址，如果是新地址，进行新增;
		ret = ecsuserService.validAddress(orderInfo);
		if (ret==false){
			writeMsg(RetMsg.MSG_AddressInvalid);	
			return null;			
		}
		
		ret = ecsorderService.add(orderInfo);
		if (ret==false){
			writeMsg(RetMsg.MSG_OrderSaveFail);	
			return null;			
		}
//		ret = ecsorderService.addGoods(items, orderInfo.getOrderId());
//		if (ret==false){
//			writeMsg(RetMsg.MSG_OrderGoodsSaveFail);	
//			return null;			
//		}
		
		JSONObject infoobj = JSONObject.fromObject(orderInfo);
		JSChooseWXPayReqData req = new JSChooseWXPayReqData(orderInfo.getPrepayId());
		JSONObject reqobj = JSONObject.fromObject(req);
		String desc = "["+infoobj.toString()+","+reqobj.toString()+"]";
		writeMsg2(RetMsg.MSG_OK,desc);			
		System.out.println("goods:"+strgoods+";userinfo:"+userinfo+"pay:"+paytype+",address:"+address);
		return null;
	}

}
