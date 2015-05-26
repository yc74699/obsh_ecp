package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040015Result;

public interface IQueryAgentBalanceService
{
	public QRY040015Result queryAgentBalance(String phoneNum, String userId, String cityId) throws LIException;

}