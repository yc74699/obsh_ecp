package com.xwtech.xwecp.dao.service;

import com.xwtech.xwecp.service.config.ServiceConfig;

public interface IServiceConfigDAO
{
	ServiceConfig getServiceConfig(String cmd);
	void addServiceConfig(String tmpXml,ServiceConfig serviceConfig);
}


