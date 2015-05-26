package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040013Result;

public interface ITransactTrafficClerkService
{
	public DEL040013Result transactTrafficClerk(String phoneNum, String biztNo, int oprType, String driverNumber1, String driverNumber2, String carNumber, int carType, int chooseFlag, String donateNum) throws LIException;

}