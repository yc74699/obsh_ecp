package com.xwtech.xwecp.service.server;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.ServiceInfo;

public abstract class BaseServerServiceImpl {
	private static final Logger logger = Logger.getLogger(BaseServerServiceImpl.class);

	protected IRemote remote;

	protected WellFormedDAO wellFormedDAO; // 支持JDBC直接访问数据库和memcached(已包含在DAO中),
	// 子类也可以自己写DAO注入

	//注入ServiceInfo 对象
	protected ServiceInfo serviceInfo;

	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}

	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	public IRemote getRemote() {
		return remote;
	}

	public void setRemote(IRemote remote) {
		this.remote = remote;
	}

	public WellFormedDAO getWellFormedDAO() {
		return wellFormedDAO;
	}

	public void setWellFormedDAO(WellFormedDAO wellFormedDAO) {
		this.wellFormedDAO = wellFormedDAO;
	}
}
