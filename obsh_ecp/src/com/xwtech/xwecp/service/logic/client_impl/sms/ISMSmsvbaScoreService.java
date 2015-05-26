package com.xwtech.xwecp.service.logic.client_impl.sms;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL020004Result;

public interface ISMSmsvbaScoreService
{
	public DEL020004Result smsmsvbaScore(String msisdn, String number, String password, String amount) throws LIException;

}