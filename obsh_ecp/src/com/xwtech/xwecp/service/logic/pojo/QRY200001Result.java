package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.WMemberInfo;

public class QRY200001Result extends BaseServiceInvocationResult
{
	private List<WMemberInfo> wMemberInfo = new ArrayList<WMemberInfo>();

	public void setWMemberInfo(List<WMemberInfo> wMemberInfo)
	{
		this.wMemberInfo = wMemberInfo;
	}

	public List<WMemberInfo> getWMemberInfo()
	{
		return this.wMemberInfo;
	}

}