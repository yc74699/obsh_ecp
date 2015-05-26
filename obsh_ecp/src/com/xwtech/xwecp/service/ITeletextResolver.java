package com.xwtech.xwecp.service;


import java.util.List;

import com.xwtech.xwecp.msg.RequestParameter;


public interface ITeletextResolver
{
	void resolve(BaseServiceInvocationResult result, Object bossResponse, List<RequestParameter> param) throws Exception;
}
