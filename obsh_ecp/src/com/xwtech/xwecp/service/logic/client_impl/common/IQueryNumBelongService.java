package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050004Result;

public interface IQueryNumBelongService
{
	public QRY050004Result queryNumBelong(String phoneNum) throws LIException;

}