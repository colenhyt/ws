package cn.hd.ws.action;

public interface RetMsg {
	public static int MSG_OK = 0;
	public static int MSG_SQLExecuteError = 1;
	public static int MSG_PrepayReqFail = 2;
	public static int MSG_UserInfoMissing = 3;
	public static int MSG_OrderAmountInvalid = 4;
	public static int MSG_GoodsNotFound = 5;
	public static int MSG_NoAnyGoods = 6;
	public static int MSG_UserNotFound = 7;
	public static int MSG_AddressInvalid = 8;
	public static int MSG_OrderSaveFail = 9;
	public static int MSG_OrderGoodsSaveFail = 10;
}
