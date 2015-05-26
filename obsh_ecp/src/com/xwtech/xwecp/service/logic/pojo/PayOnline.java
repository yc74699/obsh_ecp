package com.xwtech.xwecp.service.logic.pojo;


public class PayOnline
{
	private String payedMsisdn;

	private String startDate;

	private String endDate;

	public void setPayedMsisdn(String payedMsisdn)
	{
		this.payedMsisdn = payedMsisdn;
	}

	public String getPayedMsisdn()
	{
		return this.payedMsisdn;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public String getStartDate()
	{
		return this.startDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	public String getEndDate()
	{
		return this.endDate;
	}

}