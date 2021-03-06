package com.tencent.protocol.order_protocol;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.tencent.common.Configure;
import com.tencent.common.RandomStringGenerator;
import com.tencent.common.Signature;

public class JSWxConfigReqData {
	private String appid = null;
	private String timestamp = null;
	private String noncestr = null;
	private String url = null;
	private String jsapi_ticket = null;
	private String sign = null;
	private String configStr = null;
	
	public String getConfigStr() {
		return configStr;
	}

	public void setConfigStr(String configStr) {
		this.configStr = configStr;
	}

	public String getJsapi_ticket() {
		return jsapi_ticket;
	}

	public void setJsapi_ticket(String jsapi_ticket) {
		this.jsapi_ticket = jsapi_ticket;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public JSWxConfigReqData(String jsapi_ticket){
		
		setJsapi_ticket(jsapi_ticket);
		
		setUrl(Configure.getConfigUrl());
		
		//timestamp
		setTimestamp(String.valueOf(System.currentTimeMillis()/1000));

        //随机字符串，不长于32 位
        setNoncestr(RandomStringGenerator.getRandomStringByLength(32));  

//        setConfigStr(Signature.getStringOrder(toMap()));
        
        //根据API给的签名规则进行签名
        String sign = Signature.getSign_SHA1(toMap());
        setSign(sign);//把签名数据设置到Sign这个属性中        
		
        //微信分配的公众号ID（开通公众号之后可以获取到）
        setAppid(Configure.getAppid());
        
	}
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String nonceStr) {
		this.noncestr = nonceStr;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Map<String,Object> toMap(){
	    Map<String,Object> map = new HashMap<String, Object>();
	    Field[] fields = this.getClass().getDeclaredFields();
	    for (Field field : fields) {
	        Object obj;
	        try {
	            obj = field.get(this);
	            if(obj!=null){
            		map.put(field.getName(), obj);
	            }
	        } catch (IllegalArgumentException e) {
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        }
	    }
	    return map;
	}
	
	
}
