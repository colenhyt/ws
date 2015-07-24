<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%> 
<%
 String userinfo = (String)request.getAttribute("userinfo");
 String wxconfig = (String)request.getAttribute("wxconfig");
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
  <script src="dist/js/sea.js"></script>
  <script src="dist/js/zui.min.js"></script>
  <script src="dist/js/json2.js"></script>
  <script>
  </script>
</head>
<body>

<div id='groupDiv'>
</div>
	   <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>        
        <script type="text/javascript" src="static/js/util.js"></script>
       <script type="text/javascript" src="static/js/config.js"></script>
       <script type="text/javascript" src="static/js/htmlutil.js"></script>
       <script type="text/javascript" src="static/js/cart.js"></script>
       <script type="text/javascript" src="static/js/wxcaller.js"></script>
        <script type="text/javascript" src="static/js/region.js"></script>
        <input type='hidden' id='userinfo' value="<%=userinfo%>"/>
        <input type='hidden' id='wxconfig' value="<%=wxconfig%>"/>
<nav class="menu" data-toggle="menu" style="width: 100%">
  <ul class="nav nav-primary"  id="catDiv">
    <li>
      <a href="#" class='goods_menu'></a>
    </li>
  </ul>
</nav>	      
       <div>
       微信号: <span id="wxhao" class='wxhao'></span>
       </div>
       <div>
       付款方式*:<br>
       <table>
       <tr onclick='choosePay(0)'><td>
       <input name="paytype" id="paytype" type="radio" checked="true" value=2 class='main_radio'> 微信支付
       </td></tr>
       </table>
       </div>
       <div>收货人姓名*:<span id='contacttip' style='color:red'></span><br><input name="contact" id="contact" type="text" value=""></div>
       <div>收货人电话*:<span id='phonetip' style='color:red'></span><br><input name="phone" id="phone" type="text" value=""></div>
       <div>送货地址*:<span id='addresstip' style='color:red'></span><br>
       <div id='regionDIV'></div>
       <input name="address" id="address" type="text" value=""></div>
       <div>备注: <br><input name="remark" id="remark" type="text" value=""></div>
       <button onclick="g_cart.show()" class='button_confirm'>确认订单</button><br><br>
        <script type="text/javascript" src="static/js/main.js"></script>
       
    	</body>	
    	
</html>
