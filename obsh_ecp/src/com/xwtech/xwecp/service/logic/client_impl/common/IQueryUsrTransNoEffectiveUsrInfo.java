package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050043Result;

public interface IQueryUsrTransNoEffectiveUsrInfo
{
	public QRY050043Result getUsrTransNoEffectiveUsrInfo(String phoneNum) throws LIException;

}