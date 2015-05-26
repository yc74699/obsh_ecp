package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL070002Result;

public interface IExchangeConfirmService
{
	public DEL070002Result exchangeConfirm(String confirmSeq, String merAwardNum, String merAccountNum, String merAccountPwd, int type) throws LIException;

}