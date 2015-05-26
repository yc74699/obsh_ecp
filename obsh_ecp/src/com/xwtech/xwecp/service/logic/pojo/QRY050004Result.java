package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY050004Result extends BaseServiceInvocationResult
{
	private String provinceName = "";

	private String cityName = "";

	private String areaCode = "";

	public void setProvinceName(String provinceName)
	{
		this.provinceName = provinceName;
	}

	public String getProvinceName()
	{
		return this.provinceName;
	}

	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	public String getCityName()
	{
		return this.cityName;
	}

	public void setAreaCode(String areaCode)
	{
		this.areaCode = areaCode;
	}

	public String getAreaCode()
	{
		return this.areaCode;
	}

}