package cn.hd.ws.action;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Enumeration;

import cn.hd.base.BaseAction;

import com.tencent.common.HttpsRequest;

public class WxAction extends BaseAction {
	private String URL = "";
	
	public String code(){
		String code = this.getHttpRequest().getParameter("code");
		String state = this.getHttpRequest().getParameter("state");
		if (code==null){
			
		}else {
			String url = URL + "code="+code;
			HttpsRequest request;
			try {
				request = new HttpsRequest();
				String result = request.sendUrlPost(url);
				String token = this.getHttpRequest().getParameter("token");
				String openid = this.getHttpRequest().getParameter("token");
				System.out.println("code send return :"+result+"; "+token+";"+openid);
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
	   return null;
	}
	
	public String query()
	{
		System.out.println("'bbbbbb");
		Enumeration<String> myEnumeration = this.getHttpRequest().getParameterNames();
		while (myEnumeration.hasMoreElements()){
            System.out.println(myEnumeration.nextElement());
		} 
		String code = this.getHttpRequest().getParameter("Token");
		String state = this.getHttpRequest().getParameter("state");
		System.out.println("callback"+code+";"+state);
		Enumeration<String> myEnumeration2 = this.getHttpRequest().getAttributeNames();
		while (myEnumeration2.hasMoreElements()){
            System.out.println(myEnumeration2.nextElement());
		} 		return null;
	}
}
