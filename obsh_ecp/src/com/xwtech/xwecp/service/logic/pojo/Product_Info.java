package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;

/**
 * 主号查询V网产品信息,应答报文
 * @author Administrator
 *
 */
public class Product_Info implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String mainsubsid;//主号用户编号
	
	private String pkgprodid;//包
	
	private String increprodid;//增值产品编码
	
	private String remoteuse;//异地成员是否能享受
	
	private String startdate;//生效时间
	
	private String enddate;//失效时间

	public String getIncreprodid()
	{
		return increprodid;
	}

	public void setIncreprodid(String increprodid)
	{
		this.increprodid = increprodid;
	}

	public String getRemoteuse()
	{
		return remoteuse;
	}

	public void setRemoteuse(String remoteuse)
	{
		this.remoteuse = remoteuse;
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

	public String getMainsubsid()
	{
		return mainsubsid;
	}

	public void setMainsubsid(String mainsubsid)
	{
		this.mainsubsid = mainsubsid;
	}

	public String getPkgprodid()
	{
		return pkgprodid;
	}

	public void setPkgprodid(String pkgprodid)
	{
		this.pkgprodid = pkgprodid;
	}
	
	
}
