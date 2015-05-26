package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.AllowPlanIdInfo;

public class QRY110002Result extends BaseServiceInvocationResult
{
	private List<AllowPlanIdInfo> allowPlanIdInfos = new ArrayList<AllowPlanIdInfo>();

	public void setAllowPlanIdInfos(List<AllowPlanIdInfo> allowPlanIdInfos)
	{
		this.allowPlanIdInfos = allowPlanIdInfos;
	}

	public List<AllowPlanIdInfo> getAllowPlanIdInfos()
	{
		return this.allowPlanIdInfos;
	}

}