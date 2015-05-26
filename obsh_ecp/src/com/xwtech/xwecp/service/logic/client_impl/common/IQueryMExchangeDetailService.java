package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY030005Result;

public interface IQueryMExchangeDetailService
{
	public QRY030005Result queryMExchangeDetail(String phoneNum, String beginDate, String endDate) throws LIException;

}