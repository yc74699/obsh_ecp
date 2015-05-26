package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.ProPackage;
import com.xwtech.xwecp.service.logic.pojo.ProService;
import com.xwtech.xwecp.service.logic.pojo.ProIncrement;
import com.xwtech.xwecp.service.logic.pojo.ProSelf;

public class QRY050034Result extends BaseServiceInvocationResult
{
	private List<ProPackage> proPackages = new ArrayList<ProPackage>();

	private List<ProPackage> otherPackages = new ArrayList<ProPackage>();

	private List<ProService> proServices = new ArrayList<ProService>();

	private List<ProIncrement> proIncrements = new ArrayList<ProIncrement>();

	private List<ProSelf> proSelfs = new ArrayList<ProSelf>();

	public void setProPackages(List<ProPackage> proPackages)
	{
		this.proPackages = proPackages;
	}

	public List<ProPackage> getProPackages()
	{
		return this.proPackages;
	}

	public void setOtherPackages(List<ProPackage> otherPackages)
	{
		this.otherPackages = otherPackages;
	}

	public List<ProPackage> getOtherPackages()
	{
		return this.otherPackages;
	}

	public void setProServices(List<ProService> proServices)
	{
		this.proServices = proServices;
	}

	public List<ProService> getProServices()
	{
		return this.proServices;
	}

	public void setProIncrements(List<ProIncrement> proIncrements)
	{
		this.proIncrements = proIncrements;
	}

	public List<ProIncrement> getProIncrements()
	{
		return this.proIncrements;
	}

	public void setProSelfs(List<ProSelf> proSelfs)
	{
		this.proSelfs = proSelfs;
	}

	public List<ProSelf> getProSelfs()
	{
		return this.proSelfs;
	}

}