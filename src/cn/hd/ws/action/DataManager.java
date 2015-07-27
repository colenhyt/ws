package cn.hd.ws.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.sf.json.JSONObject;
import cn.hd.ws.dao.EcsOrderInfo;
import cn.hd.wx.WxUserInfo;

import com.tencent.common.TokenReqData;


public class DataManager {
	private BlockingQueue<String> queue;
	private Map<Integer,String>	userInfoMap;
	private Map<String,String> orderInfoMap;
	public TokenReqData tokenReq;
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
    	userInfoMap  = new HashMap<Integer,String>();
     }
    
    public TokenReqData findReq(){
    	return tokenReq;
    }
    
    public void addUser(WxUserInfo info){
    	JSONObject infoobj = JSONObject.fromObject(info);
    	synchronized(DataManager.class){
    		userInfoMap.put(info.getUserId(), infoobj.toString());  
    	}
    }
    
    public WxUserInfo findUser(int userId){
    	synchronized(DataManager.class){
    	String str = userInfoMap.get(userId);
    	if (str==null) return null;
    	
    	JSONObject obj = JSONObject.fromObject(str);
    	return (WxUserInfo)JSONObject.toBean(obj,WxUserInfo.class);
    	}
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
