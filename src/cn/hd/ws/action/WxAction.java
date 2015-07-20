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
