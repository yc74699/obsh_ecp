package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY020004Result;

public interface IQueryEmailBizStateService
{
	public QRY020004Result queryEmailBizState(String phoneNum) throws LIException;

}