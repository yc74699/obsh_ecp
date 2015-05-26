package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;


public class Imei implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String privid;

	private String status;

	public void setPrivid(String privid)
	{
		this.privid = privid;
	}

	public String getPrivid()
	{
		return this.privid;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getStatus()
	{
		return this.status;
	}

	public String toString()
	{
		return "Imei [privid=" + privid + ", status=" + status + "]";
	}

}