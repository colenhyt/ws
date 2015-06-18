package cn.hd.ws.action;

import java.util.Date;
import java.util.List;

import cn.hd.base.BaseAction;
import cn.hd.base.BaseService;
import cn.hd.ws.dao.EcsGoods;
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
	
	public String order(){
		String strgoods = this.getHttpRequest().getParameter("goods");
		List<EcsGoods> items = BaseService.jsonToBeanList(strgoods, EcsGoods.class);
		//EcsGoods[] goods = (EcsGoods[])items.toArray();
		String wxhao = this.getHttpRequest().getParameter("wxhao");
		String paytype = this.getHttpRequest().getParameter("paytype");
		String address = this.getHttpRequest().getParameter("address");
		System.out.println(items.get(0).getGoodsNumber()+"wxhao:"+wxhao+"pay:"+paytype+",add:"+address);
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
