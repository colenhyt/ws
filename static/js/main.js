function wxpayrequest(){

}

function renderGroupCats(catItems){
var tag = document.getElementById("catDiv");
var content = "";
for (var i=0;i<catItems.length;i++){
var cat = catItems[i];
content += "<li class=\"nav-parent\">"
content += "<a href='#' onclick='queryGroupGoods("+cat.catId+")'>"+cat.catName;
content += "<i class=\"icon-chevron-right nav-parent-fold-icon\"></i>"
content += "</a>";
content += "<ul class=\"nav\" id='cat"+cat.catId+"'>";
content += "</ul>";
content += "</li>";
content += "<br>";
}
tag.innerHTML = content;
}

function queryGroupCats(){
	//alert(dataParam);
	var dataobj = $.ajax({type:"post",url:"/ws/goods_cat.do",async:false});
	 var obj = cfeval(dataobj.responseText);
	 
	 renderGroupCats(obj);
}

function queryGroupGoods(catId){
	var item = {catId:catId};
	var dataParam = obj2ParamStr("ecsGoods",item);
	//alert(dataParam);
	var dataobj = $.ajax({type:"post",url:"/ws/goods_goods.do",data:dataParam,async:false});
    var tag = document.getElementById("cat"+catId);
    var items = cfeval(dataobj.responseText);
    var content = "";
    for (var i=0;i<items.length;i++){
    var item = items[i];
	content += "<table>";
    content += "    <tr><td><img src='"+item.goodsImg+"'>"+item.goodsName+"</td></tr>"
    content += "    <tr><td>"+item.goodsDesc+"</td></tr>"
    content += "    <tr><td>ps:"+item.shopPrice+"</td></tr>"
    content += "    <tr><td><li><img class='wsgoods_count' onclick=\"countGoods("+item.goodsId+",-1)\"><input type=\"text\" id=\"goodsCount"+item.goodsId+"\" value=\"0\" class='wsgoods_value'><img class='wsgoods_count add' onclick=\"countGoods("+item.goodsId+",1)\"></td></tr>"
	content += "</table>";
    }
    tag.innerHTML = content;
    
}

function countGoods(goodsId,count){
var tag = document.getElementById("goodsCount"+goodsId);
var currCount = parseInt(tag.value);
	currCount += count;
	if (currCount>=0)
    tag.value = currCount;
}

function addOrder(){
	var item = {appid:"appidtestaaa",prepayid:392,goods:"goodsaaa",sign:"sign333",mchid:"mchid",noncestr:"nonceStr"};
	var dataParam = obj2ParamStr("wxorder",item);
	//alert(dataParam);
	try    {
		$.ajax({type:"post",url:"/ws/order_add.do",data:dataParam,success:function(data){
		}});
	}   catch  (e)   {
	    alert(e.name);
	}
}

queryGroupCats();
//queryGroupGoods(3);
//addOrder();