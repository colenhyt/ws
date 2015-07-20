package com.tencent.service;

import com.tencent.common.Configure;
import com.tencent.protocol.order_protocol.OrderQueryReqData;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 16:03
 */
public class OrderQueryService extends BaseService{

    public OrderQueryService() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.ORDERQUERY_API);
    }

    /**
     * 请求支付服务
     * @param scanPayReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的数据
     * @throws Exception
     */
    public String request(OrderQueryReqData unifiedOrderReqData) throws Exception {

        //--------------------------------------------------------------------
        //发送HTTPS的Post请求到API地址
        //--------------------------------------------------------------------
        String responseString = sendPost(unifiedOrderReqData);

        return responseString;
    }
}
