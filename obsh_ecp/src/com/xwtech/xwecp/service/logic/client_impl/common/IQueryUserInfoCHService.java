package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040112Result;

public interface IQueryUserInfoCHService
{
	public QRY040112Result queryUserInfoCH(String phoneNum) throws LIException;

}