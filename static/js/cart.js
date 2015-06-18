Cart = function(){
    this.name = "cart";
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
    this.data = {}
}

Cart.prototype.init = function(){
     var page = new PageUtil(this.tagname);
    page.buildSingleTab();
     var content =     "<div class='tab-pane in active'>";
    content += "<div id='"+this.pagename+"'>"
    content += "</div>"
    content += " <input type=\"button\" value=\"确认\" onclick=\"g_cart.doBuy()\">"
    content += " <button type='button' data-dismiss='modal'>返回</button><br>"
    content += "</div>"
    page.addContent(content);
    document.write(page.toString()); 
}

Cart.prototype.show = function(){
 var tag = document.getElementById('wxhao');
 if (tag.value.length<=0){
  alert('请填上你的微信号');
  return;
 }
 
tag = document.getElementById('address');
 if (tag.value.length<=0){
  alert('请填上你的送货地址');
  return;
 }
 
 var tag = document.getElementById(this.pagename);
 var content = "";
 var totalps = 0;
 for (var key in this.data){
  var item = this.data[key];
 content += item.goodsName+",数量:"+item.count+",ps:"+item.shopPrice+"<br>";
 totalps += item.count*item.shopPrice
 }
 content += "总价:"+totalps;
 tag.innerHTML = content;
 
 $('#'+this.tagname).modal({position:100,show: true}); 
}

Cart.prototype.count = function(goodsId,goodsName,shopPrice,count){
var tag = document.getElementById("goodsCount"+goodsId);
var currCount = parseInt(tag.value);
	currCount += count;
	if (currCount>=0){
    tag.value = currCount;
    this.data[goodsId] = {count:currCount,goodsName:goodsName,shopPrice:shopPrice};
    }
}

Cart.prototype.doBuy = function(){
// var form = document.getElementById('groupForm');
// form.submit();
 
 	var dataParam = "goods=[";
 	for (var key in this.data){
 	 var item = {goodsId:parseInt(key),goodsNumber:parseInt(this.data[key].count)};
 	 var str = json2str(item);
 	 dataParam += str+","
 	}
 	dataParam += "]";
 	var tag = document.getElementById('wxhao');
  	dataParam += "&wxhao="+tag.value;
  	tag = document.getElementById('address');
  	dataParam += "&address="+tag.value;
   	tag = document.getElementById('paytype');
  	dataParam += "&paytype="+tag.value; 	
  	
	//alert(dataParam);
	try    {
		$.ajax({type:"post",url:"/ws/order_order.do",data:dataParam,success:function(data){
		}});
	}   catch  (e)   {
	    alert(e.name);
	}
}

var g_cart = new Cart();
 g_cart.init();
