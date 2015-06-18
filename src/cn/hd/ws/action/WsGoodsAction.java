package cn.hd.ws.action;

import java.util.List;

import net.sf.json.JSONArray;
import cn.hd.base.BaseAction;
import cn.hd.ws.dao.EcsCategory;
import cn.hd.ws.dao.EcsGoods;
import cn.hd.ws.dao.EcsGoodsService;

public class WsGoodsAction extends BaseAction {
	private EcsGoods ecsgoods;
	private EcsGoodsService ecsGoodsService;

	public WsGoodsAction(){
		init("ecsGoodsService");
	}
	
	public EcsGoodsService getEcsGoodsService() {
		return ecsGoodsService;
	}

	public void setEcsGoodsService(EcsGoodsService wxorderService) {
		this.ecsGoodsService = wxorderService;
	}

	public EcsGoods getEcsGoods() {
		return ecsgoods;
	}

	public void setEcsGoods(EcsGoods ecsGoods) {
		this.ecsgoods = ecsGoods;
	}
	
	public String cat(){
		List<EcsCategory> cats = ecsGoodsService.cat();
		JSONArray jsonObject = JSONArray.fromObject(cats);
		write(jsonObject.toString(),"utf-8");
		System.out.println("cat:"+cats.size());
		return null;
	}
	
	
	public String goods(){
		System.out.println("query goods cat:"+ecsgoods.getCatId());
		List<EcsGoods> goods = ecsGoodsService.goods(ecsgoods.getCatId());
		
		JSONArray jsonObject = JSONArray.fromObject(goods);
		write(jsonObject.toString(),"utf-8");
		System.out.println("goods:"+goods.get(1).getGoodsDesc());
		return null;
	}	
}
