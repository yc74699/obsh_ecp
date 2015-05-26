package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY070008Result;

public interface IQryPlanValidDateService {
	public QRY070008Result qryPlanValidDate(long ddr_city, long msisdn,String plan_id) throws LIException;
}
