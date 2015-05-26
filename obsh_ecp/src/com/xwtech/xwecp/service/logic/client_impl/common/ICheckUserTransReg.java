package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050040Result;

public interface ICheckUserTransReg
{
	public QRY050040Result checkUserTransReg(String phoneNum, String fromCity, String toCity, String userId) throws LIException;

}