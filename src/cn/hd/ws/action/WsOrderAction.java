package cn.hd.ws.action;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;
import cn.hd.base.BaseAction;
import cn.hd.base.BaseService;
import cn.hd.base.Message;
import cn.hd.ws.dao.EcsGoods;
import cn.hd.ws.dao.EcsGoodsService;
import cn.hd.ws.dao.EcsOrderInfo;
import cn.hd.ws.dao.EcsOrderService;
import cn.hd.ws.dao.Wxorder;

import com.tencent.business.UnifiedOrderBusiness;
import com.tencent.business.UnifiedOrderResult;
import com.tencent.common.Configure;
import com.tencent.protocol.unifiedorder_protocol.UnifiedOrderReqData;

public class WsOrderAction extends BaseAction {
	private Wxorder wxorder;
	private EcsOrderService ecsorderService;

	public EcsOrderService getEcsorderService() {
		return ecsorderService;
	}

	public void setEcsorderService(EcsOrderService ecsorderService) {
		this.ecsorderService = ecsorderService;
	}

	public WsOrderAction(){
		init("ecsorderService");
	}
	
	public Wxorder getWxorder() {
		return wxorder;
	}

	public void setWxorder(Wxorder wxorder) {
		this.wxorder = wxorder;
	}
	
	private void queryWxpay(Wxorder order){
    	UnifiedOrderBusiness bus = null;
    	InputStream in = getHttpSession().getServletContext().getResourceAsStream("/WEB-INF/cert/apiclient_cert.p12");
    	Configure.setIn(in);
		try {
			bus = new UnifiedOrderBusiness();
	    	UnifiedOrderReqData  reqdata = new UnifiedOrderReqData(order.getGoods(),order.getOrderid(),order.getTotalfee(),order.getIpaddr());
	    	reqdata.setTrade_type("NATIVE");
	    	UnifiedOrderResult rst = new UnifiedOrderResult();
	    	bus.run(reqdata, rst);
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
		int totalfee = 0;
		EcsGoodsService  goodsService = new EcsGoodsService();
		for (int i=0;i<items.size();i++){
			EcsGoods item = items.get(i);
			EcsGoods item2 = goodsService.find(item.getGoodsId());
		}
		Wxorder order = new Wxorder();
		//EcsGoods[] goods = (EcsGoods[])items.toArray();
		String wxhao = this.getHttpRequest().getParameter("wxhao");
		String paytype = this.getHttpRequest().getParameter("paytype");
		String contact = this.getHttpRequest().getParameter("contact");
		String phone = this.getHttpRequest().getParameter("phone");
		String remark = this.getHttpRequest().getParameter("remark");
		String address = this.getHttpRequest().getParameter("address");
		
		order.setOrderid(DataManager.getInstance().assignOrderSn());
		order.setAppid(Configure.getAppid());
		order.setMchid(Configure.getMchid());
		order.setAddress(address);
		order.setCrdate(new Date());
		order.setContact(contact);
		order.setGoods(strgoods);
		order.setPhone(phone);
		order.setRemark(remark);
		order.setIpaddr(getIpAddress());
		order.setStatus((byte)1);
		
		EcsOrderInfo orderInfo = new EcsOrderInfo();
		orderInfo.setOrderSn(DataManager.getInstance().assignOrderSn());
		orderInfo.setAddress(address);
		orderInfo.setConsignee(contact);
		orderInfo.setMobile(phone);
		orderInfo.setOrderStatus(false);
		orderInfo.setShippingStatus(false);
		orderInfo.setPayStatus(false);
		orderInfo.setPayNote(remark);
		
		boolean ret = false;
		ret = ecsorderService.add(orderInfo);
		
		Message msg = new Message();
		if (ret){
			msg.setCode(RetMsg.MSG_OK);
			msg.setDesc(order.getOrderid());
		}else {
		  msg.setCode(RetMsg.MSG_SQLExecuteError);
		}
		JSONObject obj = JSONObject.fromObject(msg);
		write(obj.toString(),"utf-8");			
		System.out.println("goods:"+strgoods+";wxhao:"+wxhao+"pay:"+paytype+",address:"+address);
		return null;
	}

}
