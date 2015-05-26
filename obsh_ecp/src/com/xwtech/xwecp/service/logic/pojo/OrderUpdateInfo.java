package com.xwtech.xwecp.service.logic.pojo;

public class OrderUpdateInfo {

	private String officeId;

	private String expectTime;

	private String expectPeriod;

	private String bz;
	
	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getExpectPeriod() {
		return expectPeriod;
	}

	public void setExpectPeriod(String expectPeriod) {
		this.expectPeriod = expectPeriod;
	}

	public String getExpectTime() {
		return expectTime;
	}

	public void setExpectTime(String expectTime) {
		this.expectTime = expectTime;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

}
