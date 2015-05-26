package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY070006Result;

public interface IQueryYxfaYktService
{
	public QRY070006Result queryYxfaYkt(String ddr_city, String usermarketingbaseinfo_user_id) throws LIException;

}