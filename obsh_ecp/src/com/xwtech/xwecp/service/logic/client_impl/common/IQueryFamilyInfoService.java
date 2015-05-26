package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY200001Result;

public interface IQueryFamilyInfoService
{
	public QRY200001Result queryFamilyInfoService(String subsid) throws LIException;

}