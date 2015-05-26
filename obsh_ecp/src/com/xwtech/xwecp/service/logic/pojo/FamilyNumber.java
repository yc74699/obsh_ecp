package com.xwtech.xwecp.service.logic.pojo;


public class FamilyNumber
{
	private String name;

	private String familyMsisdn;

	private String beginDate;

	private String endDate;

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

	public void setFamilyMsisdn(String familyMsisdn)
	{
		this.familyMsisdn = familyMsisdn;
	}

	public String getFamilyMsisdn()
	{
		return this.familyMsisdn;
	}

	public void setBeginDate(String beginDate)
	{
		this.beginDate = beginDate;
	}

	public String getBeginDate()
	{
		return this.beginDate;
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