package com.xwtech.xwecp.service.logic.pojo;


public class OrderListInfo
{
	private String disOrderId;

	private String orgId;

	private String orgName;

	private String operAmount;

	private String leftAmount;

	private String signedNum;

	public void setDisOrderId(String disOrderId)
	{
		this.disOrderId = disOrderId;
	}

	public String getDisOrderId()
	{
		return this.disOrderId;
	}

	public void setOrgId(String orgId)
	{
		this.orgId = orgId;
	}

	public String getOrgId()
	{
		return this.orgId;
	}

	public void setOrgName(String orgName)
	{
		this.orgName = orgName;
	}

	public String getOrgName()
	{
		return this.orgName;
	}

	public void setOperAmount(String operAmount)
	{
		this.operAmount = operAmount;
	}

	public String getOperAmount()
	{
		return this.operAmount;
	}

	public void setLeftAmount(String leftAmount)
	{
		this.leftAmount = leftAmount;
	}

	public String getLeftAmount()
	{
		return this.leftAmount;
	}

	public void setSignedNum(String signedNum)
	{
		this.signedNum = signedNum;
	}

	public String getSignedNum()
	{
		return this.signedNum;
	}

}