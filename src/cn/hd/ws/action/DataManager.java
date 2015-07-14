package cn.hd.ws.action;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class DataManager {
	private BlockingQueue<String> queue;
    private static DataManager uniqueInstance = null;  
	
    public static DataManager getInstance() {  
        if (uniqueInstance == null) {  
            uniqueInstance = new DataManager();  
        }  
        return uniqueInstance;  
     } 
    
    public DataManager(){
    	queue  = new LinkedBlockingQueue<String>();

     }
    
    private void genOrderIds(){
    	for (int i=0;i<1000;i++){
    		String orderid = "NCTG"+String.valueOf(System.currentTimeMillis())+i;
    		queue.offer(orderid);
    	}    	
    }
	public String assignOrderId(){
		String orderId;
		if (queue.size()>0){
			orderId = queue.poll();
		}else {
			genOrderIds();
			orderId = queue.poll();
		}
		return orderId;
	}
	
    public static void main(String[] args) {
    	DataManager stmgr = DataManager.getInstance();
    	for (int i=0;i<5000;i++)
    		System.out.println(stmgr.assignOrderId());
    }
}
