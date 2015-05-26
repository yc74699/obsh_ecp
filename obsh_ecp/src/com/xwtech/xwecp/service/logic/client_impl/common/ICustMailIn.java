package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL030006Result;

public interface ICustMailIn
{
	public DEL030006Result custMailIn(String msisdn, String pwd, String address, String optType) throws LIException;

}