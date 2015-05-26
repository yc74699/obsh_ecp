package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040009Result;

public interface IQueryPhoneNumBySimService
{
	public QRY040009Result queryPhoneNumBySim(String sId, String password, String cityNum) throws LIException;

}