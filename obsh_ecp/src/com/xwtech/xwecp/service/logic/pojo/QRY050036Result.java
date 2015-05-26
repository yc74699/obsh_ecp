package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.PackageInfo;

public class QRY050036Result extends BaseServiceInvocationResult
{
	private List<PackageInfo> closePackages = new ArrayList<PackageInfo>();

	public void setClosePackages(List<PackageInfo> closePackages)
	{
		this.closePackages = closePackages;
	}

	public List<PackageInfo> getClosePackages()
	{
		return this.closePackages;
	}

}