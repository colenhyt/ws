package com.tencent;

import com.tencent.business.UnifiedOrderBusiness;
import com.tencent.business.UnifiedOrderResult;
import com.tencent.common.Util;
import com.tencent.protocol.unifiedorder_protocol.UnifiedOrderReqData;

public class Main {

    public static void main(String[] args) {

        try {

        	UnifiedOrderBusiness bus = new UnifiedOrderBusiness();
        	String notifyUrl = "http://egonctg.com/ws/payment/";
        	String spBillCreateIP = "12.74.108.46";
        	UnifiedOrderReqData  reqdata = new UnifiedOrderReqData("bb","123",1,notifyUrl,spBillCreateIP);
        	reqdata.setTrade_type("NATIVE");
        	UnifiedOrderResult rst = new UnifiedOrderResult();
        	bus.run(reqdata, rst);
        } catch (Exception e){
            Util.log(e.getMessage());
        }

    }

}
