package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040035Result;

public interface IQueryCustBandInfo
{
	public QRY040035Result queryCustBandInfo(String msisdn) throws LIException;

}