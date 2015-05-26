package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010024Result;

public interface IQueryBillColumnarService
{
	public QRY010024Result queryBillColumnar(String subsid, String dbimonth) throws LIException;

}