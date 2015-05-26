package com.xwtech.xwecp.service.logic.pojo;

import java.util.List;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY040066Result extends BaseServiceInvocationResult {
	private String contractCount;
	private List<RetList> retList;
	public String getContractCount() {
		return contractCount;
	}
	public void setContractCount(String contractCount) {
		this.contractCount = contractCount;
	}
	public List<RetList> getRetList() {
		return retList;
	}
	public void setRetList(List<RetList> retList) {
		this.retList = retList;
	}
	
}
