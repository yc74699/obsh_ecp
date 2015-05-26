package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010010Result;

public interface IQueryInternetFeeListService
{
	public QRY010010Result queryInternetFeeList(String phoneNum, String qryMonth) throws LIException;

}