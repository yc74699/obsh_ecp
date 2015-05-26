package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL010006Result;

public interface ICancelIncrementService
{
	public DEL010006Result cancelIncrement(String phoneNum, String orderId) throws LIException;

}