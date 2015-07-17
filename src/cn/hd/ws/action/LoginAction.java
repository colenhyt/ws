package cn.hd.ws.action;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import net.sf.json.JSONObject;
import cn.hd.base.BaseAction;
import cn.hd.wx.WxUserInfo;

import com.tencent.common.Configure;
import com.tencent.common.HttpsRequest;
import com.tencent.common.Util;

public class LoginAction extends BaseAction {
    private String loginUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx14be2d51e8ad9693&redirect_uri=http://www.egonctg.com/ec/login_wxlogincallback.do&response_type=code&scope=snsapi_userinfo&state=aaa#wechat_redirect";

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
						jsonobj = JSONObject.fromObject(result);
						WxUserInfo info = (WxUserInfo)JSONObject.toBean(jsonobj, WxUserInfo.class);
						getHttpRequest().setAttribute("userinfo", result);
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
		getHttpRequest().setAttribute("userinfo", "{'openid':'333','nickname':'aaeee','province':'广东','city':'深圳'}");
		return "group";
	}
}
