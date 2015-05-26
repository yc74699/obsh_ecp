package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040016Result;

public interface IQueryAgentInfoService
{
	public QRY040016Result queryAgentInfo(String phoneNum, String agentPassWord) throws LIException;

}