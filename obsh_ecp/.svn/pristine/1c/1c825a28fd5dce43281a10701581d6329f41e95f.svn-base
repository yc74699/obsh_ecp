package com.xwtech.xwecp.service.logic.resolver;

import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.QRY050040Result;

public class CheckUserTransValidResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(CheckUserTransValidResolver.class);
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		try
		{
			QRY050040Result ret = (QRY050040Result)result;
			
			String checkCode = ret.getChkresult();
			
			if(null != checkCode)
			{
				String resultCode = checkCode.equals("0") ? "0" : "1" ;
				
				ret.setResultCode(resultCode);
			}
				
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }
}