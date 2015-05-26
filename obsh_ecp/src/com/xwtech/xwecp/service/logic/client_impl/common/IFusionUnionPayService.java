package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL610038Result;

public interface IFusionUnionPayService
{
	public DEL610038Result fusionUnionPay(String phoneNum, int payFlag, String amount,String unionPaySrl) throws LIException;

}