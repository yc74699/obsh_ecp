package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;
import java.util.List;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY120002Result extends BaseServiceInvocationResult implements Serializable
{
	private List<ServiceSerialBean> gsmList;
	
	private List<ServiceSerialBean> smsList;

	public List<ServiceSerialBean> getGsmList() {
		return gsmList;
	}

	public void setGsmList(List<ServiceSerialBean> gsmList) {
		this.gsmList = gsmList;
	}

	public List<ServiceSerialBean> getSmsList() {
		return smsList;
	}

	public void setSmsList(List<ServiceSerialBean> smsList) {
		this.smsList = smsList;
	}
}