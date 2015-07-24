package cn.hd.ws.action;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;
import cn.hd.base.BaseAction;
import cn.hd.base.BaseService;
import cn.hd.ws.dao.EcsGoods;
import cn.hd.ws.dao.EcsGoodsService;
import cn.hd.ws.dao.EcsOrderGoods;
import cn.hd.ws.dao.EcsOrderInfo;
import cn.hd.ws.dao.EcsOrderService;
import cn.hd.ws.dao.EcsRegion;
import cn.hd.ws.dao.EcsUserService;
import cn.hd.wx.WxUserInfo;

import com.tencent.business.OrderQueryBusiness;
import com.tencent.business.UnifiedOrderBusiness;
import com.tencent.common.Configure;
import com.tencent.common.Util;
import com.tencent.protocol.order_protocol.JSChooseWXPayReqData;
import com.tencent.protocol.order_protocol.OrderQueryReqData;
import com.tencent.protocol.order_protocol.OrderQueryResData;
import com.tencent.protocol.unifiedorder_protocol.UnifiedOrderReqData;
import com.tencent.protocol.unifiedorder_protocol.UnifiedOrderResData;

public class WsOrderAction extends BaseAction {
	private EcsOrderInfo wxorder;
	private EcsOrderService ecsorderService;
	private EcsUserService ecsuserService;
	private EcsGoodsService ecsGoodsService;
	
	public EcsGoodsService getEcsGoodsService() {
		return ecsGoodsService;
	}

	public void setEcsGoodsService(EcsGoodsService ecsGoodsService) {
		this.ecsGoodsService = ecsGoodsService;
	}

	public EcsUserService getEcsuserService() {
		return ecsuserService;
	}

