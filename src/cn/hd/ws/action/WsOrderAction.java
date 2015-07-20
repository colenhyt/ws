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
import cn.hd.ws.dao.Wxorder;
import cn.hd.wx.WxUserInfo;

import com.tencent.business.UnifiedOrderBusiness;
import com.tencent.common.Configure;
import com.tencent.protocol.unifiedorder_protocol.UnifiedOrderReqData;
import com.tencent.protocol.unifiedorder_protocol.UnifiedOrderResData;

public class WsOrderAction extends BaseAction {
	private Wxorder wxorder;
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
	
	public Wxorder getWxorder() {
		return wxorder;
	}

	public void setWxorder(Wxorder wxorder) {
		this.wxorder = wxorder;
	}
	
	private void queryWxpay(Wxorder order){
    	UnifiedOrderBusiness bus = null;
    	InputStream in = getHttpSession().getServletContext().getResourceAsStream(Configure.getCertLocalPath());
    	Configure.setIn(in);
		try {
			bus = new UnifiedOrderBusiness();
	    	UnifiedOrderReqData  reqdata = new UnifiedOrderReqData(order.getGoods(),order.getOrderid(),order.getTotalfee(),order.getIpaddr());
	    	reqdata.setTrade_type("NATIVE");
	    	UnifiedOrderResData rst = bus.run(reqdata);
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
	}
	
	public String order(){
		String strgoods = this.getHttpRequest().getParameter("goods");
		List<EcsGoods> items = BaseService.jsonToBeanList(strgoods, EcsGoods.class);
		float totalFee = 0;
		EcsGoodsService  goodsService = new EcsGoodsService();
		for (int i=0;i<items.size();i++){
			EcsGoods item = items.get(i);
			EcsGoods item2 = goodsService.find(item.getGoodsId());
			item.setGoodsSn(item2.getGoodsSn());
			item.setIsReal(item2.getIsReal());
			item.setShopPrice(item2.getShopPrice());
			item.setGoodsId(item2.getGoodsId());
			item.setGoodsName(item2.getGoodsName());
			
			totalFee += item.getShopPrice().floatValue() * item.getGoodsNumber();
		}
		
		//EcsGoods[] goods = (EcsGoods[])items.toArray();
		String userinfo = this.getHttpRequest().getParameter("userinfo");
		String paytype = this.getHttpRequest().getParameter("paytype");
		String contact = this.getHttpRequest().getParameter("contact");
		String phone = this.getHttpRequest().getParameter("phone");
		String remark = this.getHttpRequest().getParameter("remark");
		String province = this.getHttpRequest().getParameter("province");
		String city = this.getHttpRequest().getParameter("city");
		String address = this.getHttpRequest().getParameter("address");
		
		JSONObject jsonobj = JSONObject.fromObject(userinfo);
		WxUserInfo info = (WxUserInfo)JSONObject.toBean(jsonobj, WxUserInfo.class);
		int userId = ecsuserService.findUserIdOrAdd(info);
		
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
		
		//校验地址，如果是新地址，进行新增;
		ecsuserService.validAddress(orderInfo);
		
		boolean ret = false;
		ret = ecsorderService.add(orderInfo);
		if (ret){
			ret = ecsorderService.addGoods(items, orderInfo.getOrderId());
		}
		
		Message msg = new Message();
		if (ret){
			msg.setCode(RetMsg.MSG_OK);
			msg.setDesc(orderInfo.getOrderSn());
		}else {
		  msg.setCode(RetMsg.MSG_SQLExecuteError);
		}
		JSONObject obj = JSONObject.fromObject(msg);
		write(obj.toString(),"utf-8");			
		System.out.println("goods:"+strgoods+";userinfo:"+userinfo+"pay:"+paytype+",address:"+address);
		return null;
	}

}
