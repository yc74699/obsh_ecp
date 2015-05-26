package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY030003Result;

public interface IQueryScoreExchangeDetailService
{
	public QRY030003Result queryScoreExchangeDetail(String phoneNum, String beginDate, String endDate) throws LIException;

}