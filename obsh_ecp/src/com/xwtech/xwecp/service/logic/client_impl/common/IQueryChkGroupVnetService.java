package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY090006Result;

public interface IQueryChkGroupVnetService
{
	public QRY090006Result chkGroupVnet(String ddr_city, String main_msisdn) throws LIException;

}