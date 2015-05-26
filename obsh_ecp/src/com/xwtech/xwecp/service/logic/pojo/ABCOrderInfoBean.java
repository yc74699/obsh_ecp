package com.xwtech.xwecp.service.logic.pojo;


public class ABCOrderInfoBean
{
	private String orderid;

	private String servnumber;

	private String exchangeamt;

	private String bankstatus;

	private String isadjustatus;

	private String chargestatus;

	public void setOrderid(String orderid)
	{
		this.orderid = orderid;
	}

	public String getOrderid()
	{
		return this.orderid;
	}

	public void setServnumber(String servnumber)
	{
		this.servnumber = servnumber;
	}

	public String getServnumber()
	{
		return this.servnumber;
	}

	public void setExchangeamt(String exchangeamt)
	{
		this.exchangeamt = exchangeamt;
	}

	public String getExchangeamt()
	{
		return this.exchangeamt;
	}

	public void setBankstatus(String bankstatus)
	{
		this.bankstatus = bankstatus;
	}

	public String getBankstatus()
	{
		return this.bankstatus;
	}

	public void setIsadjustatus(String isadjustatus)
	{
		this.isadjustatus = isadjustatus;
	}

	public String getIsadjustatus()
	{
		return this.isadjustatus;
	}

	public void setChargestatus(String chargestatus)
	{
		this.chargestatus = chargestatus;
	}

	public String getChargestatus()
	{
		return this.chargestatus;
	}

}