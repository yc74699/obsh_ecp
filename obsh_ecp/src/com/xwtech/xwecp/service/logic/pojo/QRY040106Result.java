package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

/**
 * 查询用户实名制标签
 * @author wangh
 *
 */
public class QRY040106Result extends BaseServiceInvocationResult {
	/**
	 * 
	 */
	private String regStatus;
	private String regMode;
	private String regoperId;
	private String regDate;
	public String getRegStatus() {
		return regStatus;
	}
	public void setRegStatus(String regStatus) {
		this.regStatus = regStatus;
	}
	public String getRegMode() {
		return regMode;
	}
	public void setRegMode(String regMode) {
		this.regMode = regMode;
	}
	public String getRegoperId() {
		return regoperId;
	}
	public void setRegoperId(String regoperId) {
		this.regoperId = regoperId;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
}
