package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY120001Result;

public interface IQueryLableService
{
	public QRY120001Result queryLableInfo(String phoneNum) throws LIException;

}