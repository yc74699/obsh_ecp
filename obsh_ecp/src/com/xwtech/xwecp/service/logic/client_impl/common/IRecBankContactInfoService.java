package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL300002Result;

public interface IRecBankContactInfoService
{
	public DEL300002Result recBankContactInfo(String msisdn,String fee,String bankacct,String fundtype) throws LIException;

}