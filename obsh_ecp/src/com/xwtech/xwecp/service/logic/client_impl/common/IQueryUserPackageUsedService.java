package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040011Result;

public interface IQueryUserPackageUsedService
{
	public QRY040011Result queryUserPackageUsed(String phoneNum, int type, int scope) throws LIException;

}