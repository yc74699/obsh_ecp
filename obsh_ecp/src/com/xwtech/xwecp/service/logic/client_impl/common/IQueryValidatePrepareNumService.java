package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY020014Result;

public interface IQueryValidatePrepareNumService
{
	public QRY020014Result queryValidatePrepareNum(String agentId, String phoneNum) throws LIException;

}