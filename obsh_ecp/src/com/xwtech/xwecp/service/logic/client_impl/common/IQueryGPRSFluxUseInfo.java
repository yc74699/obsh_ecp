package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040041Result;

public interface IQueryGPRSFluxUseInfo
{
	public QRY040041Result queryGPRSFluxUseInfo(String date, String idType) throws LIException;

}