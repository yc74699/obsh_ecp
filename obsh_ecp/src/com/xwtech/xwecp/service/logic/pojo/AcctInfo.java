package com.xwtech.xwecp.service.logic.pojo;


public class AcctInfo
{
	private String acct_name;

	private String acct_type;

	private String pre_paytype;

	private String status;

	private String settle_day;

	public void setAcct_name(String acct_name)
	{
		this.acct_name = acct_name;
	}

	public String getAcct_name()
	{
		return this.acct_name;
	}

	public void setAcct_type(String acct_type)
	{
		this.acct_type = acct_type;
	}

	public String getAcct_type()
	{
		return this.acct_type;
	}

	public void setPre_paytype(String pre_paytype)
	{
		this.pre_paytype = pre_paytype;
	}

	public String getPre_paytype()
	{
		return this.pre_paytype;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getStatus()
	{
		return this.status;
	}

	public void setSettle_day(String settle_day)
	{
		this.settle_day = settle_day;
	}

	public String getSettle_day()
	{
		return this.settle_day;
	}

}