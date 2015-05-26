package com.xwtech.xwecp.service;


import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.service.IServiceConfigDAO;
import com.xwtech.xwecp.memcached.IMemcachedManager;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.config.ServiceConfig;


public class ServiceLocator
{
	private static final Logger logger = Logger.getLogger(ServiceLocator.class);
	
	private IServiceConfigDAO serviceConfigDAO;
	
	public IServiceConfigDAO getServiceConfigDAO()
	{
		return serviceConfigDAO;
	}

	public void setServiceConfigDAO(IServiceConfigDAO serviceConfigDAO)
	{
		this.serviceConfigDAO = serviceConfigDAO;
	}
	
	/**
	 * 定位ServiceInfo
	 * @param cmd
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public ServiceInfo locate(String cmd, List<RequestParameter> params) throws ServiceException
	{
		ServiceInfo info = null;
		
		ServiceConfig config = serviceConfigDAO.getServiceConfig(cmd);
		if(config == null)
		{
			throw new ServiceException("找不到逻辑接口配置[cmd="+cmd+"]!");
		}
		IService service = (IService)XWECPApp.SPRING_CONTEXT.getBean("serviceImpl");
		service.initialize(config, params);
		info = new ServiceInfo();
		info.setServiceInstance(service);
		
		return info;
	}
}
