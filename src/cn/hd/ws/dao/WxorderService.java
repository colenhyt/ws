package cn.hd.ws.dao;

import cn.hd.base.BaseService;

public class WxorderService extends BaseService {
	private WxorderMapper wxorderMapper;
	
	public WxorderMapper getWxorderMapper() {
		return wxorderMapper;
	}

	public void setWxorderMapper(WxorderMapper wxorderMapper) {
		this.wxorderMapper = wxorderMapper;
	}

	public WxorderService(){
		initMapper("wxorderMapper");
	}
	
	public boolean add(Wxorder record){
		try {
			wxorderMapper.insert(record);
			DBCommit();
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}		
		return false;
	}
}
