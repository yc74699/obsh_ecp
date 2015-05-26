package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.PkgUsedInfo;

public class PkgInfo implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String groupName;
	private String pkgName;
	private String pkgDec;
	private String pkgId;
	private List<PkgUsedInfo> subUsedInfoList = new ArrayList<PkgUsedInfo>();

	public void setPkgName(String pkgName)
	{
		this.pkgName = pkgName;
	}

	public String getPkgName()
	{
		return this.pkgName;
	}
	
	public String getPkgDec() {
		return pkgDec;
	}

	public void setPkgDec(String pkgDec) {
		this.pkgDec = pkgDec;
	}

	public String getPkgId() {
		return pkgId;
	}

	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}

	public void setSubUsedInfoList(List<PkgUsedInfo> subUsedInfoList)
	{
		this.subUsedInfoList = subUsedInfoList;
	}

	public List<PkgUsedInfo> getSubUsedInfoList()
	{
		return this.subUsedInfoList;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}