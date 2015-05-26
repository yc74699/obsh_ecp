package com.xwtech.xwecp.service.logic.pojo;

public class ChgProdInfo {
	private String prodId;
	private String type = "Type_NextCycle";
	private String operType;
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOperType() {
		return operType;
	}
	public void setOperType(String operType) {
		this.operType = operType;
	}
}
