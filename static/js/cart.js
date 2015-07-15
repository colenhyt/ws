Cart = function(){
    this.name = "cart";
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.data = {}
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

Cart.prototype.show = function(){

// var tag = document.getElementById('wxhao');
// if (tag.value.length<=0){
//  alert('请填上你的微信号');
//  return;
// }
//   
//tag = document.getElementById('contact');
// if (tag.value.length<=0){
//  alert('请填上收货人姓名');
//  return;
// }
//  
//tag = document.getElementById('phone');
// if (tag.value.length<=0){
//  alert('请填上收货人电话');
//  return;
// }
// 
//tag = document.getElementById('address');
// if (tag.value.length<=0){
//  alert('请填上你的送货地址');
//  return;
// }

var itemlength  = Object.keys(this.data).length ;

 if (itemlength<=0){
  alert('未挑选任何商品');
  return;
 }
 
 var tag = document.getElementById(this.pagename);
 var content = "";
 var totalps = 0;
 content += "<table class='goods'>";
  content += "<tr>"
  content += "<td>商品</td>"
  content += "<td>数量</td>"
  content += "<td>单价</td>"
     content += "</tr>"
 for (var key in this.data){
  var item = this.data[key];
  content += "<tr>"
 content += "<td>"+item.goodsName+"</td>";
 content += "<td style='text-align:center'>"+item.count+"</td>";
 content += "<td style='text-align:center'>￥&nbsp"+item.shopPrice+"</td>";
 totalps += item.count*item.shopPrice
     content += "</tr>"
 }
  content += "<tr><td colspan=2 style='text-align:right'>"
 content += "总金额:</td>"
 content += "<td style='text-align:center'>￥&nbsp"+totalps;
     content += "</td></tr>"
 	content += "</table>";
 	content += "<br>"
    content += " <button onclick=\"g_cart.doBuy()\" class='button2'>确认订单</button>"
	
 tag.innerHTML = content;
 
 $('#'+this.tagname).modal({position:100,show: true}); 
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
	if (currCount>=0){
    tag.value = currCount;
    this.data[goodsId] = {goodsId:goodsId,count:currCount,goodsName:goodsName,shopPrice:shopPrice};
    }
}

Cart.prototype.doBuy = function(){
 
 	var dataParam = "goods=[";
 	for (var key in this.data){
 	 var item = {goodsId:parseInt(key),goodsNumber:parseInt(this.data[key].count)};
 	 var str = json2str(item);
 	 dataParam += str+","
 	}
 	dataParam += "]";
 	var tag = document.getElementById('wxhao');
  	dataParam += "&wxhao="+tag.value;
  	tag = document.getElementById('contact');
  	dataParam += "&contact="+tag.value;
  	tag = document.getElementById('phone');
  	dataParam += "&phone="+tag.value;
  	tag = document.getElementById('address');
  	dataParam += "&address="+tag.value;
  	tag = document.getElementById('remark');
  	dataParam += "&remark="+tag.value;
   	tag = document.getElementById('paytype');
  	dataParam += "&paytype="+tag.value; 	
  	
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
  content = '购买成功，订单号:'+ret.desc;
 }else
  content = "购买失败，错误码:"+ret.code;
 var tag = document.getElementById(this.pagename);
 tag.innerHTML = content;  
 
 // $('#'+g_cart.tagname).modal('hide');  
}


var g_cart = new Cart();
 g_cart.init();
 // g_cart.show();
