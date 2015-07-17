package com.tencent.business;

import com.tencent.business.RefundQueryBusiness.ResultListener;
import com.tencent.protocol.refund_query_protocol.RefundQueryResData;

public class RefundQueryResult implements ResultListener {

	@Override
	public void onFailByReturnCodeError(RefundQueryResData refundQueryResData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailByReturnCodeFail(RefundQueryResData refundQueryResData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailBySignInvalid(RefundQueryResData refundQueryResData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefundQueryFail(RefundQueryResData refundQueryResData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefundQuerySuccess(RefundQueryResData refundQueryResData) {
		// TODO Auto-generated method stub

	}

}
