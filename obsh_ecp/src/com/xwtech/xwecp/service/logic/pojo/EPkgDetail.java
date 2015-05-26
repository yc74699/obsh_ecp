package com.xwtech.xwecp.service.logic.pojo;


public class EPkgDetail
{
	private String pkgName;
	
	private String pkgCode;

	private String beginDate;

	private String endDate;

	private String use;

	private String flag;

	public void setPkgName(String pkgName)
	{
		this.pkgName = pkgName;
	}

	public String getPkgName()
	{
		return this.pkgName;
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

	public void setUse(String use)
	{
		this.use = use;
	}

	public String getUse()
	{
		return this.use;
	}

	public void setFlag(String flag)
	{
		this.flag = flag;
	}

	public String getFlag()
	{
		return this.flag;
	}

	public String getPkgCode() {
		return pkgCode;
	}

	public void setPkgCode(String pkgCode) {
		this.pkgCode = pkgCode;
	}

}