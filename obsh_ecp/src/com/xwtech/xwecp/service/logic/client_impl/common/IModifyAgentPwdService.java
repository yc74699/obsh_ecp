package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040016Result;

public interface IModifyAgentPwdService
{
	public DEL040016Result modifyAgentPwd(String phoneNum, String agentNewPassword) throws LIException;

}