package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050063Result;

public interface IGetQryFreeByNumService
{
	public QRY050063Result getQryFreeByNum(String ddr_city, String telnum, String begin_date, String end_date) throws LIException;

}