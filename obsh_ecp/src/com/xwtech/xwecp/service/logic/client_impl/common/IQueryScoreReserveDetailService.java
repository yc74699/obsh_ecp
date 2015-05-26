package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY030004Result;

public interface IQueryScoreReserveDetailService
{
	public QRY030004Result queryScoreReserveDetail(String phoneNum, String beginDate, String endDate) throws LIException;

}