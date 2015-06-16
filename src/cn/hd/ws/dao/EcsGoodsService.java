package cn.hd.ws.dao;

import java.util.List;

import cn.hd.base.BaseService;

public class EcsGoodsService extends BaseService {
	private EcsGoodsMapper ecsgoodsMapper;
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
		initMapper("ecsgoodsMapper","ecscategoryMapper");
	}
	
	public List<EcsCategory> cat(){
		EcsCategoryExample example = new EcsCategoryExample();
		return ecscategoryMapper.selectByExample(example);
	}
	
	public List<EcsGoods> list(){
		EcsGoodsExample example = new EcsGoodsExample();
		return ecsgoodsMapper.selectByExample(example);
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
	
	public static void main(String[] args){
		EcsGoodsService ss = new EcsGoodsService();
		System.out.println(ss.cat().size());
	}
}
