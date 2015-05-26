package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.PackageInfoBean;

public class QRY040037Result extends BaseServiceInvocationResult
{
	private List<PackageInfoBean> packageInfoList = new ArrayList<PackageInfoBean>();

	public void setPackageInfoList(List<PackageInfoBean> packageInfoList)
	{
		this.packageInfoList = packageInfoList;
	}

	public List<PackageInfoBean> getPackageInfoList()
	{
		return this.packageInfoList;
	}

}