
var MSG_OK = 0;
var MSG_SQLExecuteError = 1;
var MSG_PrepayReqFail = 2;
var MSG_UserInfoMissing = 3;
var MSG_OrderAmountInvalid = 4;
var MSG_GoodsNotFound = 5;
var MSG_NoAnyGoods = 6;
var MSG_UserNotFound = 7;

var ERR_MSG ={};
ERR_MSG[MSG_SQLExecuteError] = "sql出错";
ERR_MSG[MSG_PrepayReqFail] = "微信支付申请失败";
ERR_MSG[MSG_UserInfoMissing] = "用户信息缺失";
ERR_MSG[MSG_OrderAmountInvalid] = "订单金额为零";
ERR_MSG[MSG_GoodsNotFound] = "商品找不到";
ERR_MSG[MSG_NoAnyGoods] = "没有购买任何商品";
ERR_MSG[MSG_UserNotFound] = "没找到对应用户";

//sc();
//aa();
