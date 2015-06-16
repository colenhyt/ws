function wxpayrequest(){

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

addOrder();