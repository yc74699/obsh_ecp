package com.xwtech.xwecp.service.logic.pojo;


public class ServiceInfo
{
	private String serviceId;

	private String serviceName;

	private String startDate;

	private String endDate;

	private int state;

	public void setServiceId(String serviceId)
	{
		this.serviceId = serviceId;
	}

	public String getServiceId()
	{
		return this.serviceId;
	}

	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}

	public String getServiceName()
	{
		return this.serviceName;
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

	public void setState(int state)
	{
		this.state = state;
	}

	public int getState()
	{
		return this.state;
	}

}