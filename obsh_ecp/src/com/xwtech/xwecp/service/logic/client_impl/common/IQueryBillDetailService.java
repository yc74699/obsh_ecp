package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010001Result;

public interface IQueryBillDetailService
{
	public QRY010001Result queryBillDetail(String phoneNum, String beginDate, String endDate, int queryType, int attachParam) throws LIException;

}