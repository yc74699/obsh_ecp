package com.xwtech.xwecp.service.logic.pojo;

import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.PkgUse;

public class PkgUseState
{
	private String pkgId;

	private String pkgName;

	private String pkgDesc;

	private int pkgType;

	private String beginDate;

	private String endDate;

	private int flag;

	private List<PkgUse> pkgUse = new ArrayList<PkgUse>();

	public void setPkgId(String pkgId)
	{
		this.pkgId = pkgId;
	}

	public String getPkgId()
	{
		return this.pkgId;
	}

	public void setPkgName(String pkgName)
	{
		this.pkgName = pkgName;
	}

	public String getPkgName()
	{
		return this.pkgName;
	}

	public void setPkgDesc(String pkgDesc)
	{
		this.pkgDesc = pkgDesc;
	}

	public String getPkgDesc()
	{
		return this.pkgDesc;
	}

	public void setPkgType(int pkgType)
	{
		this.pkgType = pkgType;
	}

	public int getPkgType()
	{
		return this.pkgType;
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

	public void setFlag(int flag)
	{
		this.flag = flag;
	}

	public int getFlag()
	{
		return this.flag;
	}

	public void setPkgUse(List<PkgUse> pkgUse)
	{
		this.pkgUse = pkgUse;
	}

	public List<PkgUse> getPkgUse()
	{
		return this.pkgUse;
	}

}