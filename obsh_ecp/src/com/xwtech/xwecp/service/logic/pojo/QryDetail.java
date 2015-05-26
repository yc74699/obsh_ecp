package com.xwtech.xwecp.service.logic.pojo;

/**
 * 小额支付明细实体类
 * 
 * @author xufan
 * 2014-03-13
 */
public class QryDetail {

	private String business_id;
	
	private String crm_businessid;
	
	private String payfee;
	
	private String pantform_date;
	
	private String status_date;
	
	private String status;
	
	private String error_code;
	
	private String error_desc;
	
	private String merchant_info;

	private String queryType;
	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}


	public String getPayfee() {
		return payfee;
	}

	public void setPayfee(String payfee) {
		this.payfee = payfee;
	}

	public String getPantform_date() {
		return pantform_date;
	}

	public void setPantform_date(String pantform_date) {
		this.pantform_date = pantform_date;
	}

	public String getStatus_date() {
		return status_date;
	}

	public void setStatus_date(String status_date) {
		this.status_date = status_date;
	}

	public String getCrm_businessid() {
		return crm_businessid;
	}

	public void setCrm_businessid(String crm_businessid) {
		this.crm_businessid = crm_businessid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getError_desc() {
		return error_desc;
	}

	public void setError_desc(String error_desc) {
		this.error_desc = error_desc;
	}

	public String getMerchant_info() {
		return merchant_info;
	}

	public void setMerchant_info(String merchant_info) {
		this.merchant_info = merchant_info;
	}

}