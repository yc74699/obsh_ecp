package com.xwtech.xwecp.service.logic.resolver;

import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.QRY040001Result;

public class QueryUserInfoResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(QueryUserInfoResolver.class);
	
	public QueryUserInfoResolver()
	{
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		QRY040001Result res = (QRY040001Result)result;
		try
		{
			res.setUserName("匿名");
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }

}