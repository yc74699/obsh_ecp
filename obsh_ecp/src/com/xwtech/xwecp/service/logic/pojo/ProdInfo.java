package com.xwtech.xwecp.service.logic.pojo;


public class ProdInfo
{
	private String prodId;

	private String prodName;

	private String subMsisdn;

	private String startDate;

	private String endDate;

	public void setProdId(String prodId)
	{
		this.prodId = prodId;
	}

	public String getProdId()
	{
		return this.prodId;
	}

	public void setProdName(String prodName)
	{
		this.prodName = prodName;
	}

	public String getProdName()
	{
		return this.prodName;
	}

	public void setSubMsisdn(String subMsisdn)
	{
		this.subMsisdn = subMsisdn;
	}

	public String getSubMsisdn()
	{
		return this.subMsisdn;
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