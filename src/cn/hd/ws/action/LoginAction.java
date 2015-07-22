package cn.hd.ws.action;

import java.io.IOException;
import java.io.InputStream;
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
    
	public String wxlogincallback()
	{
		String ip = super.getIpAddress();
		String code = this.getHttpRequest().getParameter("code");
		String state = this.getHttpRequest().getParameter("state");
		if (code==null){
			Util.log("微信用户授权回调取不到code:"+state+",ip:"+ip);
		}else 
		{
			String url = Configure.getAuthTokonAPI(code);
			JSONObject jsonobj = request.sendUrlPost(url);
			if (jsonobj.toString().indexOf("errmsg")>0){
				Util.log("获取授权token失败,errmsg:"+jsonobj.getString("errmsg")+",ip:"+ip);
				return "error";
			}else if (jsonobj.toString().indexOf("access_token")<0||jsonobj.toString().indexOf("openid")<0){
				Util.log("token返回没找到token/openid字段: "+jsonobj.toString()+",ip:"+ip);
				return "error";				
			}
			String access_token = jsonobj.getString("access_token");
			String openid = jsonobj.getString("openid");
			url = Configure.getUserInfoAPI(access_token, openid);
			jsonobj = request.sendUrlPost(url);
			if (jsonobj.toString().indexOf("nickname")>0){
				Util.log("微信用户信息获取成功:"+jsonobj.toString());
				WxUserInfo info = (WxUserInfo)JSONObject.toBean(jsonobj,WxUserInfo.class);
				EcsUsers user = ecsuserService.findUserOrAdd(info);
				if (user==null)
				{
					Util.log("user 获取数据失败:"+jsonobj.toString());
					return "error";
				}
				String jsonstr = jsonobj.toString();
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
					jsonstr = JSON.toJSONString(info);
				}
				String loginIp = getIpAddress();
				info.setIpAddress(loginIp);
				DataManager.getInstance().addUser(info);
				jsonstr = jsonstr.replace("\"", "'");
				getHttpRequest().setAttribute("userinfo", jsonstr);
				Util.log("用户信息获取成功，跳转到团购页 :"+jsonstr);
				return "group";						
			}
		}
		String loginIp = getIpAddress();
		WxUserInfo info = new WxUserInfo();
		info.setOpenid("333");
		info.setIpAddress(loginIp);
		EcsUsers user = ecsuserService.findUserOrAdd(info);		
		info.setUserId(user.getUserId());
		DataManager.getInstance().addUser(info);
		getHttpRequest().setAttribute("userinfo", "{'userId':"+info.getUserId()+",'openid':'333','nickname':'aaeee','province':'广东','city':'深圳','address':'abcd南山','contact':'colenhh','mobile':'134'}");
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
}
