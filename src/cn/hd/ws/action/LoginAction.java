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
			Util.log("微信用户授权回调取不到code:state:"+state+",ip:"+ip);
			return null;
		}
		
		String url = Configure.getAuthTokonAPI(code);
		JSONObject jsonobj = request.sendUrlPost(url);
		if (jsonobj.toString().indexOf("errmsg")>0){
			Util.log("获取授权token失败,errmsg:"+jsonobj.getString("errmsg")+",ip:"+ip);
			//看是否有:
			String strLogin = DataManager.getInstance().getLoginStrByCode(code);
			if (strLogin!=null){
				Exception e = new Exception("二次调用回调堆栈");
				e.printStackTrace();
				Util.log("用户二次回调信息获取成功:"+strLogin);
				jsonobj = JSONObject.fromObject(strLogin);
				return jsonobj;
			}else {
				return null;
			}
		}else if (jsonobj.toString().indexOf("access_token")<0||jsonobj.toString().indexOf("openid")<0){
			Util.log("token返回没找到token/openid字段: "+jsonobj.toString()+",ip:"+ip);
			return null;				
		}
		
		//根据token取用户信息:
		String access_token = jsonobj.getString("access_token");
		String openid = jsonobj.getString("openid");
		url = Configure.getUserInfoAPI(access_token, openid);
		jsonobj = request.sendUrlPost(url);
		if (jsonobj.toString().indexOf("nickname")>0){
			Util.log("微信用户信息获取成功:"+jsonobj.toString()+",ip:"+ip);
		}else
			jsonobj = null;
		return jsonobj;
	}
	
	public String wxlogincallback()
	{
		String ip = super.getIpAddress();
		String code = this.getHttpRequest().getParameter("code");
		JSONObject jsonobj = queryWxUserInfo(code);
		//沒有取到授权，不允许进入:
		if (jsonobj==null){		//
	        Util.log("无授权用户不允许进入:ip:"+ip);
	        return "error";
		}
		
		WxUserInfo info = (WxUserInfo)JSONObject.toBean(jsonobj,WxUserInfo.class);
		
		EcsUsers user = ecsuserService.findUserOrAdd(info);
		if (user==null)
		{
			Util.log("userinfo 增加到数据库失败:");
			return "error";
		}
		
		info.setUserId(user.getUserId());
		EcsUserAddress add = ecsuserService.findActiveAddress(user.getUserId());
		if (add!=null){
			EcsRegion region = ecsuserService.findRegion(add.getProvince());
			if (region!=null)
				info.setProvince(region.getRegionName());
			region = ecsuserService.findRegion(add.getCity());
			if (region!=null)
				info.setCity(region.getRegionName());
			
			info.setAddress(add.getAddress());
			info.setMobile(add.getMobile());
			info.setContact(add.getConsignee());
		}
		info.setIpAddress(ip);
		
		DataManager.getInstance().addUser(info);	
		String jsonstr  = JSON.toJSONString(info);
		DataManager.getInstance().addCode(code, jsonstr);
		
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
		Exception e2 = new Exception("二次调用回调堆栈");
		//e2.printStackTrace();
		Util.log("heehehe");
        Runtime rt = Runtime.getRuntime();  
        String[] command1=new String[]{"cmd","cd C:/tomcat-7.0.57/bin"};  
        String command = "taskkill /F /IM java.exe";      
        try  
        {  
          rt.exec(command1);//返回一个进程  
          rt.exec(command);  
          System.out.println("success closed");  
          Thread.sleep(1000 * 1);
          rt.exec(command1);//返回一个进程  
          Process  p = rt.exec("startup.bat");
          InputStream in = p.getInputStream();

			int c;

			while ((c = in.read()) != -1) {

				System.out.print(c);//如果你不需要看输出，这行可以注销掉

			}

			in.close();
          System.out.println("success start");  
        }  
        catch (Exception e)  
        {  
          e.printStackTrace();  
        }		
	}
}
