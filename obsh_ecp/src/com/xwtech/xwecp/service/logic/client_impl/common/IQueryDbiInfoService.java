package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010014Result;

public interface IQueryDbiInfoService
{
	public QRY010014Result queryDbiInfo(String phoneNum, String qryMonth) throws LIException;

}