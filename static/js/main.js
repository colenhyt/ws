var catGoods = {};

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
}
tag.innerHTML = content;
}

function findGoodsNo(goodsId)
{
	var goodsno = 0;
	for (var catid in catGoods)
	{
		var items = catGoods[catid];
		for (var i=0;i<items.length;i++){
		 if (items[i].goodsId==goodsId){
		  goodsno = items[i].goodsNumber;
		  break;
		 }
		}
	}
	return goodsno;
}

function queryGroupCats(){
	//alert(dataParam);
	var dataobj = $.ajax({type:"post",url:"/ec/goods_cat.do",async:false});
	 var obj = cfeval(dataobj.responseText);
	 
	 renderGroupCats(obj);
}

function queryGroupGoods(catId){
 
 	var items = catGoods[catId];
 	if (items==null){
		var item = {catId:catId};
		var dataParam = obj2ParamStr("ecsGoods",item);
		//alert(dataParam);
		var dataobj = $.ajax({type:"post",url:"/ec/goods_goods.do",data:dataParam,async:false});
	    items = cfeval(dataobj.responseText);
	    catGoods[catId] = items;
 	}else {
 	
 	}
 	
    var tag = document.getElementById("cat"+catId);
    var content = "";
    for (var i=0;i<items.length;i++){
    var item = items[i];
    var imgSrc = "";
    if (item.originalImg.length>0)
     imgSrc = item.originalImg;
    else if (item.goodsImg.length>0)
     imgSrc = item.goodsImg;
    else if (item.goodsThumb.length>0)
     imgSrc = item.goodsThumb;
    else
     imgSrc = "/ec/images/upload/Image/"+item.goodsSn+".jpg";
    var goodsno = item.goodsNumber;
	content += "<table class='goods'>";
    content += "    <tr><td class='goods img'><img src='"+imgSrc+"' class='goodsImg'></td><td class='goods title'>"+item.goodsName+"</td></tr>"
    content += "    <tr><td class='goods desc' colspan=2>"+item.goodsDesc+"</td></tr>"
    content += "    <tr><td class='goods ps'> ￥"+item.shopPrice+"</td>"
    content += "<td class='goods no'>"
    if (goodsno>0){
     content += "剩余: "+goodsno
    }
    content += "</td></tr>"
    content += "    <tr><td class='goods' colspan=2>"
    if (goodsno>0){
     var func = "g_cart.count("+item.goodsId+",'"+item.goodsName+"',"+item.shopPrice;   
     content += "<li><img class='wsgoods_count' onclick=\""+func+",-1)\"><input type=\"text\" id=\"goodsCount"+item.goodsId+"\" value=\"0\" class='wsgoods_value'><img class='wsgoods_count add' onclick=\""+func+",1)\">"
    }
    else
     content += "已售空"
    content += "</td></tr>"
	content += "</table>";
	content += "<div class=\"line\"></div>"
    }
 	content += "<br>"
 	
    tag.innerHTML = content;
    
}

function addOrder(){
	var item = {appid:"appidtestaaa",prepayid:392,goods:"goodsaaa",sign:"sign333",mchid:"mchid",noncestr:"nonceStr"};
	var dataParam = obj2ParamStr("wxorder",item);
	//alert(dataParam);
	try    {
		$.ajax({type:"post",url:"/ec/order_add.do",data:dataParam,success:function(data){
		}});
	}   catch  (e)   {
	    alert(e.name);
	}
}

function queryGroup()
{
	var dataobj = $.ajax({type:"post",url:"/ec/goods_group.do",async:false});
	var group = cfeval(dataobj.responseText);
	var content = "";
	content += "<div class='header'>"
	content += group.actName;
	content += "</div>"
	content += "<div class='groupDesc'>"
	var desc = group.actDesc;
	content += desc;
	content += "</div>"
	
	var tag = document.getElementById("groupDiv");
	tag.innerHTML = content;
	
}

function choosePay(index){
	var tags = document.getElementsByName("paytype");
	if (index==0){
	 tags[0].checked = true;
	 tags[1].checked = false;
	}else {
	 tags[0].checked = false;
	 tags[1].checked = true;
	}
}

queryGroup();
queryGroupCats();
//queryGroupGoods(3);
//addOrder();