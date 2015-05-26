package com.xwtech.xwecp.service.logic.pojo;

import java.util.ArrayList;
import java.util.List;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY040093Result extends BaseServiceInvocationResult {
	
	private String retCode;
	private String retMsg;
	
	private List<CorginfoDetail> list = new ArrayList<CorginfoDetail>();

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public List<CorginfoDetail> getList() {
		return list;
	}

	public void setList(List<CorginfoDetail> list) {
		this.list = list;
	}
	
	
	
}
