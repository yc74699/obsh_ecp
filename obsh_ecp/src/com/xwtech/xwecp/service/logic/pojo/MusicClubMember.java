package com.xwtech.xwecp.service.logic.pojo;

import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.YearMusicInfo;
import com.xwtech.xwecp.service.logic.pojo.DecibleInfo;

public class MusicClubMember
{
	private String pkgId;

	private String memberType;

	private String beginDate;

	private String endDate;

	private String status;

	private List<YearMusicInfo> yearMusicInfo = new ArrayList<YearMusicInfo>();

	private DecibleInfo decibleInfo;

	public void setPkgId(String pkgId)
	{
		this.pkgId = pkgId;
	}

	public String getPkgId()
	{
		return this.pkgId;
	}

	public void setMemberType(String memberType)
	{
		this.memberType = memberType;
	}

	public String getMemberType()
	{
		return this.memberType;
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

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getStatus()
	{
		return this.status;
	}

	public void setYearMusicInfo(List<YearMusicInfo> yearMusicInfo)
	{
		this.yearMusicInfo = yearMusicInfo;
	}

	public List<YearMusicInfo> getYearMusicInfo()
	{
		return this.yearMusicInfo;
	}

	public void setDecibleInfo(DecibleInfo decibleInfo)
	{
		this.decibleInfo = decibleInfo;
	}

	public DecibleInfo getDecibleInfo()
	{
		return this.decibleInfo;
	}

}