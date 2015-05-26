package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

/**
 * 流量转移分发接口
 * @author 张斌
 * 2015-4-22
 */
public class QRY610050Result extends BaseServiceInvocationResult implements Serializable
{
	private String operatingSrl;

	public String getOperatingSrl() {
		return operatingSrl;
	}

	public void setOperatingSrl(String operatingSrl) {
		this.operatingSrl = operatingSrl;
	}
	
}