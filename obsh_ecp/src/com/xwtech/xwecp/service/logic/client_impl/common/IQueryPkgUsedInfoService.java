package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040020Result;

public interface IQueryPkgUsedInfoService
{
	public QRY040020Result queryPkgUsedInfo(String phoneNum) throws LIException;

}