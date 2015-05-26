package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050059Result;

public interface IQryFamilyPayPlan
{
	public QRY050059Result qryFamilyPayPlan(String msisdn) throws LIException;

}