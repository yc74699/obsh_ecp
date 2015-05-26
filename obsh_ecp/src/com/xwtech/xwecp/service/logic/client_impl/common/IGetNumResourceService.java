package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050015Result;

public interface IGetNumResourceService
{
	public QRY050015Result getNumResource(String city, String country) throws LIException;

}