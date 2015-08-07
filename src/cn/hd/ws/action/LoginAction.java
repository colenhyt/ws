package cn.hd.ws.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import net.sf.json.JSONObject;
import cn.hd.base.BaseAction;
import cn.hd.ws.dao.EcsRegion;
import cn.hd.ws.dao.EcsUserAddress;
import cn.hd.ws.dao.EcsUserService;
import cn.hd.ws.dao.EcsUsers;
import cn.hd.wx.WxUserInfo;

import com.alibaba.fastjson.JSON;
import com.tencent.common.Configure;
import com.tencent.common.HttpsRequest;
import com.tencent.common.TokenReqData;
import com.tencent.common.Util;
import com.tencent.protocol.order_protocol.JSWxConfigReqData;

public class LoginAction extends BaseAction {
	private EcsUserService ecsuserService;
	HttpsRequest request;
	
	public EcsUserService getEcsuserService() {
		return ecsuserService;
	}

	public void setEcsuserService(EcsUserService ecsuserService) {
		this.ecsuserService = ecsuserService;
	}
	
    public LoginAction(){
    	init("ecsuserService");  	
    	InputStream in = getHttpSession().getServletContext().getResourceAsStream(Configure.getCertLocalPath());
    	Configure.setIn(in);
		try {
			request = new HttpsRequest();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	private JSONObject queryWxUserInfo(String code)
	{
		String ip = super.getIpAddress();
		if (code==null){
			String state = this.getHttpRequest().getParameter("state");
			Util.log("微信回调返回code为空:state:"+state+",ip:"+ip);
			return null;
		}
		
		String tokenStr = DataManager.getInstance().findToken(code);
		if (tokenStr!=null){
			Util.log("ip:"+ip+",code:"+code+", 从cache 得到userinfo:"+tokenStr);
			return JSONObject.fromObject(tokenStr);
		}
		Util.log("ip:"+ip+",请求token:code="+code);
		String url = Configure.getAuthTokonAPI(code);
		int retrycount = 10;
		JSONObject jsonobj = null;
		for (int i=0;i<retrycount;i++){
			jsonobj = request.sendUrlPost(url);
			if (jsonobj.toString().indexOf("access_token")>0){
				break;
			}
		}
		if (jsonobj.toString().indexOf("errmsg")>0){
			Util.log("ip:"+ip+",请求token失败,code"+code+",errmsg:"+jsonobj.getString("errmsg"));
			//看是否有:
			String strLogin = DataManager.getInstance().findUserByKey("code",code);
			if (strLogin==null){
				Util.log("ip:"+ip+",code:"+code+",无法从code取缓存用户信息,尝试用ipaddress取....");
				strLogin = DataManager.getInstance().findUserByKey("ipAddress",ip);
				if (strLogin==null){
					Util.log("ip:"+ip+",code:"+code+",用ip取缓存用户信息失败.");
					return null;
				}
			}else {
				Util.log("ip:"+ip+",取缓存用户信息:"+strLogin);
				jsonobj = JSONObject.fromObject(strLogin);
				return jsonobj;
			}
		}else if (jsonobj.toString().indexOf("access_token")<0||jsonobj.toString().indexOf("openid")<0){
			Util.log("token返回字段失败: "+jsonobj.toString()+",ip:"+ip);
			return null;				
		}
		
		Util.log("ip:"+ip+",code:"+code+",得到token,请求userinfo...:");
		//根据token取用户信息:
		String access_token = jsonobj.getString("access_token");
		String openid = jsonobj.getString("openid");
		url = Configure.getUserInfoAPI(access_token, openid);
		for (int i=0;i<retrycount;i++){
			jsonobj = request.sendUrlPost(url);
			if (jsonobj.toString().indexOf("nickname")>0){
				break;
			}
		}
		if (jsonobj.toString().indexOf("nickname")>0){
			Util.log("ip:"+ip+",code:"+code+",userinfo获取成功:"+jsonobj.toString());
			DataManager.getInstance().addToken(code, jsonobj.toString());
		}else {
			Util.log("ip:"+ip+",userinfo获取失败:"+jsonobj.toString());
			jsonobj = null;
		}
		return jsonobj;
	}
	
	public String wxlogincallback()
	{
		String ip = super.getIpAddress();
		String code = this.getHttpRequest().getParameter("code");
		Util.log("用户进入:ip:"+ip);
		JSONObject jsonobj = queryWxUserInfo(code);
		//沒有取到授权，不允许进入:
		if (jsonobj==null){		//
	        Util.log("无授权用户不允许进入:ip:"+ip);
	        return "error";
		}
		
		WxUserInfo info0 = (WxUserInfo)JSONObject.toBean(jsonobj,WxUserInfo.class);
		
		WxUserInfo info = DataManager.getInstance().findUserByInfo(info0);
		if (info==null)
		{
			Util.log("取 userinfo/增加到数据库失败:");
			return "error";
		}
		

		info.setIpAddress(ip);
		info.setCode(code);
		
		DataManager.getInstance().addUser(info);	
		String jsonstr  = JSON.toJSONString(info);
		
		jsonstr = jsonstr.replace("\"", "'");
		getHttpRequest().setAttribute("userinfo", jsonstr);
		Util.log("进入团购页 :"+jsonstr);
		return "group";
	}
	
    
	public String wxjsinit()
	{	
		String jsonstr = null;
		String jstoken = null;
		TokenReqData reqData = DataManager.getInstance().tokenReq;
		if (!reqData.isTimeout()){
			jstoken = reqData.getJsTicket();
		}else {
			JSONObject jsonobj = request.sendUrlPost(Configure.getJSTokenAPI());
			String access_token = jsonobj.getString("access_token");
			if (access_token!=null){
				String expires_in = jsonobj.getString("expires_in");
				JSONObject jsonticket = request.sendUrlPost(Configure.getJSTicketAPI(access_token));
				jstoken = jsonticket.getString("ticket");
				reqData.reqWeixin(access_token, jstoken,Integer.valueOf(expires_in).longValue());
			}
		}
		if (jstoken!=null){
			JSWxConfigReqData req = new JSWxConfigReqData(jstoken);
			JSONObject jsonObject = JSONObject.fromObject(req);
			jsonstr = jsonObject.toString();
			jsonstr = jsonstr.replace("\"", "'");			
		}

		return jsonstr;
	}
	
	public static void main(String[] args){
		Exception e = new Exception("二次调用回调堆栈");
		//e2.printStackTrace();
		Util.log("heehehe".indexOf("aa"));
		
		Util.log(e.getMessage());
		StackTraceElement[] traces = e.getStackTrace();
		for (int i=0;i<traces.length;i++){
			Util.log(traces[i].getLineNumber()+traces[i].getMethodName()+traces[i].getFileName());
		}		
	}
}
