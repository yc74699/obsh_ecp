package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY020003Result;

public interface IQueryPkgUseStateService
{
	public QRY020003Result queryPkgUseState(String phoneNum, int type, String pkgId) throws LIException;

}