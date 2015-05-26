package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050011Result;

public interface IQueryUserTribeService
{
	public QRY050011Result queryUserTribe(String phoneNum) throws LIException;

}