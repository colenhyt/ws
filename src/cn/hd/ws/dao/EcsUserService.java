package cn.hd.ws.dao;

import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.ws.dao.EcsUsersExample.Criteria;
import cn.hd.wx.WxUserInfo;

public class EcsUserService extends BaseService {
	private EcsUsersMapper ecsUsersMapper;
	private EcsUserAddressMapper ecsUserAddressMapper;
	private EcsRegionMapper	ecsRegionMapper;
	
	public EcsRegionMapper getEcsRegionMapper() {
		return ecsRegionMapper;
	}

	public void setEcsRegionMapper(EcsRegionMapper ecsRegionMapper) {
		this.ecsRegionMapper = ecsRegionMapper;
	}

	public EcsUserAddressMapper getEcsUserAddressMapper() {
		return ecsUserAddressMapper;
	}

	public void setEcsUserAddressMapper(EcsUserAddressMapper ecsUserAddressMapper) {
		this.ecsUserAddressMapper = ecsUserAddressMapper;
	}

	public EcsUsersMapper getEcsUsersMapper() {
		return ecsUsersMapper;
	}

	public void setEcsUsersMapper(EcsUsersMapper ecsUsersMapper) {
		this.ecsUsersMapper = ecsUsersMapper;
	}

	public EcsUsers find(String openid)
	{
		EcsUsersExample example = new EcsUsersExample();
		Criteria criteria = example.createCriteria();
		criteria.andOpenidEqualTo(openid);
		List<EcsUsers> users = ecsUsersMapper.selectByExample(example);
		if (users.size()>0)
			return users.get(0);
		
		return null;
	}
	
	public boolean addAddress(EcsUserAddress record)
	{
		try{
			ecsUserAddressMapper.insert(record);
			DBCommit();
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public EcsUserAddress findActiveAddress(Integer userid)
	{
		EcsUserAddressExample example = new EcsUserAddressExample();
		EcsUserAddressExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userid);
		List<EcsUserAddress> list = ecsUserAddressMapper.selectByExample(example);	
		if (list.size()>0){
			return list.get(0);
		}
		
		return null;
	}
	
	public boolean validAddress(EcsOrderInfo order)
	{
		EcsUserAddressExample example = new EcsUserAddressExample();
		EcsUserAddressExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(order.getUserId());
		criteria.andConsigneeEqualTo(order.getConsignee());
		criteria.andProvinceEqualTo(order.getProvince());
		criteria.andCityEqualTo(order.getCity());
		criteria.andAddressEqualTo(order.getAddress().toLowerCase());
		List<EcsUserAddress> list = ecsUserAddressMapper.selectByExample(example);
		EcsUserAddress address;
		if (list.size()>0){
			address = list.get(0);
			return true;
		}else {
			address = new EcsUserAddress();
			address.setUserId(order.getUserId());
			address.setConsignee(order.getConsignee());
			address.setProvince(order.getProvince());
			address.setCity(order.getCity());
			address.setAddress(order.getAddress());
			address.setMobile(order.getMobile());
			address.setZipcode(order.getZipcode());
			address.setTel(order.getTel());
			address.setEmail(order.getEmail());
			address.setCountry(order.getCountry());
			return addAddress(address);
		}
	}
	
	public int findUserIdOrAdd(WxUserInfo userInfo){
		EcsUsers user = find(userInfo.getOpenid());
		if (user!=null){
			return user.getUserId();
		}else {
			EcsUsers record = new EcsUsers();
			record.setUserName(userInfo.getNickname());
			record.setOpenid(userInfo.getOpenid());
			if (userInfo.getSex()!=null&&userInfo.getSex().length()>0){
				Integer sex = Integer.valueOf(userInfo.getSex());
				if (sex==1)
					record.setSex(true);
				else
					record.setSex(false);				
			}

//			record.setSex(userInfo.);
			boolean added = add(record);
			if (added)
			 return record.getUserId();
			else
			 return -1;
		}
			
	}
	
	public EcsUserService(){
		initMapper("ecsUsersMapper","ecsUserAddressMapper","ecsRegionMapper");
	}
	
	public EcsRegion findRegion(String regionName){
		EcsRegionExample example = new EcsRegionExample();
		EcsRegionExample.Criteria criteria = example.createCriteria();
		criteria.andRegionNameLike(regionName);
		List<EcsRegion> list = ecsRegionMapper.selectByExample(example);
		if (list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public boolean add(EcsUsers record){
		try {
			ecsUsersMapper.insert(record);
			DBCommit();
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}		
	}
	
}
