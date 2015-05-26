package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class DEL610050Result extends BaseServiceInvocationResult implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String ret_code;
	
	private String operating_srl;

	public String getRet_code() {
		return ret_code;
	}

	public void setRet_code(String retCode) {
		ret_code = retCode;
	}

	public String getOperating_srl() {
		return operating_srl;
	}

	public void setOperating_srl(String operatingSrl) {
		operating_srl = operatingSrl;
	}
	
	

}
