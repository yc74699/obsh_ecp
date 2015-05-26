package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;

public interface IQueryBusinessService
{
	public QRY020001Result queryBusiness(String phoneNum, int type, String bizId) throws LIException;
}