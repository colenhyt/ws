package com.tencent;

import net.sf.json.JSONObject;
import cn.hd.wx.WxUserInfo;

import com.tencent.business.UnifiedOrderBusiness;
import com.tencent.business.UnifiedOrderResult;
import com.tencent.common.Util;
import com.tencent.protocol.unifiedorder_protocol.UnifiedOrderReqData;

public class Main {

    public static void main(String[] args) {

        try {

//        	UnifiedOrderBusiness bus = new UnifiedOrderBusiness();
//        	String notifyUrl = "http://egonctg.com/ws/payment/";
//        	String spBillCreateIP = "112.74.108.46";
//        	UnifiedOrderReqData  reqdata = new UnifiedOrderReqData("bb","123",1,spBillCreateIP);
//        	reqdata.setTrade_type("NATIVE");
//        	UnifiedOrderResult rst = new UnifiedOrderResult();
        	//bus.run(reqdata, rst);
        	String result = "{'privilege':['a','b','daa']}";
        	JSONObject jsonobj = JSONObject.fromObject(result);
        	WxUserInfo info = (WxUserInfo)JSONObject.toBean(jsonobj, WxUserInfo.class);
        	System.out.println(info.getPrivilege().length);
        } catch (Exception e){
            Util.log(e.getMessage());
        }

    }

}
