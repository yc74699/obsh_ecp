package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY090101Result;

public interface IQryUserActByImeiService
{
	public QRY090101Result qryUserActByImei(String imeiid) throws LIException;

}