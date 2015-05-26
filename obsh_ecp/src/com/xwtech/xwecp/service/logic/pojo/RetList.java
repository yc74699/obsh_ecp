package com.xwtech.xwecp.service.logic.pojo;

public class RetList {
	private String servNumber;
	private String bankAcct;
	private String startDate;
	private String endDate;
	private String payFlag;
	private String drawamt;
	private String trigamt;
	private String accessType;
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public String getServNumber() {
		return servNumber;
	}
	public void setServNumber(String servNumber) {
		this.servNumber = servNumber;
	}
	public String getBankAcct() {
		return bankAcct;
	}
	public void setBankAcct(String bankAcct) {
		this.bankAcct = bankAcct;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getPayFlag() {
		return payFlag;
	}
	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}
	public String getDrawamt() {
		return drawamt;
	}
	public void setDrawamt(String drawamt) {
		this.drawamt = drawamt;
	}
	public String getTrigamt() {
		return trigamt;
	}
	public void setTrigamt(String trigamt) {
		this.trigamt = trigamt;
	}
}
