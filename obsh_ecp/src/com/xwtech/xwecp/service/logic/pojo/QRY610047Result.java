package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY610047Result extends BaseServiceInvocationResult implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String ret_code;
	private String operating_srl;
	private String total_value;
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
	public String getTotal_value() {
		return total_value;
	}
	public void setTotal_value(String totalValue) {
		total_value = totalValue;
	}
	
	
}
