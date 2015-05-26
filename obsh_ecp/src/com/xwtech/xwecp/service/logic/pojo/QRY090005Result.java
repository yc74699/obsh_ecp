package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.YxfaCheckInfo;

public class QRY090005Result extends BaseServiceInvocationResult
{
	private List<YxfaCheckInfo> yxfaCheckInfo = new ArrayList<YxfaCheckInfo>();

	public void setYxfaCheckInfo(List<YxfaCheckInfo> yxfaCheckInfo)
	{
		this.yxfaCheckInfo = yxfaCheckInfo;
	}

	public List<YxfaCheckInfo> getYxfaCheckInfo()
	{
		return this.yxfaCheckInfo;
	}

}