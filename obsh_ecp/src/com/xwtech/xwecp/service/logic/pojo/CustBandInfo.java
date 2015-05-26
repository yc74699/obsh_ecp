package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;


public class CustBandInfo implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String loginNum;

	private String applyData;

	private String status;

	private String limitBandWidth;

	private String bandCode;

	private String address;

	private String bandSubsid;
	//增加宽带有效时间
	private String invalidDate;
	//增加宽带状态 （正在使用）
	private String statusName;
	
	public void setLoginNum(String loginNum)
	{
		this.loginNum = loginNum;
	}

	public String getLoginNum()
	{
		return this.loginNum;
	}

	public void setApplyData(String applyData)
	{
		this.applyData = applyData;
	}

	public String getApplyData()
	{
		return this.applyData;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getStatus()
	{
		return this.status;
	}

	public void setLimitBandWidth(String limitBandWidth)
	{
		this.limitBandWidth = limitBandWidth;
	}

	public String getLimitBandWidth()
	{
		return this.limitBandWidth;
	}

	public void setBandCode(String bandCode)
	{
		this.bandCode = bandCode;
	}

	public String getBandCode()
	{
		return this.bandCode;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getAddress()
	{
		return this.address;
	}

	public void setBandSubsid(String bandSubsid)
	{
		this.bandSubsid = bandSubsid;
	}

	public String getBandSubsid()
	{
		return this.bandSubsid;
	}
	
	public String getInvalidDate()
	{
		return invalidDate;
	}

	
	public void setInvalidDate(String invalidDate)
	{
		this.invalidDate = invalidDate;
	}

	
	public String getStatusName()
	{
		return statusName;
	}

	
	public void setStatusName(String statusName)
	{
		this.statusName = statusName;
	}

}