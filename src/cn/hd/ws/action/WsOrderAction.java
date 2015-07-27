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
	public final static int INFO_STATUS_ORDERCHECKFAIL		= 	0;
	public final static int INFO_STATUS_ORDERSAVE			= 	1;
	public final static int INFO_STATUS_UNIFIEDORDER_OK		= 	2;
	public final static int INFO_STATUS_UNIFIEDORDER_FAIL	= 	3;
	public final static int INFO_STATUS_COMMIT_CHECKFAIL	= 	4;
	public final static int INFO_STATUS_WXPAY_OK			= 	5;
	public final static int INFO_STATUS_WXPAY_FAIL			= 	6;
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
		int retCode = RetMsg.MSG_OK;
		
		if (orderSn==null||orderSn.equalsIgnoreCase("null")){
			retCode = RetMsg.MSG_NoOrderSn;	
		}			
		String orderStr = DataManager.getInstance().findOrder(orderSn);
		if (orderStr==null){
			retCode = RetMsg.MSG_OrderNotExist;	
		}
		String payOk = this.getHttpRequest().getParameter("payOk");
		if (payOk==null||payOk.equalsIgnoreCase("null")){
			retCode = RetMsg.MSG_NoPayOk;	
		}	
		String userId = this.getHttpRequest().getParameter("userId");
		Pattern pattern = Pattern.compile("[0-9]*");
		 Matcher matcher = pattern.matcher(userId);
		if (userId==null||userId.equalsIgnoreCase("null")||!matcher.matches()){
			retCode = RetMsg.MSG_UserIdMissing;	
		}	
		JSONObject orderobj = JSONObject.fromObject(orderStr);
		EcsOrderInfo orderInfo = (EcsOrderInfo)JSONObject.toBean(orderobj, EcsOrderInfo.class);
		if (orderInfo.getUserId().intValue()!=Integer.valueOf(userId).intValue()){
			retCode = RetMsg.MSG_OrderNotExist;	
		}
		if (orderInfo.getPayStatus().booleanValue()==true){
			retCode = RetMsg.MSG_OrderHadPaid;
		}
		
		
		if (retCode!=RetMsg.MSG_OK){
			orderInfo.setInfoStatus(INFO_STATUS_COMMIT_CHECKFAIL);
		}else if (payOk.endsWith("true")){
			//更新商品库存:
			List<EcsOrderGoods> goods = ecsorderService.findGoods(orderInfo.getOrderId());
			ecsGoodsService.updateByOrderGoods(goods);
			orderInfo.setPayStatus(true);
			orderInfo.setInfoStatus(INFO_STATUS_WXPAY_OK);
			
//			if (checkWxOrder(orderInfo)){
//				orderInfo.setPayStatus(true);
//				ecsorderService.updateStatus(orderInfo);
//				DataManager.getInstance().addOrder(orderInfo);
//			}else {
//				Util.log("支付单据查询对账错误"+orderInfo.getOrderSn());
//			}
		}else
			orderInfo.setInfoStatus(INFO_STATUS_WXPAY_FAIL);
		
		DataManager.getInstance().addOrder(orderInfo);
		ecsorderService.updateStatus(orderInfo);
		Util.log("订单完成：校验结果"+retCode+",支付返回:"+payOk+",订单信息:"+orderobj.toString());
		if (retCode!=RetMsg.MSG_OK){
			writeMsg(retCode);						
		}else {
			String retstr = "{'payOk':"+payOk+",'orderSn':'"+orderInfo.getOrderSn()+"'}";
			writeMsg2(RetMsg.MSG_OK,retstr);			
		}
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
		String paytype = this.getHttpRequest().getParameter("paytype");
		String contact = this.getHttpRequest().getParameter("contact");
		String phone = this.getHttpRequest().getParameter("phone");
		String remark = this.getHttpRequest().getParameter("remark");
		String province = this.getHttpRequest().getParameter("province");
		String city = this.getHttpRequest().getParameter("city");
		String address = this.getHttpRequest().getParameter("address");
		String strTotalfee = this.getHttpRequest().getParameter("totalfee");
		
		WxUserInfo cacheUser = null;
		int retCode = RetMsg.MSG_OK;
		
		if (userinfo==null||userinfo.equalsIgnoreCase("null")){
			retCode = RetMsg.MSG_UserInfoMissing;	
		}
		if (retCode==RetMsg.MSG_OK){
			JSONObject jsonobj = JSONObject.fromObject(userinfo);
			WxUserInfo info = (WxUserInfo)JSONObject.toBean(jsonobj, WxUserInfo.class);
			if (info.getOpenid()==null||info.getOpenid().length()<=0){
				retCode = RetMsg.MSG_OpenidInvalid;	
			}else{
				//未经过登陆的用户下单
				cacheUser = DataManager.getInstance().findUser(info.getUserId());
				if (cacheUser==null){
					retCode = RetMsg.MSG_NotLoginUser;	
				}
			}
		}
		
		if (retCode==RetMsg.MSG_OK){
		//下单ip跟登陆ip不一致:
		if (!cacheUser.getIpAddress().equalsIgnoreCase(ipAddress)){
			retCode = RetMsg.MSG_InvalidOrderScene;	
		}
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
		if (retCode==RetMsg.MSG_OK&&strgoods==null){
			retCode = RetMsg.MSG_NoAnyGoods;	
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
		if (retCode==RetMsg.MSG_OK){
			if (wrongStock){
				retCode = RetMsg.MSG_StockNotEnough;
			}else if (nogoods!=null){
				writeMsg2(RetMsg.MSG_GoodsNotFound,nogoods);
				return null;
			}else if (totalFee<=0){
				retCode = RetMsg.MSG_OrderAmountInvalid;	
			}
		}
		EcsRegion region = ecsuserService.findRegion(Short.valueOf(city));
		float fFreight = 0;
		if (region!=null&&region.getFreight()!=null){
			fFreight = region.getFreight().intValue();
		}
		totalFee += fFreight;
		float reqTotalfee = Float.valueOf(strTotalfee).floatValue();
		if (Math.abs(reqTotalfee-totalFee)>0.1) {
			retCode = RetMsg.MSG_WrongTotalfee;
		}
		EcsOrderInfo orderInfo = new EcsOrderInfo();
		orderInfo.setOrderSn(DataManager.getInstance().assignOrderSn());
		if (cacheUser!=null)
			orderInfo.setUserId(cacheUser.getUserId());
		else
			orderInfo.setUserId(0);
		orderInfo.setProvince(Short.valueOf(province));
		orderInfo.setCity(Short.valueOf(city));
		orderInfo.setAddress(address);
		orderInfo.setConsignee(contact);
		orderInfo.setMobile(phone);
		orderInfo.setOrderStatus(false);
		orderInfo.setPayStatus(false);
		orderInfo.setShippingStatus(false);
		orderInfo.setPayId(Integer.valueOf(paytype).byteValue());
		orderInfo.setPayStatus(false);
		orderInfo.setPayNote(remark);
		orderInfo.setInfoStatus(INFO_STATUS_ORDERSAVE);
		
		boolean ret2 = false;
		if (retCode!=RetMsg.MSG_OK){
			orderInfo.setReturnCode(String.valueOf(retCode));
			orderInfo.setInfoStatus(INFO_STATUS_ORDERCHECKFAIL);
			ecsorderService.add(orderInfo);
			writeMsg(retCode);	
			return null;			
		}
		
		orderInfo.setGoodsAmount(BigDecimal.valueOf(totalFee));
		ret2 = ecsorderService.add(orderInfo);
		if (ret2==false){
			writeMsg(RetMsg.MSG_OrderSaveFail);	
			return null;			
		}
		ret2 = ecsorderService.addGoods(items, orderInfo.getOrderId());
		if (ret2==false){
			writeMsg(RetMsg.MSG_OrderGoodsSaveFail);	
			return null;			
		}
		
		//校验地址，如果是新地址，进行新增;
		ret2 = ecsuserService.validAddress(orderInfo);
		if (ret2==false){
			JSONObject infoobj = JSONObject.fromObject(orderInfo);
			Util.log("保存新地址失败:订单信息:"+infoobj.toString());
		}
		
		//add to cache:
		DataManager.getInstance().addOrder(orderInfo);
		cacheUser.setLastOrderTime(System.currentTimeMillis());
		DataManager.getInstance().addUser(cacheUser);
		
		//统一下单,向微信申请prepay_id:
		ret2 = queryWxpay(orderInfo,cacheUser.getOpenid(),orderTitle,ipAddress);
		if (ret2==false||orderInfo.getPrepayId()==null||orderInfo.getPrepayId().length()<=0)	{//统一下单请求失败:
			orderInfo.setInfoStatus(INFO_STATUS_UNIFIEDORDER_FAIL);
			ecsorderService.update(orderInfo);
			writeMsg2(RetMsg.MSG_PrepayReqFail,orderInfo.getErrCodeDes());
			Util.log("统一下单申请prepay_id失败:userId="+orderInfo.getUserId());
			return null;
		}
		//更新infostatus,微信支付返回数据:
		orderInfo.setInfoStatus(INFO_STATUS_UNIFIEDORDER_OK);
		ecsorderService.update(orderInfo);
		
		JSONObject infoobj = JSONObject.fromObject(orderInfo);
		JSChooseWXPayReqData req = new JSChooseWXPayReqData(orderInfo.getPrepayId());
		JSONObject reqobj = JSONObject.fromObject(req);
		String desc = "["+infoobj.toString()+","+reqobj.toString()+"]";
		writeMsg2(RetMsg.MSG_OK,desc);			
		Util.log("订单提交成功:订单信息:"+infoobj.toString()+", 商品: "+strgoods);
		return null;
	}

	public void test(){
		EcsOrderInfo info = new EcsOrderInfo();
		queryWxpay(info,"a","b","d");
	}
	public static void main(String[] args) {
		WsOrderAction a = new WsOrderAction();
		List<EcsGoods> items = BaseService.jsonToBeanList(null, EcsGoods.class);
		Util.log(items.size());
		
	}
}
