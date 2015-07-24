package cn.hd.ws.action;

import java.io.InputStream;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.hd.base.BaseAction;
import cn.hd.ws.dao.EcsCategory;
import cn.hd.ws.dao.EcsGoods;
import cn.hd.ws.dao.EcsGoodsActivityWithBLOBs;
import cn.hd.ws.dao.EcsGoodsService;

import com.tencent.business.UnifiedOrderBusiness;
import com.tencent.common.Configure;
import com.tencent.protocol.unifiedorder_protocol.UnifiedOrderReqData;
import com.tencent.protocol.unifiedorder_protocol.UnifiedOrderResData;

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
		List<EcsCategory> cats = ecsGoodsService.cats();
		JSONArray jsonObject = JSONArray.fromObject(cats);
		write(jsonObject.toString());
		//System.out.println("cat:"+cats.size());
		return null;
	}
	
	
	public String group(){
//		queryWxpay(null);
		EcsGoodsActivityWithBLOBs group = ecsGoodsService.activeGroup();
		JSONObject jsonObject = JSONObject.fromObject(group);
		write(jsonObject.toString());
		//System.out.println("group:"+jsonObject);
		return null;
	}
		
	
	public String goods(){
		System.out.println("query goods cat:"+ecsgoods.getCatId());
		List<EcsGoods> goods = ecsGoodsService.goods(ecsgoods.getCatId());
		
		JSONArray jsonObject = JSONArray.fromObject(goods);
		write(jsonObject.toString());
		//System.out.println("goods:"+jsonObject.toString());
		return null;
	}	
}
