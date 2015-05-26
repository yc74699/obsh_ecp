package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY050065Result;
import com.xwtech.xwecp.service.logic.LIException;

public interface IGetEdtByCtrService
{
	public QRY050065Result getEdtByCtr(String ddr_city, String contractid, String agreeoid) throws LIException;

}