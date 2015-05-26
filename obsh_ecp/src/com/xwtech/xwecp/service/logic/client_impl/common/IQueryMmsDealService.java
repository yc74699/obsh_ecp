package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040012Result;

public interface IQueryMmsDealService
{
	public QRY040012Result queryMmsDeal(String phoneNum, int scope) throws LIException;

}