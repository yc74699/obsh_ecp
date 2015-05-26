package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.SmsPkgInfo;

public class QRY040031Result extends BaseServiceInvocationResult
{
	private List<SmsPkgInfo> pkgInfoList = new ArrayList<SmsPkgInfo>();

	public void setPkgInfoList(List<SmsPkgInfo> pkgInfoList)
	{
		this.pkgInfoList = pkgInfoList;
	}

	public List<SmsPkgInfo> getPkgInfoList()
	{
		return this.pkgInfoList;
	}

}