package com.xwtech.xwecp.service.logic.pojo;


public class UserPackage
{
	private String proId;

	private String startDate;

	private String endDate;

	private String nextDate;

	private String pkgId;

	private String pkgType;

	private String packageCode;

	private String packageName;

	private String packageDesc;

	private String curOpen;

	private String pkgNumBoss;
	
	public String getPkgNumBoss() {
		return pkgNumBoss;
	}

	public void setPkgNumBoss(String pkgNumBoss) {
		this.pkgNumBoss = pkgNumBoss;
	}

	public void setProId(String proId)
	{
		this.proId = proId;
	}

	public String getProId()
	{
		return this.proId;
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

	public void setNextDate(String nextDate)
	{
		this.nextDate = nextDate;
	}

	public String getNextDate()
	{
		return this.nextDate;
	}

	public void setPkgId(String pkgId)
	{
		this.pkgId = pkgId;
	}

	public String getPkgId()
	{
		return this.pkgId;
	}

	public void setPkgType(String pkgType)
	{
		this.pkgType = pkgType;
	}

	public String getPkgType()
	{
		return this.pkgType;
	}

	public void setPackageCode(String packageCode)
	{
		this.packageCode = packageCode;
	}

	public String getPackageCode()
	{
		return this.packageCode;
	}

	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}

	public String getPackageName()
	{
		return this.packageName;
	}

	public void setPackageDesc(String packageDesc)
	{
		this.packageDesc = packageDesc;
	}

	public String getPackageDesc()
	{
		return this.packageDesc;
	}

	public void setCurOpen(String curOpen)
	{
		this.curOpen = curOpen;
	}

	public String getCurOpen()
	{
		return this.curOpen;
	}

}