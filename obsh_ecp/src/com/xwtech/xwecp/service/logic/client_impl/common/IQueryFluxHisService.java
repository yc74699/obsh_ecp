package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY040071Result;

public interface IQueryFluxHisService {
	public QRY040071Result qryFluxHistory(String startMonth, String endMonth) throws LIException;
}
