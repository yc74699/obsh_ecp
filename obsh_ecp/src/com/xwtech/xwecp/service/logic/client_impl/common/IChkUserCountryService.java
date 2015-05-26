package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050052Result;

public interface IChkUserCountryService
{
	public QRY050052Result chkUserCountry(String msisdn, String operator) throws LIException;

}