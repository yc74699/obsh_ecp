package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040017Result;

public interface IQueryAgentSerialNumberService
{
	public QRY040017Result queryAgentSerialNumber(String phoneNum, String queryType, String startDate, String endDate) throws LIException;

}