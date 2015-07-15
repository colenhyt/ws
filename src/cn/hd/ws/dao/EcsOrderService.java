package cn.hd.ws.dao;

import cn.hd.base.BaseService;
import cn.hd.ws.action.DataManager;

public class EcsOrderService extends BaseService {
	private EcsOrderInfoMapper ecsOrderInfoMapper;
	
	public EcsOrderInfoMapper getEcsOrderInfoMapper() {
		return ecsOrderInfoMapper;
	}

	public void setEcsOrderInfoMapper(EcsOrderInfoMapper ecsOrderInfoMapper) {
		this.ecsOrderInfoMapper = ecsOrderInfoMapper;
	}

	public EcsOrderService(){
		initMapper("ecsOrderInfoMapper");
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
	
	public static void main(String[] args){
		EcsOrderInfo orderInfo = new EcsOrderInfo();
		orderInfo.setOrderSn(DataManager.getInstance().assignOrderSn());
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
