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
    <meta name="viewport" content="width=640, height=1236, target-densitydpi=device-dpi" />  
		<title>e美农场</title>
	</head>
  <script src="dist/js/jquery-2.0.3.min.js"></script>
  <script src="dist/js/sea.js"></script>
  <script src="dist/js/zui.min.js"></script>
  <script src="dist/js/json2.js"></script>
  <script>
  </script>
  <style type="text/css">
/* CSS Document */
img{ max-width:100%;} 

body {
margin-left:30px;
  font-size:33px;
  width:580px;
  background-color:#FAFAD2;
}

table {
background-color:#ffffff;
width:100%;
}

td {
border:none;
padding:5px;
padding-left:20px;
height:200%;
}


input {
width: 100%;
}

.line {
width:100%;
height:1px;
margin:0px auto;
padding-top:5px;
padding-bottom:5px;
padding:0px;
background-color:#D5D5D5;
overflow:hidden;
}


div {
margin-top:20px;
margin-bottom:20px;
}

.button_confirm {
background-color:#EECFA1;
width:100%;
}

.button_confirm.order {
margin:10px;
width:95%;
}

.main_header{
font-size:120%;
width:100%;
}

.main_radio {
width:5%;
}

.main_region {
width:260px;
}

.goods_menu
{
  background-color:#EECFA1;

}

.goods_table {
width:100%;
border:none;
}

.goods_item_name{
font-size:85%;
}

.goods_item_desc {
font-size:75%;
width:100%;
}

.goods_countbutton {
width:80px;
height:58px;
margin-top:0px;
border:none;
-moz-background-size:100% 100%;  
    background-size:100% 100%;  
}

.goods_countvalue {
width:70%;
text-align:center;
}

.goods_item_img_td {
width:40%;
}

.goods_item_img{
width:250px;
height:250px;
}

.goods_item_ps {
color:red;
width:30%;
}

.goods_item_number {
text-align:right;
font-size:85%;
}


tr.orderlist_title{
background-color:#F2F2F2;
}

td.orderlist_td{
border:1px solid #D9D9D9;
text-align:center;
}

td.orderlist_td{
border:1px solid #D9D9D9;
text-align:center;
}

.modal-dialog.orderlist_page{
width:600px;
}

.orderlist_items {
margin:10px;
font-size:70%;
width:95%;
}

table.orderlist_address{
width:95%;
margin:10px;
font-size:60%;
text-align:left;
}


  </style>

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
