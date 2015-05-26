package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040004Result;

public interface IQueryOperDetailService
{
	public QRY040004Result queryOperDetail(String phoneNum, String beginDate, String endDate) throws LIException;

}