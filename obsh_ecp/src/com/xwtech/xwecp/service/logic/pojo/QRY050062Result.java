package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.PkgDetail;

public class QRY050062Result extends BaseServiceInvocationResult
{
	private List<PkgDetail> pkgDetailList = new ArrayList<PkgDetail>();

	public void setPkgDetailList(List<PkgDetail> pkgDetailList)
	{
		this.pkgDetailList = pkgDetailList;
	}

	public List<PkgDetail> getPkgDetailList()
	{
		return this.pkgDetailList;
	}

}