package cn.hd.ws.action;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;
import cn.hd.base.BaseAction;
import cn.hd.base.BaseService;
import cn.hd.base.Message;
import cn.hd.ws.dao.EcsGoods;
import cn.hd.ws.dao.Wxorder;
import cn.hd.ws.dao.WxorderService;

import com.tencent.common.Configure;

public class WsOrderAction extends BaseAction {
	private Wxorder wxorder;
	private WxorderService wxorderService;

	public WsOrderAction(){
		init("wxorderService");
	}
	
	public WxorderService getWxorderService() {
		return wxorderService;
	}

	public void setWxorderService(WxorderService wxorderService) {
		this.wxorderService = wxorderService;
	}

	public Wxorder getWxorder() {
		return wxorder;
	}

	public void setWxorder(Wxorder wxorder) {
		this.wxorder = wxorder;
	}
	
	public String order(){
		String strgoods = this.getHttpRequest().getParameter("goods");
		List<EcsGoods> items = BaseService.jsonToBeanList(strgoods, EcsGoods.class);
		Wxorder order = new Wxorder();
		//EcsGoods[] goods = (EcsGoods[])items.toArray();
		String wxhao = this.getHttpRequest().getParameter("wxhao");
		String paytype = this.getHttpRequest().getParameter("paytype");
		String contact = this.getHttpRequest().getParameter("contact");
		String phone = this.getHttpRequest().getParameter("phone");
		String remark = this.getHttpRequest().getParameter("remark");
		String address = this.getHttpRequest().getParameter("address");
		
		order.setOrderid(DataManager.getInstance().assignOrderId());
		order.setAppid(Configure.getAppid());
		order.setMchid(Configure.getMchid());
		order.setAddress(address);
		order.setCrdate(new Date());
		order.setContact(contact);
		order.setGoods(strgoods);
		order.setPhone(phone);
		order.setRemark(remark);
		order.setStatus((byte)1);
		boolean ret = wxorderService.add(order);
		
		Message msg = new Message();
		if (ret){
			msg.setCode(RetMsg.MSG_OK);
			msg.setDesc(order.getOrderid());
		}else
		 msg.setCode(RetMsg.MSG_SQLExecuteError);
		JSONObject obj = JSONObject.fromObject(msg);
		write(obj.toString(),"utf-8");			
		System.out.println("goods:"+strgoods+";wxhao:"+wxhao+"pay:"+paytype+",address:"+address);
		return null;
	}
	
	public String add(){
		wxorder.setOrderid("orderid1");
		wxorder.setCrdate(new Date());
		wxorder.setStatus((byte)1);
		wxorderService.add(wxorder);
		System.out.println("add order:"+wxorder.getPrepayid());
		return null;
	}
}
