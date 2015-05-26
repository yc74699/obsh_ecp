package com.xwtech.xwecp.service.logic.client_impl.reserve;

import com.xwtech.xwecp.service.logic.pojo.RES002Result;
import com.xwtech.xwecp.service.logic.LIException;
public interface IQryAmountByOfficeService
{
	public RES002Result qryAmountByOffice(String officeId, String expectTime, String expectPeriod, String amountFlag) throws LIException;

}