package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050014Result;

public interface IQueryViceNumberService
{
	public QRY050014Result queryViceNumber(int queryType, String number) throws LIException;

}