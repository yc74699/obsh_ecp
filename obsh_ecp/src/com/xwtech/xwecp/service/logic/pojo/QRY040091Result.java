package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY040091Result extends BaseServiceInvocationResult {
	
	/**<ret_code>0</ret_code>
	<check_status>1</check_status>
	<imsi>460027620215619</imsi>
	<region>12</region>**/

	private long retCode;
	private long checkStatus;
	private String imsi;
	private String region;
	public long getRetCode() {
		return retCode;
	}
	public void setRetCode(long retCode) {
		this.retCode = retCode;
	}
	public long getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(long checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	
	
}
