package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040024Result;

public interface IvrNetBookService
{
	public DEL040024Result vrNetBookService(String phoneNum, String city, String cardPwd, String amount, String isMZone) throws LIException;

}