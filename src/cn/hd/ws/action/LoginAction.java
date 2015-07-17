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
import com.tencent.common.Util;

public class LoginAction extends BaseAction {
    private String loginUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx14be2d51e8ad9693&redirect_uri=http://www.egonctg.com/ec/login_wxlogincallback.do&response_type=code&scope=snsapi_userinfo&state=aaa#wechat_redirect";
	private EcsUserService ecsuserService;
	
	public EcsUserService getEcsuserService() {
		return ecsuserService;
	}

	public void setEcsuserService(EcsUserService ecsuserService) {
		this.ecsuserService = ecsuserService;
	}
	
    public LoginAction(){
    	init("ecsuserService");  	
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
			HttpsRequest request;
	    	InputStream in = getHttpSession().getServletContext().getResourceAsStream(Configure.getCertLocalPath());
	    	Configure.setIn(in);
			try {
				request = new HttpsRequest();
				String result = request.sendUrlPost(url);
				JSONObject jsonobj = JSONObject.fromObject(result);
				String access_token = jsonobj.getString("access_token");
				if (access_token==null){
					Util.log("wx token request fail:"+result);
				}else {
					String openid = jsonobj.getString("openid");
					url = Configure.getUserInfoAPI(access_token, openid);
					result = request.sendUrlPost(url);
					if (result.indexOf("nickname")>0){
						Util.log("wxauth userinfo:"+result);
						jsonobj = JSONObject.fromObject(result);
						WxUserInfo info = (WxUserInfo)JSONObject.toBean(jsonobj, WxUserInfo.class);
						EcsUsers user = ecsuserService.find(info.getOpenid());
						String jsonstr = result;
						if (user!=null){
							EcsUserAddress add = ecsuserService.findActiveAddress(user.getUserId());
							info.setAddress(add.getAddress());
							info.setMobile(add.getMobile());
							info.setContact(add.getConsignee());
							jsonstr = JSON.toJSONString(info);
						}
						getHttpRequest().setAttribute("userinfo", jsonstr);
						WsGoodsAction goods =new WsGoodsAction();
						goods.queryWxpay(info.getOpenid());
						Util.log("request userinfo return :"+info.getNickname()+";"+info.getOpenid());
						return null;						
					}

				}
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
		getHttpRequest().setAttribute("userinfo", "{'openid':'333','nickname':'aaeee','province':'广东省','city':'深圳市','address':'abcd南山','contact':'colenhh','mobile':'134'}");
		return "group";
	}
}
