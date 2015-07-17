package cn.hd.ws.dao;

import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.ws.dao.EcsUsersExample.Criteria;
import cn.hd.wx.WxUserInfo;

public class EcsUserService extends BaseService {
	private EcsUsersMapper ecsUsersMapper;
	
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
	
	public int findUserIdOrAdd(WxUserInfo userInfo){
		EcsUsers user = find(userInfo.getOpenid());
		if (user!=null)
			return user.getUserId();
		else {
			EcsUsers record = new EcsUsers();
			record.setUserName(userInfo.getNickname());
			record.setOpenid(userInfo.getOpenid());
			add(record);
			return record.getUserId();
		}
			
	}
	
	public EcsUserService(){
		initMapper("ecsUsersMapper");
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
