package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010018Result;

public interface IQueryNewRealTimeBillingService
{
	public QRY010018Result queryNewRealTimeBilling(String userMsisdn, Long idType, String modeId, String startcycle, String endcycle, Integer billType, Integer billlType) throws LIException;

}