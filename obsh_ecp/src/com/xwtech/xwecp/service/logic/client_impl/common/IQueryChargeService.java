package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010013Result;

public interface IQueryChargeService
{
	public QRY010013Result queryCharge(String phoneNum, String queryMonth) throws LIException;

}