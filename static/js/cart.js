Cart = function(){
    this.name = "cart";
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.currOrder = null;
    this.data = [];
}

Cart.prototype.init = function(){
     var page = new PageUtil(this.tagname);
   // page.buildSingleTab();
     var content =     "";
    content += "<div id='"+this.pagename+"'>"
    content += "</div>"
    page.addContent(content);
    document.write(page.toString()); 
}

Cart.prototype.check = function(){
 var ret = true;
var tag = document.getElementById('contact');
  var tag22 = document.getElementById('contacttip');
 if (tag.value.length<=0){
  tag22.innerHTML = '(请填上收货人姓名)'; 
  ret = false;
 }else {
  tag22.innerHTML = ''; 
 }
  
 var tag = document.getElementById('phone');
  tag22 = document.getElementById('phonetip');
 if (tag.value.length!=11||!isNumber(tag.value)){
  tag22.innerHTML = '(请填上有效联系电话)';
  ret = false;
 }else {
  tag22.innerHTML = ''; 
 }
 
tag = document.getElementById('address');
  tag22 = document.getElementById('addresstip');
  
  var tagp = document.getElementById('region_province')
  var tagcity = document.getElementById('region_city')
 if (tag.value.length<=0||tagp.value<0||tagcity.value<0){
  tag22.innerHTML = '(请填上完整送货地址)'; 
  ret = false;
 }else {
  tag22.innerHTML = ''; 
 }
var itemlength  = this.data.length ;
 if (itemlength<=0){
  alert('未挑选任何商品');
  ret = false;
 } 
 
  return ret;
}

Cart.prototype.freight = function(){
  	var citytag = document.getElementById('region_city');
  	var cityvalue = citytag.value;
	var dataobj = $.ajax({type:"post",url:"/ec/order_freight.do",data:"city="+cityvalue,async:false});
	 var obj = cfeval(dataobj.responseText);
	 var freight = 0;
	if (obj.code==0){
	 if (isNumber(obj.desc))
	  freight = parseInt(obj.desc);
	}
	return freight;
}

Cart.prototype.address = function(){
  	var contact = document.getElementById('region_province');
  	var reg_id = contact.value;
  	var pro = ""
	 for (var i=0;i<data_region.length;i++){
	  if (data_region[i][0]==reg_id){
	   pro = data_region[i][1];
	   break;
	  }
	 }
 
  	contact = document.getElementById('region_city');
  	var city = "";
 	for (var key in data_subregion){
 	 var cities = data_subregion[key];
 	 for (var i=0;i<cities.length;i++){
 	  if (cities[i][0]==contact.value){
 	   city = cities[i][1];
 	   break;
 	  }
 	 }
 	}
 	
  	contact = document.getElementById('address');
	 return pro+" "+city+" "+contact.value;
}

Cart.prototype.show = function(){

var check = this.check();
if (!check) return;

 var freight = this.freight();

 var content = "";
 var totalps = 0;
 content += "<table class='orderlist_items'>";
  content += "<tr class='orderlist_title'>"
  content += "<td class='orderlist_td'>商品</td>"
  content += "<td class='orderlist_td'>数量</td>"
  content += "<td class='orderlist_td'>单价</td>"
  content += "<td class='orderlist_td'>小计(元)</td>"
     content += "</tr>"
 for (var key in this.data){
  var item = this.data[key];
  if (item==null||item.count<=0) continue;
  content += "<tr>"
 content += "<td class='orderlist_td'>"+item.goodsName+"</td>";
 content += "<td class='orderlist_td'>"+item.goodsNumber+"</td>";
 content += "<td class='orderlist_td'>￥&nbsp"+ForDight(item.shopPrice)+".00</td>";
 content += "<td class='orderlist_td'>￥&nbsp"+ForDight((item.shopPrice*item.goodsNumber))+".00</td>";
 totalps += item.goodsNumber*item.shopPrice
     content += "</tr>"
 }
 totalps += freight;
 
  if (freight>0){
  content += "<tr><td class='orderlist_td' colspan=3 style='text-align:right'>"
 content += "该地区运费:</td>"
  content += "<td class='orderlist_td'>￥&nbsp"+ForDight(freight);
     content += ".00</td></tr>"
  
  }
  content += "<tr><td class='orderlist_td' colspan=3 style='text-align:right'>"
 content += "订单金额总计:</td>"
 content += "<td class='orderlist_td'>￥&nbsp<span style='color:red;font-size:130%' id='goods_totalfee'>"+ForDight(totalps)+".00</span>";
     content += "</td></tr>"
 	content += "</table>";
 	
  	var contact = document.getElementById('region_provice');
  	var address = this.address();
 	content += "<table class='orderlist_address'><tr><td style='width:20%'>送货地址: </td><td>"+address+"</td></tr>"
  	contact = document.getElementById('contact');
 	content += "<tr><td style='width:20%'>收货人: </td><td>"+contact.value+"</td></tr>"
  	contact = document.getElementById('phone');
 	content += "<tr><td style='width:20%'>联系电话: </td><td>"+contact.value+"</td></tr>"
 	content += "</table>"
 	
    content += " <button onclick=\"g_cart.doBuy()\" class='button_confirm order'>提交订单</button>"
	
	
  var tag = document.getElementById(this.pagename);
 tag.innerHTML = content;
 
 $('#'+this.tagname).modal({position:100,show: true}); 
}

Cart.prototype.close = function(){
 $('#'+this.tagname).modal('hide');  
}

