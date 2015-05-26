package com.xwtech.xwecp.service.logic.pojo;

import java.util.List;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY040061Result extends BaseServiceInvocationResult {
	private String prodid;

	private String prod_instance_id;

	private List<LogLeft> loglist;

	public List<LogLeft> getLoglist() {
		return loglist;
	}

	public void setLoglist(List<LogLeft> loglist) {
		this.loglist = loglist;
	}

	public String getProd_instance_id() {
		return prod_instance_id;
	}

	public void setProd_instance_id(String prod_instance_id) {
		this.prod_instance_id = prod_instance_id;
	}

	public String getProdid() {
		return prodid;
	}

	public void setProdid(String prodid) {
		this.prodid = prodid;
	}

}
