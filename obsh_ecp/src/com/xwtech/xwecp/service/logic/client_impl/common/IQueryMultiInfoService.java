package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY020006Result;

public interface IQueryMultiInfoService
{
	public QRY020006Result queryMultiInfo(String phoneNum) throws LIException;

}