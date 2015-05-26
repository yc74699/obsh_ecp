package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL040054Result;

public interface IPhonePayService
{
	public DEL040054Result phonePay(String phoneNum, String amount, String bankcode, String payFlag) throws LIException;

}