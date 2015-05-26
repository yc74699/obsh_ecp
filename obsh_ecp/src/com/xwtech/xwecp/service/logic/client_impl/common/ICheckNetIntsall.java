package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050048Result;

public interface ICheckNetIntsall
{
	public QRY050048Result checkNetIntsall(String ddrCity, String userMsisdn) throws LIException;

}