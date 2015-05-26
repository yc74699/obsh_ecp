package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050025Result;

public interface ICheckUserService
{
	public QRY050025Result checkUser(String phoneNum, String proId, String brandId, int oldPayMode, int payMode) throws LIException;

}