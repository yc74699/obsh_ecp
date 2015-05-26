package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050035Result;

public interface IGetServiceAndIncService
{
	public QRY050035Result getServiceAndInc(String phoneNum) throws LIException;

}