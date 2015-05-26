package com.xwtech.xwecp.service.logic.resolver;

import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.QRY070001Result;

/**
 * 查询营销方案列表(增加返回值地市)
 * @author YangXQ
 * 2014 - 09 -24
 */
public class QueryYxfaList1Resolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(QueryYxfaList1Resolver.class);
	
	public QueryYxfaList1Resolver()
	{
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		QRY070001Result res = (QRY070001Result)result;
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