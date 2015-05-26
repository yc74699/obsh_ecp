package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.Prodinfodt;

public class QRY060074Result extends BaseServiceInvocationResult
{
	private String packmaxcount = "";

	private String packmincount = "";

	private List<Prodinfodt> prodinfodt = new ArrayList<Prodinfodt>();

	public void setPackmaxcount(String packmaxcount)
	{
		this.packmaxcount = packmaxcount;
	}

	public String getPackmaxcount()
	{
		return this.packmaxcount;
	}

	public void setPackmincount(String packmincount)
	{
		this.packmincount = packmincount;
	}

	public String getPackmincount()
	{
		return this.packmincount;
	}

	public void setProdinfodt(List<Prodinfodt> prodinfodt)
	{
		this.prodinfodt = prodinfodt;
	}

	public List<Prodinfodt> getProdinfodt()
	{
		return this.prodinfodt;
	}

}