package cn.hd.base;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;


/**
 * action基类，提供共用方法
 * @author hd
 *
 */
public class BaseAction extends BaseService{
	public static int MODAL_SIGNIN = 0;
	
	public int pageSize=10;
	public int currPage=1;
	public int totalCount=0;
	public String pageStr;
	public String pageFuncId;
	public String showMessage;
	public String forwardUrl="";
	public int forwardSeconds=0;
	public String getForwardUrl() {
		return forwardUrl;
	}
	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}
	public int getForwardSeconds() {
		return forwardSeconds;
	}
	public void setForwardSeconds(int forwardSeconds) {
		this.forwardSeconds = forwardSeconds;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurrPage() {
		return currPage;
	}
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public HttpSession getHttpSession(){
	    return ServletActionContext.getRequest().getSession();
	}
	public HttpServletRequest getHttpRequest(){
		return ServletActionContext.getRequest();
	}
	public HttpServletResponse getHttpResponse(){
		return ServletActionContext.getResponse();
	}
	public PageContext getPageContext(){
		return ServletActionContext.getPageContext();
	}
	public ServletContext getServletContext(){
		return ServletActionContext.getServletContext();
	}
	public Map<String, Object> getApplication(){
		return ServletActionContext.getContext().getApplication();
	}
	public String getBasePath(){
		String path = getHttpRequest().getContextPath();
		String basePath = getHttpRequest().getScheme()+"://"+getHttpRequest().getServerName()+":"+getHttpRequest().getServerPort()+path+"/";
		return basePath;
	}
	public void write(String content){
		getHttpResponse().setCharacterEncoding("utf-8");
		getHttpResponse().setContentType("text/html;charset=utf-8");
		try {
			getHttpResponse().getWriter().print(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	  public String getIpAddress() { 
		  HttpServletRequest request = getHttpRequest();
		    String ip = request.getHeader("x-forwarded-for"); 
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		      ip = request.getHeader("Proxy-Client-IP"); 
		    } 
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		      ip = request.getHeader("WL-Proxy-Client-IP"); 
		    } 
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		      ip = request.getHeader("HTTP_CLIENT_IP"); 
		    } 
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		      ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
		    } 
		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		      ip = request.getRemoteAddr(); 
		    } 
		    return ip; 
		  }
	  
	public void writeMsg(int code){
		Message msg = new Message();
		msg.setCode(code);
		JSONObject obj = JSONObject.fromObject(msg);
		write(obj.toString());			
	}
	
	public void writeMsg2(int code,String desc){
		Message msg = new Message();
		msg.setCode(code);
		msg.setDesc(desc);
		JSONObject obj = JSONObject.fromObject(msg);
		write(obj.toString());			
	}
	/**
	 * 判断是否为站点总管理员
	 */
	public boolean isSiteAdmin(){
		if (getHttpSession().getAttribute("siteAdmin")!=null) {
			return (Boolean)getHttpSession().getAttribute("siteAdmin");
		}
		return false;
	}
	/**
	 * 返回到通用信息提示页面
	 * @param msg
	 * @param url
	 * @param seconds
	 * @return
	 */
	public String showMessage(String showMessage,String forwardUrl,int forwardSeconds){
		this.showMessage=showMessage;
		this.forwardUrl=forwardUrl;
		this.forwardSeconds=forwardSeconds;
		return "showMessage";
	}
	public String getContextPath(){
		return getHttpRequest().getContextPath()+"/";
	}
	public String getContextPathNo(){
		return getHttpRequest().getContextPath()+"/";
	}
	public String getPageStr() {
		return pageStr;
	}
	public void setPageStr(String pageStr) {
		this.pageStr = pageStr;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public String getPageFuncId() {
		return pageFuncId;
	}
	public void setPageFuncId(String pageFuncId) {
		this.pageFuncId = pageFuncId;
	}
	public String getShowMessage() {
		return showMessage;
	}
	public void setShowMessage(String showMessage) {
		this.showMessage = showMessage;
	}
}
