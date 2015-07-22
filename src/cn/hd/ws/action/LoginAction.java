package cn.hd.ws.action;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import net.sf.json.JSONObject;
import cn.hd.base.BaseAction;
import cn.hd.ws.dao.EcsUserAddress;
import cn.hd.ws.dao.EcsUserService;
import cn.hd.ws.dao.EcsUsers;
import cn.hd.wx.WxUserInfo;

import com.alibaba.fastjson.JSON;
import com.tencent.common.Configure;
import com.tencent.common.HttpsRequest;
import com.tencent.common.JSReqData;
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
    
	public String wxlogincallback()
	{
		String code = this.getHttpRequest().getParameter("code");
		String state = this.getHttpRequest().getParameter("state");
		if (code==null){
			Util.log("wx request failt,state:"+state);
		}else 
		{
			String url = Configure.getAuthTokonAPI(code);
			Util.log("request wx token:url="+url);
			JSONObject jsonobj = request.sendUrlPost(url);
			Util.log("rev token return :"+jsonobj.toString());
			if (jsonobj.toString().indexOf("errmsg")>0){
				Util.log("reuqest token error:"+jsonobj.getString("errmsg"));
				return "error";
			}else if (jsonobj.toString().indexOf("access_token")<0||jsonobj.toString().indexOf("openid")<0){
				Util.log("no token/openid found:"+jsonobj.toString());
				return "error";				
			}
			String access_token = jsonobj.getString("access_token");
			String openid = jsonobj.getString("openid");
			url = Configure.getUserInfoAPI(access_token, openid);
			jsonobj = request.sendUrlPost(url);
			if (jsonobj.toString().indexOf("nickname")>0){
				Util.log("微信用户信息获取成功:"+jsonobj.toString());
				WxUserInfo info = (WxUserInfo)JSONObject.toBean(jsonobj, WxUserInfo.class);
				EcsUsers user = ecsuserService.find(info.getOpenid());
				String jsonstr = jsonobj.toString();
				if (user!=null){
					EcsUserAddress add = ecsuserService.findActiveAddress(user.getUserId());
					info.setAddress(add.getAddress());
					info.setMobile(add.getMobile());
					info.setContact(add.getConsignee());
					jsonstr = JSON.toJSONString(info);
				}
				jsonstr = jsonstr.replace("\"", "'");
				getHttpRequest().setAttribute("userinfo", jsonstr);
				Util.log("request userinfo return :"+jsonstr);
				return "group";						
			}


		}
		getHttpRequest().setAttribute("userinfo", "{'openid':'333','nickname':'aaeee','province':'广东省','city':'深圳市','address':'abcd南山','contact':'colenhh','mobile':'134'}");
		return "error";
	}
	
    
	public String wxjsinit()
	{	
		String jsonstr = null;
		String jstoken = null;
		JSReqData reqData = DataManager.getInstance().jsReq;
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
}
