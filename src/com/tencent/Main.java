package com.tencent;

import com.tencent.business.UnifiedOrderBusiness;
import com.tencent.business.UnifiedOrderResult;
import com.tencent.common.Util;
import com.tencent.protocol.unifiedorder_protocol.UnifiedOrderReqData;

public class Main {

    public static void main(String[] args) {

        try {

        	UnifiedOrderBusiness bus = new UnifiedOrderBusiness();
        	String notifyUrl = "112.74.108.46";
        	String spBillCreateIP = "12.74.108.46";
        	String timeStart = "";
        	String timeExpire = "";
        	String goodsTag = "101";
        	UnifiedOrderReqData  reqdata = new UnifiedOrderReqData("AA","bb","123",1,notifyUrl,spBillCreateIP,timeStart,timeExpire,goodsTag);
        	reqdata.setTrade_type("NATIVE");
        	UnifiedOrderResult rst = new UnifiedOrderResult();
        	bus.run(reqdata, rst);
        } catch (Exception e){
            Util.log(e.getMessage());
        }

    }

}
