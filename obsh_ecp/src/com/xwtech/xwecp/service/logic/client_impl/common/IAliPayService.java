package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL040029Result;

public interface IAliPayService
{
	public DEL040029Result aliPay(String phoneNum, String amount, String paymethod, String bankcode, int payFlag) throws LIException;

}