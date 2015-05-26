package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;


public class UserPkgInfo implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String pkgBagCode;

	private String pkgCode;

	private String pkgName;

	private String startDate;

	private String endDate;

	private String isBxtc;

	public void setPkgBagCode(String pkgBagCode)
	{
		this.pkgBagCode = pkgBagCode;
	}

	public String getPkgBagCode()
	{
		return this.pkgBagCode;
	}

	public void setPkgCode(String pkgCode)
	{
		this.pkgCode = pkgCode;
	}

	public String getPkgCode()
	{
		return this.pkgCode;
	}

	public void setPkgName(String pkgName)
	{
		this.pkgName = pkgName;
	}

	public String getPkgName()
	{
		return this.pkgName;
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

	public void setIsBxtc(String isBxtc)
	{
		this.isBxtc = isBxtc;
	}

	public String getIsBxtc()
	{
		return this.isBxtc;
	}

	public String toString()
	{
		return "UserPkgInfo [endDate=" + endDate + ", isBxtc=" + isBxtc
				+ ", pkgBagCode=" + pkgBagCode + ", pkgCode=" + pkgCode
				+ ", pkgName=" + pkgName + ", startDate=" + startDate + "]";
	}

}