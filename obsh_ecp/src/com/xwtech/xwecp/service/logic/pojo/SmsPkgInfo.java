package com.xwtech.xwecp.service.logic.pojo;

import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.SmsPkgUsedInfo;

public class SmsPkgInfo
{
	private String pkgName;

	private String pkgDec;

	private String pkgId;

	private String pkgType;

	private List<SmsPkgUsedInfo> subUsedInfoList = new ArrayList<SmsPkgUsedInfo>();

	public void setPkgName(String pkgName)
	{
		this.pkgName = pkgName;
	}

	public String getPkgName()
	{
		return this.pkgName;
	}

	public void setPkgDec(String pkgDec)
	{
		this.pkgDec = pkgDec;
	}

	public String getPkgDec()
	{
		return this.pkgDec;
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

	public void setSubUsedInfoList(List<SmsPkgUsedInfo> subUsedInfoList)
	{
		this.subUsedInfoList = subUsedInfoList;
	}

	public List<SmsPkgUsedInfo> getSubUsedInfoList()
	{
		return this.subUsedInfoList;
	}

}