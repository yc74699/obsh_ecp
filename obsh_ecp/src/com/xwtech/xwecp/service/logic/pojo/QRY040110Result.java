package com.xwtech.xwecp.service.logic.pojo;

import java.util.ArrayList;
import java.util.List;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY040110Result extends BaseServiceInvocationResult {
	
	private List<PkgInfo> pkgInfoList = new ArrayList<PkgInfo>();

	public void setPkgInfoList(List<PkgInfo> pkgInfoList)
	{
		this.pkgInfoList = pkgInfoList;
	}

	public List<PkgInfo> getPkgInfoList()
	{
		return this.pkgInfoList;
	}
}
