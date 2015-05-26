package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;

public class Packagedt implements Serializable {
	private String prodId;
	private String prodName;
	private String recDate;
	private String startDate;
	private String endDate;
	private String attrs;
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getRecDate() {
		return recDate;
	}
	public void setRecDate(String recDate) {
		this.recDate = recDate;
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
	public String getAttrs() {
		return attrs;
	}
	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}
}