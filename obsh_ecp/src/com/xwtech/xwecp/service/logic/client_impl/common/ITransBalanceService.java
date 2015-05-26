package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040030Result;

public interface ITransBalanceService
{
	public DEL040030Result transBalance(String formnum, String from_acctid, String from_userid, String from_msisdn, String from_subjectid, String to_acctid, String to_subjectid, String subsid, String msisdn, long transfee, String rectype, String accesstype, String paytype, String deal_type) throws LIException;

}