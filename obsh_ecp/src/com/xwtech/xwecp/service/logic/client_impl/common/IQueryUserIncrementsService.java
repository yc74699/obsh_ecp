package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040023Result;

public interface IQueryUserIncrementsService
{
	public QRY040023Result queryUserIncrements(String phoneNum, String type) throws LIException;

}