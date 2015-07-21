package com.tencent.protocol.order_protocol;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.tencent.common.Configure;
import com.tencent.common.RandomStringGenerator;
import com.tencent.common.Signature;

public class JSChooseWXPayReqData {
	private String appId;
	private String timeStamp = null;
	private String nonceStr = null;
	private String prepay_id = null;
	private String signType = "MD5";
	private String sign = "";
	
	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timestamp) {
		this.timeStamp = timestamp;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPrepay_id() {
		return prepay_id;
	}

	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}

	public JSChooseWXPayReqData(String prepay_id){
		
		//timestamp
		setTimeStamp(String.valueOf(System.currentTimeMillis()/1000));
		
        //微信分配的公众号ID（开通公众号之后可以获取到）
        setAppId(Configure.getAppid());

        //随机字符串，不长于32 位
        setNonceStr(RandomStringGenerator.getRandomStringByLength(32));  

        //prepay_id:
        setPrepay_id("prepay_id="+prepay_id);
        
        //根据API给的签名规则进行签名
        String sign = Signature.getSign(toMap());
        setSign(sign);//把签名数据设置到Sign这个属性中        
	}
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appid) {
		this.appId = appid;
	}
	
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
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
	            	if (field.getName().endsWith("prepay_id"))
	            		map.put("package", obj);
	            	else
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
