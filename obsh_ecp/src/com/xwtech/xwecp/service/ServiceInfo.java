package com.xwtech.xwecp.service;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.ServiceMessage;

public class ServiceInfo {
	private static final Logger logger = Logger.getLogger(ServiceInfo.class);

	private IService serviceInstance;

	private ServiceMessage serviceMessage;

	public ServiceMessage getServiceMessage() {
		return serviceMessage;
	}

	public void setServiceMessage(ServiceMessage serviceMessage) {
		this.serviceMessage = serviceMessage;
	}

	public IService getServiceInstance() {
		return serviceInstance;
	}

	public void setServiceInstance(IService serviceInstance) {
		this.serviceInstance = serviceInstance;

		/**
		 * 2011-11-15 maofw修改 IService中注入ServiceInfo对象
		 */

		if ( serviceInstance != null ) {
			serviceInstance.setServiceInfo(this);
		}
	}
}
