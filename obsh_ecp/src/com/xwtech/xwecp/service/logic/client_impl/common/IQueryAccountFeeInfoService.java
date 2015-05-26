package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010002Result;

public interface IQueryAccountFeeInfoService
{
	public QRY010002Result queryAccountFeeInfo(String phoneNum, String queryMonth) throws LIException;

}