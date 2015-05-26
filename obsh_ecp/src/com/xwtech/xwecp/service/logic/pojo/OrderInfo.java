package com.xwtech.xwecp.service.logic.pojo;


public class OrderInfo
{
	private String orderId;

	private String emsNo;

	private String status;

	private String siteName;

	private String fetchType;

	private String custName;

	private String fee;

	private String mobile;

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	public String getOrderId()
	{
		return this.orderId;
	}

	public void setEmsNo(String emsNo)
	{
		this.emsNo = emsNo;
	}

	public String getEmsNo()
	{
		return this.emsNo;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getStatus()
	{
		return this.status;
	}

	public void setSiteName(String siteName)
	{
		this.siteName = siteName;
	}

	public String getSiteName()
	{
		return this.siteName;
	}

	public void setFetchType(String fetchType)
	{
		this.fetchType = fetchType;
	}

	public String getFetchType()
	{
		return this.fetchType;
	}

	public void setCustName(String custName)
	{
		this.custName = custName;
	}

	public String getCustName()
	{
		return this.custName;
	}

	public void setFee(String fee)
	{
		this.fee = fee;
	}

	public String getFee()
	{
		return this.fee;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getMobile()
	{
		return this.mobile;
	}

}