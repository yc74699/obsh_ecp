package com.xwtech.xwecp.service.logic.resolver;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;

/**
 * QRY020001 通用业务查询 密码修改白名单查询
 * @author yuantao
 * 2010-01-11
 */
public class QurUserPwdLmtResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(FindPackageResolver.class);
	
	public QurUserPwdLmtResolver ()
	{
		
	}
	
	/**
	 * 实现类
	 */
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
			                      List<RequestParameter> param) throws Exception
	{
		GommonBusiness dt = null;
		List<GommonBusiness> list = null;
		List<GommonBusiness> reList = new ArrayList();
		
		try
		{
			QRY020001Result ret = (QRY020001Result)result;
			list = ret.getGommonBusiness();
			
			//已开通
			if (null != list && list.size() > 0)
			{
				dt = (GommonBusiness)list.get(0);
				dt.setId("MMFW_MMCZBH");  //密码修改白名单查询
				dt.setName("密码重置保护"); //业务名称
				dt.setState(2);           //已开通
			}
			else  //未开通
			{
				dt = new GommonBusiness();
				dt.setId("MMFW_MMCZBH");  //密码修改白名单查询
				dt.setName("密码重置保护"); //业务名称
				dt.setState(1);           //未开通
			}
			reList.add(dt);
			ret.setGommonBusiness(reList);  //设置返回参数
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
}
