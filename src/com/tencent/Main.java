package com.tencent;

import net.sf.json.JSONObject;
import cn.hd.wx.WxUserInfo;

import com.alibaba.fastjson.JSON;
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
        	String spBillCreateIP = "120.196.99.5";
        	UnifiedOrderReqData  reqdata = new UnifiedOrderReqData("Ipad","111",200,spBillCreateIP);
        	UnifiedOrderResult rst = new UnifiedOrderResult();
        	bus.run(reqdata, rst);
        	
        	RefundQueryBusiness bus2 = new RefundQueryBusiness();
        	RefundQueryReqData req = new RefundQueryReqData("1","1","1","1","1");
        	RefundQueryResult rr = new RefundQueryResult();
//        	bus2.run(req, rr);
        	
        	OrderQueryBusiness bus3 = new OrderQueryBusiness();
        	OrderQueryReqData req2 = new OrderQueryReqData();
        	OrderQueryResult rr2 = new OrderQueryResult();
//        	bus3.run(req2, rr2);        	
        	
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
