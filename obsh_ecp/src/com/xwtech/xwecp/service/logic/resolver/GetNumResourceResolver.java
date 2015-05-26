package com.xwtech.xwecp.service.logic.resolver;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.NumResource;
import com.xwtech.xwecp.service.logic.pojo.QRY050015Result;

public class GetNumResourceResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(GetNumResourceResolver.class);
	
	public GetNumResourceResolver()
	{
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		try
		{
			QRY050015Result ret = (QRY050015Result)result;
			List list = ret.getNumResource();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				NumResource obj = (NumResource)it.next();
				if (obj.getReserve1() == 1) {
					it.remove();
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }
}
