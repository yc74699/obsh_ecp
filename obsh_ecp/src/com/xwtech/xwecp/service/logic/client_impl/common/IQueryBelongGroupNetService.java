package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY060073Result;

public interface IQueryBelongGroupNetService
{
	public QRY060073Result queryBelongGroupNet(String groupName, String region) throws LIException;

}