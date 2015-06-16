package cn.hd.ws.action;

import java.util.Date;

import cn.hd.base.BaseAction;
import cn.hd.ws.dao.Wxorder;
import cn.hd.ws.dao.WxorderService;

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
	
	public String add(){
		wxorder.setOrderid("orderid1");
		wxorder.setCrdate(new Date());
		wxorder.setStatus((byte)1);
		wxorderService.add(wxorder);
		System.out.println("add order:"+wxorder.getPrepayid());
		return null;
	}
}
