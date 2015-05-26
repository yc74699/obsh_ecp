package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010034Result;

public interface IQueryPayListService
{
	public QRY010034Result queryPayHistory(String phoneNum, String sDate, String eDate,String typeMoney) throws LIException;

}