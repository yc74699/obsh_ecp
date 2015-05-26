package com.xwtech.xwecp.service.logic.pojo;

public class TrackInfo {
	private String seqNo;
	private String name;
	private String startTime;
	private String handlerName;
	private String handlerPhone;
	private String status;
	private String remark;
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getHandlerName() {
		return handlerName;
	}
	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}
	public String getHandlerPhone() {
		return handlerPhone;
	}
	public void setHandlerPhone(String handlerPhone) {
		this.handlerPhone = handlerPhone;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String toString()
	{
		return "TrackInfo [handlerName=" + handlerName + ", handlerPhone="
				+ handlerPhone + ", name=" + name + ", remark=" + remark
				+ ", seqNo=" + seqNo + ", startTime=" + startTime + ", status="
				+ status + "]";
	}
	
}
