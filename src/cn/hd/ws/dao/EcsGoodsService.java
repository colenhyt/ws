package cn.hd.ws.dao;

import java.util.ArrayList;
import java.util.List;

import javax.rmi.CORBA.Util;

import cn.hd.base.BaseService;
import cn.hd.ws.action.WsGoodsAction;
import cn.hd.ws.dao.EcsGoodsExample.Criteria;

public class EcsGoodsService extends BaseService {
	private EcsGoodsMapper ecsgoodsMapper;
	private EcsGoodsActivityMapper ecsgoodsactivityMapper;
	public EcsGoodsActivityMapper getEcsgoodsactivityMapper() {
		return ecsgoodsactivityMapper;
	}

	public void setEcsgoodsactivityMapper(
			EcsGoodsActivityMapper ecsgoodsactivityMapper) {
		this.ecsgoodsactivityMapper = ecsgoodsactivityMapper;
	}

	private EcsCategoryMapper ecscategoryMapper;
	
	public EcsCategoryMapper getEcscategoryMapper() {
		return ecscategoryMapper;
	}

	public void setEcscategoryMapper(EcsCategoryMapper ecscategoryMapper) {
		this.ecscategoryMapper = ecscategoryMapper;
	}

	public EcsGoodsMapper getEcsgoodsMapper() {
		return ecsgoodsMapper;
	}

	public void setEcsgoodsMapper(EcsGoodsMapper ecsgoodsMapper) {
		this.ecsgoodsMapper = ecsgoodsMapper;
	}

	public EcsGoodsService(){
		initMapper("ecsgoodsMapper","ecscategoryMapper","ecsgoodsactivityMapper");
	}
	
	public List<EcsCategory> cats(){
		EcsGoodsActivity group = activeGroup();
		String act_cats = group.getActCats();
		String[] cats = act_cats.split(",");
		List<Short> values = new ArrayList<Short>();
		for (int i=0;i<cats.length;i++){
			if (cats[i].length()>0){
			 Short catid = Short.valueOf(cats[i]);
			 values.add(catid);
			}
		}
		if (values.size()>0){
		EcsCategoryExample example = new EcsCategoryExample();
		EcsCategoryExample.Criteria criteria = example.createCriteria();
		criteria.andCatIdIn(values);
		return ecscategoryMapper.selectByExample(example);
		}
		
		return new ArrayList<EcsCategory>();
	}
	
	public EcsGoodsActivityWithBLOBs activeGroup(){
		EcsGoodsActivityExample example = new EcsGoodsActivityExample();
		example.setOrderByClause("end_time desc");
		List<EcsGoodsActivityWithBLOBs> groups = ecsgoodsactivityMapper.selectByExampleWithBLOBs(example);
		return groups.get(0);
	}
	
	public EcsGoods find(int goodsid){
		EcsGoodsExample example = new EcsGoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(goodsid);
		List<EcsGoods> goods =  ecsgoodsMapper.selectByExampleWithBLOBs(example);
		if (goods.size()>0)
			return goods.get(0);
		
		return null;
	}
	
	public List<EcsGoods> goods(short catID){
		EcsGoodsExample example = new EcsGoodsExample();
		if (catID>0){
			Criteria criteria = example.createCriteria();
			criteria.andCatIdEqualTo(catID);
		}
		return ecsgoodsMapper.selectByExampleWithBLOBs(example);
	}
	
	public boolean add(EcsGoods record){
		try {
			ecsgoodsMapper.insert(record);
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}		
		return false;
	}
	
	public boolean updateByOrderGoods(List<EcsOrderGoods> list){
		try {
			for (int i=0;i<list.size();i++){
				EcsOrderGoods orderGoods = list.get(i);
				EcsGoods record = find(orderGoods.getGoodsId());
				if (record==null){
					continue;
				}
				short nowno = (short)(record.getGoodsNumber().shortValue()-orderGoods.getGoodsNumber().shortValue());
				if (nowno<0){
					System.out.println("错误库存操作，已<0:当前库存:"+record.getGoodsNumber()+",准备减少库存:"+orderGoods.getGoodsNumber());
					nowno = 0;
				}
				record.setGoodsNumber(Short.valueOf(nowno));
				ecsgoodsMapper.updateByPrimaryKeySelective(record);
			}
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
	public boolean update(EcsGoods record){
		try {
			ecsgoodsMapper.updateByPrimaryKeySelective(record);
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}		
		return true;
	}	
	
	public static void main(String[] args){
		EcsGoodsService ss = new EcsGoodsService();
	//	List<EcsCategory> aa = ss.cats();
	//	List<EcsGoods> goods = ss.goods((short)4);
		WsGoodsAction action = new WsGoodsAction();
		action.group();
	//	System.out.println(goods.get(1).getGoodsName()+";"+goods.get(1).getGoodsImg());
	}
}
