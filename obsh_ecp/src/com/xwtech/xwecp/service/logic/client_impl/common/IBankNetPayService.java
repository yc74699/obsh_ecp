package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040025Result;

public interface IBankNetPayService
{
	public DEL040025Result bankNetPay(String phoneNum, String amount, String bankType) throws LIException;

}