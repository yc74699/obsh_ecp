package com.xwtech.xwecp.service.logic.pojo;


public class FamilyPay
{
	private String familyservnumber;

	private String subsid;

	private String paytype;

	private String payvalue;

	private String startdate;

	public void setFamilyservnumber(String familyservnumber)
	{
		this.familyservnumber = familyservnumber;
	}

	public String getFamilyservnumber()
	{
		return this.familyservnumber;
	}

	public void setSubsid(String subsid)
	{
		this.subsid = subsid;
	}

	public String getSubsid()
	{
		return this.subsid;
	}

	public void setPaytype(String paytype)
	{
		this.paytype = paytype;
	}

	public String getPaytype()
	{
		return this.paytype;
	}

	public void setPayvalue(String payvalue)
	{
		this.payvalue = payvalue;
	}

	public String getPayvalue()
	{
		return this.payvalue;
	}

	public void setStartdate(String startdate)
	{
		this.startdate = startdate;
	}

	public String getStartdate()
	{
		return this.startdate;
	}

}