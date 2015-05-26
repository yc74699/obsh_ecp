package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class B2B007Result extends BaseServiceInvocationResult {
	private String busiId;
	private String succAmount;
	private String failAmount;
	public String getBusiId() {
		return busiId;
	}
	public void setBusiId(String busiId) {
		this.busiId = busiId;
	}
	public String getSuccAmount() {
		return succAmount;
	}
	public void setSuccAmount(String succAmount) {
		this.succAmount = succAmount;
	}
	public String getFailAmount() {
		return failAmount;
	}
	public void setFailAmount(String failAmount) {
		this.failAmount = failAmount;
	}
}
