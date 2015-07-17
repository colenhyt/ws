<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%> 
<%
 String userinfo = (String)request.getAttribute("userinfo");
%>
<html>
	<head>
		<meta charset='utf-8'>
    <link href="dist/css/zui.css" rel="stylesheet">	
  <link href="static/css/cf640.css" rel="stylesheet">
    <meta name="viewport" content="width=640, height=1236, target-densitydpi=device-dpi" />  
		<title>ws</title>
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
       <script type="text/javascript" src="static/js/cart.js"></script>
        <script type="text/javascript" src="static/js/region.js"></script>
        <input type='hidden' id='userinfo' value="<%=userinfo%>"/>
<nav class="menu" data-toggle="menu" style="width: 100%">
  <ul class="nav nav-primary"  id="catDiv">
    <li>
      <a href="#" class='menu2'>一，手工定制产品(肉粽，咸鸭蛋)</a>
      <ul class="nav">
        <table><tr><td>fdafad</td></tr><tr><td>fdafad</td></tr></table>
        <table>
        <tr><td><img src="static/img/bt_minus.png">茅山网</td></tr>
        <tr><td>desc</td></tr>
        <tr><td>ps</td></tr>
        <tr><td><li><img class='wsgoods_count'><input type="text" id="goodsCount" value="0" class='wsgoods_value'><img class='wsgoods_count add'></td></tr>
        </table>
        ...
      </ul>
    </li>
  </ul>
</nav>	      
       <div>
       微信号*<br>
       <input name="wxhao" id="wxhao" type="text" class='wxhao'>
       </div>
       <div>
       付款方式*:<br>
       <table>
       <tr onclick='choosePay(0)'><td>
       <input name="paytype" id="paytype" type="radio" checked="true" value=2 class='rr'> 微信支付
       </td></tr>
       </table>
       </div>
       <div>收货人姓名*:<br><input name="contact" id="contact" type="text" value=""></div>
       <div>收货人电话*:<br><input name="phone" id="phone" type="text" value=""></div>
       <div>送货地址*:<br>
       <div id='regionDIV'></div>
       <input name="address" id="address" type="text" value=""></div>
       <div>备注: <br><input name="remark" id="remark" type="text" value=""></div>
       <button onclick="g_cart.show()" class='button2'>确定</button><br><br>
        <script type="text/javascript" src="static/js/main.js"></script>
       
    	</body>	
    	
</html>
