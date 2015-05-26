package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY090012Result;

public interface IQueryMmsDealNewService
{
	public QRY090012Result queryMmsDealNew(String phoneNum, int scope, String bossmarket_rec_type) throws LIException;

}