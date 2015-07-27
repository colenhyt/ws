package cn.hd.ws.dao;

import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.ws.action.DataManager;
import cn.hd.ws.dao.EcsUsersExample.Criteria;
import cn.hd.wx.WxUserInfo;

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
	
	public List<EcsOrderGoods> findGoods(int orderId){
		EcsOrderGoodsExample example = new EcsOrderGoodsExample();
		EcsOrderGoodsExample.Criteria criteria = example.createCriteria();
		criteria.andOrderIdEqualTo(orderId);
		List<EcsOrderGoods> list = ecsOrderGoodsMapper.selectByExample(example);
		return list;
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
				record.setGoodsNumber(item.getGoodsNumber().shortValue());
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
	
	public boolean updateStatus(int orderId,int infoStatus){
		try {
			EcsOrderInfo record = new EcsOrderInfo();
			record.setOrderId(orderId);
			record.setInfoStatus(infoStatus);
			ecsOrderInfoMapper.updateByPrimaryKeySelective(record);
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
		WxUserInfo userinfo = new WxUserInfo();
		userinfo.setNickname("aaa");
		userinfo.setOpenid("1334");
		EcsUsers user = userService.findUserOrAdd(userinfo);
		
		EcsOrderInfo orderInfo = new EcsOrderInfo();
		orderInfo.setOrderSn(DataManager.getInstance().assignOrderSn());
		orderInfo.setUserId(user.getUserId());
		orderInfo.setAddress("add");
		orderInfo.setConsignee("contact");
		orderInfo.setMobile("123");
		orderInfo.setOrderStatus(false);
		orderInfo.setShippingStatus(false);
		orderInfo.setPayStatus(false);
		orderInfo.setPayNote("remark");
		
		EcsOrderService ecsorderService = new EcsOrderService();
		boolean ret = false;
//		ret = ecsorderService.add(orderInfo);		
	}
}
