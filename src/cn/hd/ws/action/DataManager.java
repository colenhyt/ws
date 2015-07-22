package cn.hd.ws.action;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.tencent.common.TokenReqData;


public class DataManager {
	private BlockingQueue<String> queue;
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
     }
    
    public TokenReqData findReq(){
    	return tokenReq;
    }
    
    private void genOrderIds(){
    	for (int i=0;i<1000;i++){
    		String orderid = "NCTG"+String.valueOf(System.currentTimeMillis())+i;
    		queue.offer(orderid);
    	}    	
    }
	public String assignOrderSn(){
		String orderSn;
		if (queue.size()>0){
			orderSn = queue.poll();
		}else {
			genOrderIds();
			orderSn = queue.poll();
		}
		return orderSn;
	}
	
    public static void main(String[] args) {
    	DataManager stmgr = DataManager.getInstance();
    	for (int i=0;i<5000;i++)
    		System.out.println(stmgr.assignOrderSn());
    }
}
