<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%> 
<%
 String errorinfo = (String)request.getAttribute("errorinfo");
%>
<html>
	<head>
		<meta charset='utf-8'>
    <link href="dist/css/zui.css" rel="stylesheet">	
  <link href="static/css/cf640.css" rel="stylesheet">
    <meta name="viewport" content="width=640, height=1236, target-densitydpi=device-dpi" />  
		<title>e美农场</title>
	</head>
  <script src="dist/js/jquery-2.0.3.min.js"></script>
  <script src="dist/js/zui.min.js"></script>
  <script src="dist/js/json2.js"></script>
  <script>
  </script>
</head>
<body>
<div id='groupDiv'>
</div>
        <script type="text/javascript" src="static/js/util.js"></script>
       <script type="text/javascript" src="static/js/htmlutil.js"></script>
       获取微信授权失败<br>
       <a href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx14be2d51e8ad9693&redirect_uri=http://www.egonctg.com/ec/login_wxlogincallback.do&response_type=code&scope=snsapi_userinfo&state=nctg#wechat_redirect">点击重新加载</a>
       
    	</body>	
    	
</html>