Cart.prototype.count = function(goodsId,goodsName,shopPrice,count){
var tag = document.getElementById("goodsCount"+goodsId);
var currCount = parseInt(tag.value);
	currCount += count;
	var currNo = findGoodsNo(goodsId);
	if (currCount>currNo){
	 alert('购买数量不能大于剩余数');
	 return;
	}
	if (currCount<0) return;
	
     tag.value = currCount;
     var isNew = true;
     
     for (var i=0;i<this.data.length;i++){
      var item = this.data[i];
      if (item.goodsId==goodsId){
       if (currCount==0){
        this.data.splice(i,1);
       }else {
        this.data[i].goodsNumber = currCount;
       }
       isNew = false;
        break;
      }
     }
     if (isNew&&currCount>0){
      this.data.push({goodsId:goodsId,goodsNumber:currCount,goodsName:goodsName,shopPrice:shopPrice});
     }
}

Cart.prototype.doBuy = function(){
  	var citytag = document.getElementById('region_city');
  	var cityvalue = citytag.value;
	var dataobj = $.ajax({type:"post",url:"/ec/order_freight.do",data:"city="+cityvalue,async:false});
	 var obj = cfeval(dataobj.responseText);
	 var freight = 0;
	if (obj.code==0){
	 freight = obj.desc;
	}
 	var dataParam = "";
 	
 	//goods items:
 	dataParam += "goods=[";
 	for (var i=0;i<this.data.length;i++){
 	 var item = this.data[i];
 	 var str = json2str(item);
 	 dataParam += str+","
 	}
 	dataParam += "]";
 	var tag = document.getElementById('userinfo');
 	if (tag.value.length>0)
  	 dataParam += "&userinfo="+tag.value;
  	tag = document.getElementById('contact');
  	dataParam += "&contact="+tag.value;
  	tag = document.getElementById('phone');
  	dataParam += "&phone="+tag.value;
  	tag = document.getElementById('region_province');
  	dataParam += "&province="+tag.value;
  	tag = document.getElementById('region_city');
  	dataParam += "&city="+tag.value;
  	tag = document.getElementById('address');
  	dataParam += "&address="+tag.value;
  	tag = document.getElementById('remark');
  	dataParam += "&remark="+tag.value;
   	tag = document.getElementById('paytype');
  	dataParam += "&paytype="+tag.value; 	
   	tag = document.getElementById('goods_totalfee');
  	dataParam += "&totalfee="+tag.innerText; 	
  	
  	
  	var content ="<div class='orderlist_wait_msg' id='orderlist_msg'><br>正在提交您的订单，请稍等....</div>"
  	var cc = "<div class='orderlist_wait_img' id='orderlist_wait'><img src='static/img/w1.gif'></div>"
  	content += cc;
  	
 var tag = document.getElementById(this.pagename);
 tag.innerHTML = content;  
   	
	//alert(dataParam);
	try    {
		$.ajax({type:"post",url:"/ec/order_order.do",data:dataParam,success:function(data){
		 g_cart.buyCallback(data);
		}});
	}   catch  (e)   {
	    alert(e.name);
	}
}

Cart.prototype.buyCallback = function(data){
 var ret = cfeval(data);
 var content;
 if (ret.code==0){
 var rets = cfeval(ret.desc);
  var info = rets[0];
  var req = rets[1];
  content = "订单号生成:<span style='color:red'>"+info.orderSn+"</span><br>向微信发起支付....";
  this.currOrder = info;
  var g_wx = new WxCaller();
  g_wx.reqWxpay(req);
 }else {
  content = "<br><img src='static/img/error.png' class='main_icon'>购买失败:"
  content += ERR_MSG[ret.code];
  if (ret.desc.length>0){
   content += ","+ret.desc;
  }
    content += "<br><br><br><button onclick=\"g_cart.close()\" class='button_confirm quit'>退出</button>"

  var my = document.getElementById("orderlist_wait");
    if (my != null)
        my.parentNode.removeChild(my);
 }
 
        
 var tag = document.getElementById('orderlist_msg');
 tag.innerHTML = content;  
 
}

Cart.prototype.wxpayCallback = function(payOk){
 if (this.currOrder==null)
  return;
  
  var dataParam = "";  
  dataParam += "orderSn="+this.currOrder.orderSn;
  dataParam += "&payOk="+payOk;
   
 	if (g_user.userId>0)
  	 dataParam += "&userId="+g_user.userId;
  	   
	try    {
		$.ajax({type:"post",url:"/ec/order_commit.do",data:dataParam,success:function(data){
		 var msg = cfeval(data);
		 g_cart.commitCallback(msg);
		}});
	}   catch  (e)   {
	    alert(e.name);
	}
	  
 
  this.currOrder = null; 
}

Cart.prototype.commitCallback = function(ret){
 var content;
 if (ret.code==0){
	 var desc = cfeval(ret.desc);
	  content = "<br><img src='static/img/error.png' class='main_icon'>订单<span style='color:red'>"+desc.orderSn+"</span><br>支付失败或被取消!!"
	  if (desc.payOk==true){
	    content = "<br><img src='static/img/ok.png' class='main_icon'>订单"+desc.orderSn+"提交成功!!"
	  }
  }else {
   content = "<br><img src='static/img/error.png' class='main_icon'>订单提交失败或被取消:"+ERR_MSG[ret.code];
  }
   
   content += "<br><br><br><button onclick=\"g_cart.close()\" class='button_confirm quit'>关闭</button>"
  
  var my = document.getElementById("orderlist_wait");
    if (my != null)
        my.parentNode.removeChild(my);
          
  var tag = document.getElementById('orderlist_msg');
  tag.innerHTML = content;  
  
}

var g_cart = new Cart();
 g_cart.init();
 // g_cart.show();
