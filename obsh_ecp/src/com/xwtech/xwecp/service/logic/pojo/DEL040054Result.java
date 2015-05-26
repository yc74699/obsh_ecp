package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class DEL040054Result extends BaseServiceInvocationResult
{
	private String operStr;
	
	private String operDate;
	
	private RequestData requestData;
	
	private String requestSignData;
	
	private String requestAction;

	public String getRequestSignData() {
		return requestSignData;
	}

	public void setRequestSignData(String requestSignData) {
		this.requestSignData = requestSignData;
	}

	public RequestData getRequestData() {
		return requestData;
	}

	public void setRequestData(RequestData requestData) {
		this.requestData = requestData;
	}

	public String getRequestAction() {
		return requestAction;
	}

	public void setRequestAction(String requestAction) {
		this.requestAction = requestAction;
	}

	public String getOperStr() {
		return operStr;
	}

	public void setOperStr(String operStr) {
		this.operStr = operStr;
	}

	public String getOperDate() {
		return operDate;
	}

	public void setOperDate(String operDate) {
		this.operDate = operDate;
	}
}