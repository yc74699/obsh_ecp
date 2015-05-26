package com.xwtech.xwecp.service.logic.pojo;

/**
 * 小额支付话费
 * 
 * @author xufan
 * 2014-03-13
 */
public class SmallPay
{

	private String invalid_date;

	private String balance;

	private String payfee;

	private String limitfee;

	private String usefee;

	public String getInvalid_date() {
		return invalid_date;
	}

	public void setInvalid_date(String invalid_date) {
		this.invalid_date = invalid_date;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getPayfee() {
		return payfee;
	}

	public void setPayfee(String payfee) {
		this.payfee = payfee;
	}

	public String getLimitfee() {
		return limitfee;
	}

	public void setLimitfee(String limitfee) {
		this.limitfee = limitfee;
	}

	public String getUsefee() {
		return usefee;
	}

	public void setUsefee(String usefee) {
		this.usefee = usefee;
	}

	

}