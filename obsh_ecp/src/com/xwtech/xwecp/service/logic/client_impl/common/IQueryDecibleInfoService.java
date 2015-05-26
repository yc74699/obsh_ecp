package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY020008Result;

public interface IQueryDecibleInfoService
{
	public QRY020008Result queryDecibleInfo(String phoneNum, String pkgId) throws LIException;

}