package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;

/**
 * 用户查询归属V网信息,返回报文
 * @author Administrator
 *
 */
public class Member_Info implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String familysubsid;//主号用户编号
	
	private String region;//家庭归属地
	
	private String memsubsid;//成员用户编号
	
	private String servnumber;//成员号码
	
	private String isprima;//是否主号
	
	private String memregion;//成员归属地
	
	private String shortnum;//短号号码
	
	private String startdate;//成员生效时间
	
	private String enddate;//成员失效时间
	
	private String memtype;//成员类型名称

	public String getFamilysubsid()
	{
		return familysubsid;
	}

	public void setFamilysubsid(String familysubsid)
	{
		this.familysubsid = familysubsid;
	}

	public String getRegion()
	{
		return region;
	}

	public void setRegion(String region)
	{
		this.region = region;
	}

	public String getMemsubsid()
	{
		return memsubsid;
	}

	public void setMemsubsid(String memsubsid)
	{
		this.memsubsid = memsubsid;
	}

	public String getServnumber()
	{
		return servnumber;
	}

	public void setServnumber(String servnumber)
	{
		this.servnumber = servnumber;
	}

	public String getIsprima()
	{
		return isprima;
	}

	public void setIsprima(String isprima)
	{
		this.isprima = isprima;
	}

	public String getMemregion()
	{
		return memregion;
	}

	public void setMemregion(String memregion)
	{
		this.memregion = memregion;
	}

	public String getShortnum()
	{
		return shortnum;
	}

	public void setShortnum(String shortnum)
	{
		this.shortnum = shortnum;
	}

	public String getStartdate()
	{
		return startdate;
	}

	public void setStartdate(String startdate)
	{
		this.startdate = startdate;
	}

	public String getEnddate()
	{
		return enddate;
	}

	public void setEnddate(String enddate)
	{
		this.enddate = enddate;
	}

	public String getMemtype()
	{
		return memtype;
	}

	public void setMemtype(String memtype)
	{
		this.memtype = memtype;
	}
	
	
}
