package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040008Result;

public interface IQueryShortNumService
{
	public QRY040008Result queryShortNum(String phoneNum, String queryType, String queryNum) throws LIException;

}