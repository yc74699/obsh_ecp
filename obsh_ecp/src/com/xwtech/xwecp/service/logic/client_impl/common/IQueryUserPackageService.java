package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050027Result;

public interface IQueryUserPackageService
{
	public QRY050027Result queryUserPackageList(String phoneNum, String city) throws LIException;

}