package com.tencent;

import com.tencent.business.OrderQueryBusiness;
import com.tencent.business.OrderQueryResult;
import com.tencent.business.RefundQueryBusiness;
import com.tencent.business.RefundQueryResult;
import com.tencent.business.UnifiedOrderBusiness;
import com.tencent.business.UnifiedOrderResult;
import com.tencent.common.Util;
import com.tencent.protocol.order_protocol.OrderQueryReqData;
import com.tencent.protocol.refund_query_protocol.RefundQueryReqData;
import com.tencent.protocol.unifiedorder_protocol.UnifiedOrderReqData;

public class Main {

    public static void main(String[] args) {

        try {

        	UnifiedOrderBusiness bus = new UnifiedOrderBusiness();
        	String spBillCreateIP = "112.74.108.46";
        	UnifiedOrderReqData  reqdata = new UnifiedOrderReqData("goods","123",1,spBillCreateIP);
        	reqdata.setTrade_type("NATIVE");
        	UnifiedOrderResult rst = new UnifiedOrderResult();
//        	bus.run(reqdata, rst);
        	
        	RefundQueryBusiness bus2 = new RefundQueryBusiness();
        	RefundQueryReqData req = new RefundQueryReqData("1","1","1","1","1");
        	RefundQueryResult rr = new RefundQueryResult();
//        	bus2.run(req, rr);
        	
        	
        	OrderQueryBusiness bus3 = new OrderQueryBusiness();
        	OrderQueryReqData req2 = new OrderQueryReqData();
        	OrderQueryResult rr2 = new OrderQueryResult();
        	bus3.run(req2, rr2);        	
//        	String result = "{'privilege':['a','b','daa']}";
//        	JSONObject jsonobj = JSONObject.fromObject(result);
//        	WxUserInfo info = (WxUserInfo)JSONObject.toBean(jsonobj, WxUserInfo.class);
//        	System.out.println(info.getPrivilege().length);
        } catch (Exception e){
            Util.log(e.getMessage());
        }

    }

}
