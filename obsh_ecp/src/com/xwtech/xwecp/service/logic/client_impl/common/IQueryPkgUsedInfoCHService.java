package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040110Result;

public interface IQueryPkgUsedInfoCHService
{
	public QRY040110Result queryPkgUsedInfoCH(String phoneNum, String isPool) throws LIException;

}