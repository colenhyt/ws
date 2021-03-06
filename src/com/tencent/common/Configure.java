package com.tencent.common;

import java.io.InputStream;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 14:40
 * 这里放置各种配置数据
 */
public class Configure {

	//sdk的版本号
	private static final String sdkVersion = "java sdk 1.0.1";

	private static InputStream in = null;
//这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
	// 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
	// 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改

	public static InputStream getIn() {
		return in;
	}

	public static void setIn(InputStream in) {
		Configure.in = in;
	}

	private static String tradeType = "JSAPI";
	
	public static String getTradeType() {
		return tradeType;
	}

	public static void setTradeType(String tradeType) {
		Configure.tradeType = tradeType;
	}

	private static String configUrl = "http://www.egonctg.com/ec/login_wxlogincallback.do";

	public static String getConfigUrl() {
		return configUrl;
	}

	public static void setConfigUrl(String configUrl) {
		Configure.configUrl = configUrl;
	}

	private static String notifyUrl = "http://www.egonctg.com/ec/payment/";

	private static String key = "1977carson24075043841976jidofdaf";

	//微信分配的公众号ID（开通公众号之后可以获取到）
	private static String appID = "wx14be2d51e8ad9693";

	//微信分配的公众号secret（开通公众号之后可以获取到）
	private static String appSecret = "e781d90c43531e5ee0ef53b7d20a10c8";

	//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
	private static String mchID = "1245767202";

	//受理模式下给子商户分配的子商户号
	private static String subMchID = "";

	//HTTPS证书的本地路径
	private static String certLocalPath = "WEB-INF/cert/apiclient_cert.p12";

	//HTTPS证书密码，默认密码等于商户号MCHID
	private static String certPassword = "1245767202";

	//是否使用异步线程的方式来上报API测速，默认为异步模式
	private static boolean useThreadToDoReport = true;

	//机器IP
	private static String ip = "112.74.108.46";

	//js token api:
	public static String JS_TOKEN_API = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appID+"&secret="+appSecret;
	
	//js ticket api:
	public static String JS_TICHKET_API = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?";

	//用户授权请求API:
	public static String AUTH_API = "https://open.weixin.qq.com/connect/oauth2/authorize?";
	
	//ws 用户授权
	public static String WS_AUTH_API = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx14be2d51e8ad9693&redirect_uri=http://www.egonctg.com/ec/login_wxlogincallback.do&response_type=code&scope=snsapi_userinfo&state=aaa#wechat_redirect";

	//用户授权获取access_token:
	public static String AUTH_TOKEN_API = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appID+"&secret="+appSecret+"&grant_type=authorization_code";

	//获取用户资料:
	public static String USERINFO_API = "https://api.weixin.qq.com/sns/userinfo?";

	//以下是几个API的路径：
	//1）统一下单API
	public static String UNIFIEDORDER_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	//单据查询
	public static String ORDERQUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";
	
	//1）被扫支付API
	public static String PAY_API = "https://api.mch.weixin.qq.com/pay/micropay";

	//2）被扫支付查询API
	public static String PAY_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";

	//3）退款API
	public static String REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	//4）退款查询API
	public static String REFUND_QUERY_API = "https://api.mch.weixin.qq.com/pay/refundquery";

	//5）撤销API
	public static String REVERSE_API = "https://api.mch.weixin.qq.com/secapi/pay/reverse";

	//6）下载对账单API
	public static String DOWNLOAD_BILL_API = "https://api.mch.weixin.qq.com/pay/downloadbill";

	//7) 统计上报API
	public static String REPORT_API = "https://api.mch.weixin.qq.com/payitil/report";
	
	public static String getNotifyUrl() {
		return notifyUrl;
	}

	public static void setNotifyUrl(String notifyUrl) {
		Configure.notifyUrl = notifyUrl;
	}
	public static boolean isUseThreadToDoReport() {
		return useThreadToDoReport;
	}

	public static void setUseThreadToDoReport(boolean useThreadToDoReport) {
		Configure.useThreadToDoReport = useThreadToDoReport;
	}

	public static String HttpsRequestClassName = "com.tencent.common.HttpsRequest";

	public static void setKey(String key) {
		Configure.key = key;
	}

	public static void setAppID(String appID) {
		Configure.appID = appID;
	}

	public static void setMchID(String mchID) {
		Configure.mchID = mchID;
	}

	public static void setSubMchID(String subMchID) {
		Configure.subMchID = subMchID;
	}

	public static void setCertLocalPath(String certLocalPath) {
		Configure.certLocalPath = certLocalPath;
	}

	public static String getAuthAPI(String appid,String redirect_uri,int scopeType,String state){
		String scope = "snsapi_base";
		if (scopeType!=0)
			scope = "snsapi_userinfo";
		
		return AUTH_API+"appid="+appid+"&redirect_uri="+redirect_uri+"&scope="+scope+"&state="+state+"#wechat_redirect";
	}
	
	public static String getAuthTokonAPI(String code){
		return AUTH_TOKEN_API+"&code="+code;
	}
	
	public static String getJSTokenAPI(){
		return JS_TOKEN_API;
	}
	
	public static String getJSTicketAPI(String access_token){
		return JS_TICHKET_API+"access_token="+access_token+"&type=jsapi";
	}
	
	public static String getUserInfoAPI(String access_token,String openid){
		return USERINFO_API+"access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
	}
	
	public static void setCertPassword(String certPassword) {
		Configure.certPassword = certPassword;
	}

	public static void setIp(String ip) {
		Configure.ip = ip;
	}

	public static String getKey(){
		return key;
	}
	
	public static String getAppid(){
		return appID;
	}
	
	public static String getMchid(){
		return mchID;
	}

	public static String getSubMchid(){
		return subMchID;
	}
	
	public static String getCertLocalPath(){
		return certLocalPath;
	}
	
	public static String getCertPassword(){
		return certPassword;
	}

	public static String getIP(){
		return ip;
	}

	public static void setHttpsRequestClassName(String name){
		HttpsRequestClassName = name;
	}

	public static String getSdkVersion(){
		return sdkVersion;
	}

}
