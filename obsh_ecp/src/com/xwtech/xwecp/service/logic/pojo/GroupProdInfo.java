package com.xwtech.xwecp.service.logic.pojo;


public class GroupProdInfo
{
	private String groupProdName;

	private String groupProdStartDate;

	private String groupProdEndDate;
	
	private String groupProdProdid;

	public void setGroupProdName(String groupProdName)
	{
		this.groupProdName = groupProdName;
	}

	public String getGroupProdName()
	{
		return this.groupProdName;
	}

	public void setGroupProdStartDate(String groupProdStartDate)
	{
		this.groupProdStartDate = groupProdStartDate;
	}

	public String getGroupProdStartDate()
	{
		return this.groupProdStartDate;
	}

	public void setGroupProdEndDate(String groupProdEndDate)
	{
		this.groupProdEndDate = groupProdEndDate;
	}

	public String getGroupProdEndDate()
	{
		return this.groupProdEndDate;
	}

	public String getGroupProdProdid() {
		return groupProdProdid;
	}

	public void setGroupProdProdid(String groupProdProdid) {
		this.groupProdProdid = groupProdProdid;
	}
	

}