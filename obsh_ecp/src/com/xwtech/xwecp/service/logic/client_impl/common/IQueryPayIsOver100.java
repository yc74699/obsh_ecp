package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY010034Result;

public interface IQueryPayIsOver100
{
	public QRY010034Result queryPayIsOver100(String phoneNum) throws LIException;

}