package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.PackageManage;

public class QRY210010Result extends BaseServiceInvocationResult
{
	private List<PackageManage> package_info = new ArrayList<PackageManage>();
	
	private List<JTVWPackageInfo> jtvwPackageInfos = new ArrayList<JTVWPackageInfo>();

	public List<PackageManage> getPackage_info() {
		return package_info;
	}

	public void setPackage_info(List<PackageManage> package_info) {
		this.package_info = package_info;
	}

	public List<JTVWPackageInfo> getJtvwPackageInfos() {
		return jtvwPackageInfos;
	}

	public void setJtvwPackageInfos(List<JTVWPackageInfo> jtvwPackageInfos) {
		this.jtvwPackageInfos = jtvwPackageInfos;
	}
}