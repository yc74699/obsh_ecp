package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY090016Result;

public interface IQueryCardLoginService
{
	public QRY090016Result queryCardLoginInfo(String pcard_imsi, String tpcard_msisdn, String tpcard_pwd) throws LIException;

}