package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL100004Result;

public interface ITransBindIMEIService
{
	public DEL100004Result transBindIMEI(String servnumber, String privid, String imei, String orderid) throws LIException;
	public DEL100004Result transBindIMEI(String servnumber, String privid, String imei, String orderid,String imeiorgid) throws LIException;
}