	public String freight(){
		String city = this.getHttpRequest().getParameter("city");
		if (city==null){
			writeMsg2(RetMsg.MSG_OK,String.valueOf(0));
			return null;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		 Matcher matcher = pattern.matcher(city);		
		 if (!matcher.matches()){
			 writeMsg2(RetMsg.MSG_OK,String.valueOf(0));
			 return null;
		 }
		 short cityid = Short.valueOf(city).shortValue();
		 EcsRegion region = ecsuserService.findRegion(cityid);
		 if (region==null){
			 writeMsg2(RetMsg.MSG_OK,String.valueOf(0));
			 return null;
		 }
		 writeMsg2(RetMsg.MSG_OK,String.valueOf(region.getFreight()));
		return null;
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
		init("ecsorderService","ecsuserService","ecsGoodsService");
	}
	
	public EcsOrderInfo getEcsOrderInfo() {
		return wxorder;
	}

	public void setEcsOrderInfo(EcsOrderInfo wxorder) {
		this.wxorder = wxorder;
	}
	
	private boolean queryWxpay(EcsOrderInfo order,String openid,String orderTitle,String ipAddress){
    	UnifiedOrderBusiness bus = null;
    	InputStream in = getHttpSession().getServletContext().getResourceAsStream(Configure.getCertLocalPath());
    	Configure.setIn(in);
		try {
			bus = new UnifiedOrderBusiness();
			int intTotalFee = (int)(order.getGoodsAmount().floatValue()*100);	//单位是分
			intTotalFee = 2;
			String body = orderTitle;
	    	UnifiedOrderReqData  reqdata = new UnifiedOrderReqData(openid,body,order.getOrderSn(),intTotalFee,ipAddress);
	    	UnifiedOrderResData rst = new UnifiedOrderResData();
//	    	rst.setResult_code("SUCCESS");
//	    	rst.setPrepay_id("aaa");
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
	
	//支付完毕/失败，订单状态修改
	public String commit(){
		String orderSn = this.getHttpRequest().getParameter("orderSn");
		if (orderSn==null||orderSn.equalsIgnoreCase("null")){
			writeMsg(RetMsg.MSG_NoOrderSn);	
			return null;
		}			
		String orderStr = DataManager.getInstance().findOrder(orderSn);
		if (orderStr==null){
			writeMsg(RetMsg.MSG_OrderNotExist);	
			return null;			
		}
		String payOk = this.getHttpRequest().getParameter("payOk");
		if (payOk==null||payOk.equalsIgnoreCase("null")){
			writeMsg(RetMsg.MSG_NoPayOk);	
			return null;
		}	
		String userId = this.getHttpRequest().getParameter("userId");
		Pattern pattern = Pattern.compile("[0-9]*");
		 Matcher matcher = pattern.matcher(userId);
		if (userId==null||userId.equalsIgnoreCase("null")||!matcher.matches()){
			writeMsg(RetMsg.MSG_UserIdMissing);	
			return null;
		}	
		JSONObject orderobj = JSONObject.fromObject(orderStr);
		EcsOrderInfo orderInfo = (EcsOrderInfo)JSONObject.toBean(orderobj, EcsOrderInfo.class);
		if (orderInfo.getUserId().intValue()!=Integer.valueOf(userId).intValue()){
			writeMsg(RetMsg.MSG_OrderNotExist);	
			return null;				
		}
		if (orderInfo.getPayStatus().booleanValue()==true){
			writeMsg(RetMsg.MSG_OrderHadPaid);
			return null;				
		}
		if (payOk.endsWith("true")){
			//更新商品库存:
			List<EcsOrderGoods> goods = ecsorderService.findGoods(orderInfo.getOrderId());
			ecsGoodsService.updateByOrderGoods(goods);
			orderInfo.setPayStatus(true);
			ecsorderService.updateStatus(orderInfo);
			DataManager.getInstance().addOrder(orderInfo);
			
//			if (checkWxOrder(orderInfo)){
//				orderInfo.setPayStatus(true);
//				ecsorderService.updateStatus(orderInfo);
//				DataManager.getInstance().addOrder(orderInfo);
//			}else {
//				Util.log("支付单据查询对账错误"+orderInfo.getOrderSn());
//			}
		}
		String retstr = "{'payOk':"+payOk+",'orderSn':'"+orderInfo.getOrderSn()+"'}";
		writeMsg2(RetMsg.MSG_OK,retstr);
		return null;
	}
	
	private boolean checkWxOrder(EcsOrderInfo orderInfo){
        try {
	    	OrderQueryBusiness bus3 = new OrderQueryBusiness();
	    	OrderQueryReqData req2 = new OrderQueryReqData(orderInfo.getOrderSn());
	    	OrderQueryResData res = bus3.run(req2);  
	    	if (res.isSuccess()){
	    		int totalFee = Integer.valueOf(res.getTotal_fee()).intValue();
	    		Util.log("查询单价金额:"+orderInfo.getOrderSn()+"："+res.getTotal_fee());
	    		return totalFee==orderInfo.getGoodsAmount().intValue()*100;
	    	}		
        } catch (Exception e){
            Util.log(e.getMessage());
        }    	
    	return false;
	}
	
	public String order(){
		String ipAddress = getIpAddress();
		
		String userinfo = this.getHttpRequest().getParameter("userinfo");
		if (userinfo==null||userinfo.equalsIgnoreCase("null")){
			writeMsg(RetMsg.MSG_UserInfoMissing);	
			return null;
		}		
		JSONObject jsonobj = JSONObject.fromObject(userinfo);
		WxUserInfo info = (WxUserInfo)JSONObject.toBean(jsonobj, WxUserInfo.class);
		if (info.getOpenid()==null||info.getOpenid().length()<=0){
			writeMsg(RetMsg.MSG_OpenidInvalid);	
			return null;			
		}
		//未经过登陆的用户下单
		WxUserInfo info2 = DataManager.getInstance().findUser(info.getUserId());
		if (info2==null){
			writeMsg(RetMsg.MSG_NotLoginUser);	
			return null;				
		}
		//下单ip跟登陆ip不一致:
		if (!info2.getIpAddress().equalsIgnoreCase(ipAddress)){
			writeMsg(RetMsg.MSG_InvalidOrderScene);	
			return null;
		}
		//频繁下单:
		long now = System.currentTimeMillis();
//		if (now-info2.getLastOrderTime()<30*1000){
//			writeMsg(RetMsg.MSG_OrderSequenceWrong);	
//			return null;			
//		}
		float totalFee = 0;
		String orderTitle = "";
		String strgoods = this.getHttpRequest().getParameter("goods");
		if (strgoods==null){
			writeMsg(RetMsg.MSG_NoAnyGoods);	
			return null;			
		}
		List<EcsGoods> items = BaseService.jsonToBeanList(strgoods, EcsGoods.class);
		String nogoods = null;
		boolean wrongStock = false;
		EcsGoodsService  goodsService = new EcsGoodsService();
		for (int i=0;i<items.size();i++){
			EcsGoods item = items.get(i);
			EcsGoods item2 = goodsService.find(item.getGoodsId());
			if (item2==null){
				nogoods = item.getGoodsName();
				break;
			}else if (item2.getGoodsNumber()<item.getGoodsNumber()){
				wrongStock = true;
				break;
			}
			item.setGoodsSn(item2.getGoodsSn());
			item.setIsReal(item2.getIsReal());
			item.setShopPrice(item2.getShopPrice());
			item.setGoodsId(item2.getGoodsId());
			item.setGoodsName(item2.getGoodsName());
			
			orderTitle += item2.getGoodsName()+"("+item.getGoodsNumber()+")";
			if (i!=items.size()-1)
				orderTitle += ",";
				
			totalFee += item.getShopPrice().floatValue() * item.getGoodsNumber();
		}
		if (wrongStock){
			writeMsg(RetMsg.MSG_StockNotEnough);
			return null;
		}else if (nogoods!=null){
			writeMsg2(RetMsg.MSG_GoodsNotFound,nogoods);
			return null;
		}else if (totalFee<=0){
			writeMsg(RetMsg.MSG_OrderAmountInvalid);	
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
		orderInfo.setUserId(info.getUserId());
		orderInfo.setProvince(Short.valueOf(province));
		orderInfo.setCity(Short.valueOf(city));
		orderInfo.setAddress(address);
		orderInfo.setGoodsAmount(BigDecimal.valueOf(totalFee));
		orderInfo.setConsignee(contact);
		orderInfo.setMobile(phone);
		orderInfo.setOrderStatus(false);
		orderInfo.setPayStatus(false);
		orderInfo.setShippingStatus(false);
		orderInfo.setPayId(Integer.valueOf(paytype).byteValue());
		orderInfo.setPayStatus(false);
		orderInfo.setPayNote(remark);

		boolean ret = false;
		
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
		ret = ecsorderService.addGoods(items, orderInfo.getOrderId());
		if (ret==false){
			writeMsg(RetMsg.MSG_OrderGoodsSaveFail);	
			return null;			
		}
		
		//add to cache:
		DataManager.getInstance().addOrder(orderInfo);
		info2.setLastOrderTime(System.currentTimeMillis());
		DataManager.getInstance().addUser(info2);
		
		//统一下单,向微信申请prepay_id:
		ret = queryWxpay(orderInfo,info.getOpenid(),orderTitle,ipAddress);
		if (ret==false||orderInfo.getPrepayId()==null||orderInfo.getPrepayId().length()<=0)	{//统一下单请求失败:
			orderInfo.setOrderStatus(false);
			ecsorderService.update(orderInfo);
			writeMsg2(RetMsg.MSG_PrepayReqFail,orderInfo.getErrCodeDes());
			Util.log("统一下单申请prepay_id失败");
			return null;
		}
		
		JSONObject infoobj = JSONObject.fromObject(orderInfo);
		JSChooseWXPayReqData req = new JSChooseWXPayReqData(orderInfo.getPrepayId());
		JSONObject reqobj = JSONObject.fromObject(req);
		String desc = "["+infoobj.toString()+","+reqobj.toString()+"]";
		writeMsg2(RetMsg.MSG_OK,desc);			
		System.out.println("goods:"+strgoods+";userinfo:"+userinfo+"pay:"+paytype+",address:"+address);
		return null;
	}

}
