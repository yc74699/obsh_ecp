package com.xwtech.xwecp.service.logic.pojo;


import com.xwtech.xwecp.service.BaseServiceInvocationResult;

/**
 * 客户经理信息查询
 * @author wang.h
 *
 */
public class QRY040105Result  extends BaseServiceInvocationResult  {
	private ManagerInfo managerInfo = new ManagerInfo();

	public ManagerInfo getManagerInfo() {
		return managerInfo;
	}

	public void setManagerInfo(ManagerInfo managerInfo) {
		this.managerInfo = managerInfo;
	}
	
	
}
