package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040003Result;

public interface ICheckPasswordService
{
	public QRY040003Result checkPassword(String phoneNum, String password) throws LIException;

}