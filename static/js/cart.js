Cart = function(){
    this.name = "cart";
    this.tagname = "my"+this.name;
    this.pagename = this.tagname+"page";
}

Cart.prototype.init = function(){
     var page = new PageUtil(this.tagname);
    page.buildSingleTab();
     var content =     "<div class='tab-pane in active'>";
     var pclass = "cfpage ";
    content += "<div class='"+pclass+"' id='"+this.pagename+"'>"
    content += " <input type=\"button\" value=\"购买\" onclick=\"g_cart.doBuy()\"><br>"
    content += ""
    content += ""
    content += "</div></div>"
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
 
 $('#'+this.tagname).modal({position:100,show: true}); 
}

Cart.prototype.doBuy = function(){
 var form = document.getElementById('groupForm');
 form.submit();
}

var g_cart = new Cart();
 g_cart.init();
