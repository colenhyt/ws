package cn.hd.ws.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.sf.json.JSONObject;
import cn.hd.ws.dao.EcsOrderInfo;
import cn.hd.ws.dao.EcsRegion;
import cn.hd.ws.dao.EcsUserAddress;
import cn.hd.ws.dao.EcsUserService;
import cn.hd.ws.dao.EcsUsers;
import cn.hd.wx.WxUserInfo;

import com.tencent.common.TokenReqData;


public class DataManager {
	private BlockingQueue<String> queue;
	private Map<String,String>	userInfoMap;
	private Map<String,String>	tokenInfoMap;
	private Map<String,String> orderInfoMap;
	public TokenReqData tokenReq;
	private EcsUserService userService;
    private static DataManager uniqueInstance = null;  
	
    public static DataManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new DataManager();  
        }  
        return uniqueInstance;  
     } 
    
    public DataManager(){
    	queue  = new LinkedBlockingQueue<String>();
    	tokenReq = new TokenReqData();
    	orderInfoMap = new HashMap<String,String>();
    	userInfoMap  = new HashMap<String,String>();
    	tokenInfoMap = new HashMap<String,String>();
     }
    
    private void _setAddress(WxUserInfo info,EcsUserAddress add){
		if (add!=null){
			EcsRegion region = userService.findRegion(add.getProvince());
			if (region!=null){
				info.setProvince(region.getRegionName());
				info.setProvinceid(region.getRegionId());
			}
			region = userService.findRegion(add.getCity());
			if (region!=null){
				info.setCity(region.getRegionName());
				info.setCityid(region.getRegionId());
			}
			
			info.setAddress(add.getAddress());
			info.setMobile(add.getMobile());
			info.setContact(add.getConsignee());
		}    	
    }
    
    public void initData(){
    	userService = new EcsUserService();
    	List<EcsUsers> users = userService.findUsers();
    	for (int i=0;i<users.size();i++){
    		EcsUsers user = users.get(i);
    		WxUserInfo info = new WxUserInfo();
    		info.setUserId(user.getUserId());
    		info.setOpenid(user.getOpenid());
    		info.setNickname(user.getUserName());
    		if (info.getOpenid()!=null&&info.getOpenid().equalsIgnoreCase("olcTqjimfsr539BUa5dR9fEAM74c"))
    			info.setCode("aaa");
    		EcsUserAddress add = userService.findActiveAddress(user.getUserId());
    		_setAddress(info,add);
    		addUser(info);
    	}
    }
    
    public TokenReqData findReq(){
    	return tokenReq;
    }
    
    public String findUserByKey(String strKey,String strValue){
    	synchronized(DataManager.class){
    		Set<String> keyset = userInfoMap.keySet();
    		Iterator<String> iter = keyset.iterator();
    		while (iter.hasNext()){
    			String jsonstr = userInfoMap.get(iter.next());
            	JSONObject obj = JSONObject.fromObject(jsonstr);
            	if (obj.get(strKey).toString().equalsIgnoreCase(strValue)){
            		return obj.toString();
            	}
    		}
    		return null;
    	}
    }
    
    
    public void addUser(WxUserInfo info){
    	synchronized(DataManager.class){
        	JSONObject infoobj = JSONObject.fromObject(info);
    		userInfoMap.put(info.getOpenid(), infoobj.toString());  
    	}
    }
    
    public boolean validUserAddress(WxUserInfo userInfo,EcsOrderInfo order){
    	synchronized(DataManager.class){
    		//new address:
    		if (!userInfo.getContact().equalsIgnoreCase(order.getConsignee())||!userInfo.getAddress().equalsIgnoreCase(order.getAddress())||
    				!userInfo.getMobile().equalsIgnoreCase(order.getMobile())||userInfo.getProvinceid()!=order.getProvince().shortValue()
    				||userInfo.getCityid()!=order.getCity().shortValue()){
    			EcsUserService userService = new EcsUserService();
    			EcsUserAddress add = userService.addAddressWithOrder(order);
    			if (add!=null)
    				_setAddress(userInfo,add);
    			else
    				return false;
    		}
    		return true;
    	}
    }
    
    public WxUserInfo findUser(String openId){
    	synchronized(DataManager.class){
    	String str = userInfoMap.get(openId);
    	if (str==null) return null;
    	
    	JSONObject obj = JSONObject.fromObject(str);
    	return (WxUserInfo)JSONObject.toBean(obj,WxUserInfo.class);
    	}
    }
    
    public WxUserInfo findUserByInfo(WxUserInfo userInfo){
    	synchronized(DataManager.class){
    	WxUserInfo info = findUser(userInfo.getOpenid());
    	if (info==null){
    		EcsUserService ecsuserService = new EcsUserService();
    		EcsUsers user = ecsuserService.addWithInfo(userInfo);
    		if (user!=null){
	    		JSONObject jsonobj = JSONObject.fromObject(userInfo);
	    		info = (WxUserInfo)JSONObject.toBean(jsonobj,WxUserInfo.class);
	    		info.setUserId(user.getUserId());
	    		EcsRegion region = ecsuserService.findRegion(userInfo.getProvince());
				if (region!=null){
					info.setProvince(region.getRegionName());
					info.setProvinceid(region.getRegionId());
				}
				region = ecsuserService.findRegion(userInfo.getCity());
				if (region!=null){
					info.setCity(region.getRegionName());
					info.setCityid(region.getRegionId());
				}
    		}
    		info.setAddress(userInfo.getAddress());
    	}
    	return info;
    	}
    }
    
    public String findToken(String code){
    	synchronized(DataManager.class){
    		if (tokenInfoMap.containsKey(code))
    			return tokenInfoMap.get(code);
    		return null;
    	}
    } 
    
    public boolean addToken(String code,String tokenStr){
    	synchronized(DataManager.class){
    	tokenInfoMap.put(code, tokenStr);
    	}
    	return true;
    } 
    
    public boolean addOrder(EcsOrderInfo orderInfo){
    	JSONObject infoobj = JSONObject.fromObject(orderInfo);
    	synchronized(DataManager.class){
    	orderInfoMap.put(orderInfo.getOrderSn(), infoobj.toString());
    	}
    	return true;
    }   
    
    public String findOrder(String orderSn){
    	synchronized(DataManager.class){
    	if (!orderInfoMap.containsKey(orderSn))
    		return null;
    	return orderInfoMap.get(orderSn);
    	}
    }
    
    private void genOrderIds(){
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间  
        sdf.applyPattern("yyyyMMddHHmmss");// a为am/pm的标记  
        Date date = new Date();// 获取当前时间      		
        synchronized(DataManager.class){
    	for (int i=0;i<3000;i++){
    		String orderid = "NCTG"+sdf.format(date)+i;
    		queue.offer(orderid);
    	}    	
        }
    }
	public String assignOrderSn(){
		String orderSn;
		synchronized(DataManager.class){
			if (queue.size()>0){
				orderSn = queue.poll();
			}else {
				genOrderIds();
				orderSn = queue.poll();
			}
		}
		return orderSn;
	}
	
    public static void main(String[] args) {
    	DataManager stmgr = DataManager.getInstance();
    	for (int i=0;i<5000;i++)
    		System.out.println(stmgr.assignOrderSn());
    }
}
