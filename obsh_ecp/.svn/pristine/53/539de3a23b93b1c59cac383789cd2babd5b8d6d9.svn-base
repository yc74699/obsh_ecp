package com.xwtech.xwecp.service.logic.resolver;

import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.QRY070002Result;

/**
 * 查询档次(增加返回值地市)
 * @author YangXQ
 * 2014 - 09 -24
 */
public class QueryYxfaMxResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(QueryYxfaMxResolver.class);
	
	public QueryYxfaMxResolver()
	{
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		QRY070002Result res = (QRY070002Result)result;
		try
		{
			String city = (String) param.get(0).getParameterValue();//获取ddr_city值
			res.setDdr_city(city);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }

}