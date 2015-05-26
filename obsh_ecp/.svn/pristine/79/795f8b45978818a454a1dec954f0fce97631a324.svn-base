package com.xwtech.xwecp.service;


import java.util.List;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.config.ServiceConfig;


public interface IService
{
	public void initialize(ServiceConfig config, List<RequestParameter> params) throws ServiceException;
	
	public BaseServiceInvocationResult execute(String accessId) throws ServiceException;
	
	//注入ServiceInfo对象
	public void setServiceInfo(ServiceInfo serviceInfo);
}
