package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL030007Result;

public interface IResetMailPwd
{
	public DEL030007Result resetMailPwd(String msisdn, String findString) throws LIException;

}