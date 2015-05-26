package com.xwtech.xwecp.service.logic.client_impl.sms;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL020005Result;

public interface IMgrZoneMScoreService
{
	public DEL020005Result mgrZoneMScore(String msisdn, String number, String redeeAmount, String randomPwd, String flag) throws LIException;

}