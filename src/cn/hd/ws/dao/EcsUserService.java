package cn.hd.ws.dao;

import java.util.List;

import cn.hd.base.BaseService;
import cn.hd.ws.dao.EcsUsersExample.Criteria;

public class EcsUserService extends BaseService {
	private EcsUsersMapper ecsUsersMapper;
	
	public EcsUsersMapper getEcsUsersMapper() {
		return ecsUsersMapper;
	}

	public void setEcsUsersMapper(EcsUsersMapper ecsUsersMapper) {
		this.ecsUsersMapper = ecsUsersMapper;
	}

	public EcsUsers find(String userName)
	{
		EcsUsersExample example = new EcsUsersExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserNameEqualTo(userName);
		List<EcsUsers> users = ecsUsersMapper.selectByExample(example);
		if (users.size()>0)
			return users.get(0);
		
		return null;
	}
	
	public int findUserIdOrAdd(String userName){
		EcsUsers user = find(userName);
		if (user!=null)
			return user.getUserId();
		else {
			EcsUsers record = new EcsUsers();
			record.setUserName(userName);
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
