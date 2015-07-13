package com.tencent.business;

import com.tencent.protocol.unifiedorder_protocol.UnifiedOrderResData;

public class UnifiedOrderResult implements UnifiedOrderBusiness.ResultListener{

	@Override
	public void onFailByReturnCodeError(UnifiedOrderResData unifiedOrderResData) {
		// TODO Auto-generated method stub
		int a = 10;
	}

	@Override
	public void onFailByReturnCodeFail(UnifiedOrderResData unifiedOrderResData) {
		// TODO Auto-generated method stub
		int a = 10;
		
	}

	@Override
	public void onSuccess(UnifiedOrderResData unifiedOrderResData) {
		// TODO Auto-generated method stub
		int a = 10;
		
	}

	@Override
	public void onFail(UnifiedOrderResData unifiedOrderResData) {
		// TODO Auto-generated method stub
		int a = 10;
		
	}

}
