package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050023Result;

public interface IGetCloseServiceService
{
	public QRY050023Result getCloseService(String phoneNum, String oldProId, String newProId) throws LIException;

}