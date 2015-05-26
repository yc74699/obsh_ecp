package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040039Result;

public interface IQueryUserPayType
{
	public QRY040039Result queryUserPayType(String msisdn) throws LIException;

}