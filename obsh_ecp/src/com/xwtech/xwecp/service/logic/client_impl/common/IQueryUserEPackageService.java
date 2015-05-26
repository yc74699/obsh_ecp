package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040014Result;

public interface IQueryUserEPackageService
{
	public QRY040014Result queryUserEPackage(String phoneNum) throws LIException;

}