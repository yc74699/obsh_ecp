package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.UserPackage;

public class QRY050027Result extends BaseServiceInvocationResult
{
	private String changeFlag;

	private List<UserPackage> userPackageList = new ArrayList<UserPackage>();

	public void setChangeFlag(String changeFlag)
	{
		this.changeFlag = changeFlag;
	}

	public String getChangeFlag()
	{
		return this.changeFlag;
	}

	public void setUserPackageList(List<UserPackage> userPackageList)
	{
		this.userPackageList = userPackageList;
	}

	public List<UserPackage> getUserPackageList()
	{
		return this.userPackageList;
	}

}