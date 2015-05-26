package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY060067Result;
import com.xwtech.xwecp.service.logic.LIException;
public interface IQueryOrderForNetService
{
	public QRY060067Result queryOrderForNet(String msisdn, String startData, String endData) throws LIException;

}