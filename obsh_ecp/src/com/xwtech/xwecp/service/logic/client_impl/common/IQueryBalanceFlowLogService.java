package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010020Result;

public interface IQueryBalanceFlowLogService
{
	public QRY010020Result queryBalanceFlowLog(String extId, String idType, String extQueryDateYm, String qryType, String isHome) throws LIException;

}