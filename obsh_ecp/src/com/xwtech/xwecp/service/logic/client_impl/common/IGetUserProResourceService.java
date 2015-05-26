package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050022Result;

public interface IGetUserProResourceService
{
	public QRY050022Result getUserProResource(String productId) throws LIException;

}