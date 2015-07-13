package cn.hd.ws.action;

import java.io.InputStream;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.hd.base.BaseAction;
import cn.hd.ws.dao.EcsGoods;
import cn.hd.ws.dao.EcsGoodsActivityWithBLOBs;
import cn.hd.ws.dao.EcsGoodsService;

import com.tencent.business.UnifiedOrderBusiness;
import com.tencent.business.UnifiedOrderResult;
import com.tencent.common.Configure;
import com.tencent.protocol.unifiedorder_protocol.UnifiedOrderReqData;

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
//		List<EcsCategory> cats = ecsGoodsService.cats();
//		JSONArray jsonObject = JSONArray.fromObject(cats);
//		write(jsonObject.toString(),"utf-8");
//		System.out.println("cat:"+cats.size());
		
    	UnifiedOrderBusiness bus = null;
    	InputStream in = getHttpSession().getServletContext().getResourceAsStream("/WEB-INF/cert/apiclient_cert.p12");
    	Configure.setIn(in);
		try {
			bus = new UnifiedOrderBusiness();
	    	String notifyUrl = "http://egonctg.com/ws/payment/";
	    	String spBillCreateIP = "112.74.108.46";
	    	UnifiedOrderReqData  reqdata = new UnifiedOrderReqData("bb","123",1,notifyUrl,spBillCreateIP);
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

    	
		return null;
	}
	
	public String group(){
		EcsGoodsActivityWithBLOBs group = ecsGoodsService.activeGroup();
		JSONObject jsonObject = JSONObject.fromObject(group);
		//write(jsonObject.toString(),"utf-8");
		System.out.println("group:"+jsonObject);
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
