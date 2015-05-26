package com.xwtech.xwecp.service.logic.pojo;

public class CreditDetail {
 
	//信用服务类型
	private String creditservid;
	//信用额度
	private String creditvalue;
	//信用时长
	private String creditduration;
	//生效日期
	private String start_date;
	//失效日期
	private String end_date;
	
	
	public String getCreditduration() {
		return creditduration;
	}
	public void setCreditduration(String creditduration) {
		this.creditduration = creditduration;
	}
	public String getCreditservid() {
		return creditservid;
	}
	public void setCreditservid(String creditservid) {
		this.creditservid = creditservid;
	}
	public String getCreditvalue() {
		return creditvalue;
	}
	public void setCreditvalue(String creditvalue) {
		this.creditvalue = creditvalue;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	
	
	
	
	
}
