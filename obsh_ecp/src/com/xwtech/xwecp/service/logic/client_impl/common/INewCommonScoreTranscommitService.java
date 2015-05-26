package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY090009Result;

public interface INewCommonScoreTranscommitService
{
	public QRY090009Result newCommonScoreTranscommit(String msisdn, String transoutscore, String transoutscoretype, String isfamily, String transinscore, String transinsernumber) throws LIException;

}