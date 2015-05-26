package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY020010Result;

public interface IQueryMobileGameService
{
	public QRY020010Result queryMobileGame(String phoneNum) throws LIException;

}