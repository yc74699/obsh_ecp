package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY040038Result;
import com.xwtech.xwecp.service.logic.LIException;
public interface IQueryMoneyAvailCountService
{
	public QRY040038Result queryMoneyAvailCount(String phoneNum, String amount) throws LIException;

}