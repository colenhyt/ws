package cn.hd.ws.dao;

import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.ws.action.DataManager;

public class EcsOrderService extends BaseService {
	private EcsOrderInfoMapper ecsOrderInfoMapper;
	private EcsOrderGoodsMapper ecsOrderGoodsMapper;
	
	public EcsOrderGoodsMapper getEcsOrderGoodsMapper() {
		return ecsOrderGoodsMapper;
	}

	public void setEcsOrderGoodsMapper(EcsOrderGoodsMapper ecsOrderGoodsMapper) {
		this.ecsOrderGoodsMapper = ecsOrderGoodsMapper;
	}

	public EcsOrderInfoMapper getEcsOrderInfoMapper() {
		return ecsOrderInfoMapper;
	}

	public void setEcsOrderInfoMapper(EcsOrderInfoMapper ecsOrderInfoMapper) {
		this.ecsOrderInfoMapper = ecsOrderInfoMapper;
	}

	public EcsOrderService(){
		initMapper("ecsOrderInfoMapper","ecsOrderGoodsMapper");
	}
	
	public boolean addGoods(List<EcsGoods> goods,int orderId){
		try {
			for (int i=0;i<goods.size();i++){	
				EcsGoods item = goods.get(i);
				EcsOrderGoods record = new EcsOrderGoods();
				record.setOrderId(orderId);
				record.setGoodsId(item.getGoodsId());
				if (item.getIsReal()==1)
				record.setIsReal(true);
				record.setGoodsSn(item.getGoodsSn());
				record.setGoodsNumber(item.getGoodsNumber());
				record.setGoodsName(item.getGoodsName());
				record.setGoodsPrice(item.getShopPrice());
				record.setMarketPrice(item.getShopPrice());
				ecsOrderGoodsMapper.insert(record);
			}
			DBCommit();
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}		
	}	
	public boolean add(EcsOrderInfo record){
		try {
			ecsOrderInfoMapper.insert(record);
			DBCommit();
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}		
	}
	
	public boolean update(EcsOrderInfo record){
		try {
			ecsOrderInfoMapper.updateByPrimaryKeySelective(record);
			DBCommit();
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}		
	}
	
	public static void main(String[] args){
		EcsUserService userService= new EcsUserService();
		int userid = userService.findUserIdOrAdd("colen黄3334");
		
		EcsOrderInfo orderInfo = new EcsOrderInfo();
		orderInfo.setOrderSn(DataManager.getInstance().assignOrderSn());
		orderInfo.setUserId(userid);
		orderInfo.setAddress("add");
		orderInfo.setConsignee("contact");
		orderInfo.setMobile("123");
		orderInfo.setOrderStatus(false);
		orderInfo.setShippingStatus(false);
		orderInfo.setPayStatus(false);
		orderInfo.setPayNote("remark");
		
		EcsOrderService ecsorderService = new EcsOrderService();
		boolean ret = false;
		ret = ecsorderService.add(orderInfo);		
	}
}
