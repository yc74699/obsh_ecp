package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040040Result;

public interface IQueryCashLeaveMoneyService
{
	public QRY040040Result queryCashLeaveMoney(String phoneNum) throws LIException;

}