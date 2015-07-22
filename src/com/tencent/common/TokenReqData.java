package com.tencent.common;

public class TokenReqData {
	private String accessToken = null;
	private String jsTicket = null;
	private long timeOut = 3600;
	private String openid = null;
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	private long reqStart = 0;		//请求时间
	
	public void reqWeixinAuth(String accessToken,String openid,long expire){
		reqStart = System.currentTimeMillis();
		this.timeOut = expire/2;
		this.accessToken = accessToken;
		this.openid = openid;		
	}
	
	public void reqWeixin(String accessToken,String jsTicket,long expire){
		reqStart = System.currentTimeMillis();
		this.timeOut = expire/2;
		this.accessToken = accessToken;
		this.jsTicket = jsTicket;
	}
	
	public boolean isTimeout(){
		long now = System.currentTimeMillis();
		return (now-reqStart)>timeOut*1000;
	}
	
	public long getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getJsTicket() {
		return jsTicket;
	}
	public void setJsTicket(String jsTicket) {
		this.jsTicket = jsTicket;
	}
	
}
