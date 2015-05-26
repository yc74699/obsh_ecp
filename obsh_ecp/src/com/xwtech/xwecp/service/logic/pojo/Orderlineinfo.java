package com.xwtech.xwecp.service.logic.pojo;


public class Orderlineinfo
{
	private String formnum;	
	private String recdefname;
	private String statusname;
	private String recfee;
	public String getFormnum() {
		return formnum;
	}
	public void setFormnum(String formnum) {
		this.formnum = formnum;
	}
	public String getRecdefname() {
		return recdefname;
	}
	public void setRecdefname(String recdefname) {
		this.recdefname = recdefname;
	}
	public String getStatusname() {
		return statusname;
	}
	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}
	public String getRecfee() {
		return recfee;
	}
	public void setRecfee(String recfee) {
		this.recfee = recfee;
	}
	@Override
	public String toString()
	{
		return "Orderlineinfo [formnum=" + formnum + ", recdefname="
				+ recdefname + ", recfee=" + recfee + ", statusname="
				+ statusname + "]";
	}
	
}