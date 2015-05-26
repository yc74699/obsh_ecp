package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY020005Result;

public interface IQueryUserXpService
{
	public QRY020005Result queryUserXp(String phoneNum, int qryFlag) throws LIException;

}