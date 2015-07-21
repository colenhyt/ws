package com.tencent;

import com.tencent.business.OrderQueryBusiness;
import com.tencent.business.OrderQueryResult;
import com.tencent.business.RefundQueryBusiness;
import com.tencent.business.RefundQueryResult;
import com.tencent.business.UnifiedOrderBusiness;
import com.tencent.common.Configure;
import com.tencent.common.Util;
import com.tencent.protocol.order_protocol.JSChooseWXPayReqData;
import com.tencent.protocol.order_protocol.OrderQueryReqData;
import com.tencent.protocol.refund_query_protocol.RefundQueryReqData;
import com.tencent.protocol.unifiedorder_protocol.UnifiedOrderReqData;
import com.tencent.protocol.unifiedorder_protocol.UnifiedOrderResData;

public class Main {

    public static void main(String[] args) {

        try {

        	UnifiedOrderBusiness bus = new UnifiedOrderBusiness();
        	String spBillCreateIP = "120.196.99.5";
        	String tradeNo = "111";
        	Configure.setTradeType("NATIVE");
        	UnifiedOrderReqData  reqdata = new UnifiedOrderReqData(null,"Ipad",tradeNo,1,spBillCreateIP);
        	UnifiedOrderResData rst = bus.run(reqdata);
//        	if (rst.isSuccess()){
//        		int a = 100;
//        	}
        	
        	RefundQueryBusiness bus2 = new RefundQueryBusiness();
        	RefundQueryReqData req = new RefundQueryReqData("1","1","1","1","1");
        	RefundQueryResult rr = new RefundQueryResult();
//        	bus2.run(req, rr);
        	
        	OrderQueryBusiness bus3 = new OrderQueryBusiness();
        	OrderQueryReqData req2 = new OrderQueryReqData("111");
        	OrderQueryResult rr2 = new OrderQueryResult();
//        	bus3.run(req2, rr2);        	
        	
        	JSChooseWXPayReqData rerr = new JSChooseWXPayReqData("123");
        	System.out.println(rerr.getSign());
//        	String result = "{'privilege':['a','b','daa']}";
//        	JSONObject jsonobj = JSONObject.fromObject(result);
//        	WxUserInfo info = (WxUserInfo)JSONObject.toBean(jsonobj, WxUserInfo.class);
//        	info.setAddress("fdfda");
//        	String jsonstr = JSON.toJSONString(info);
//        	System.out.println(jsonstr);
        } catch (Exception e){
            Util.log(e.getMessage());
        }

    }

}
