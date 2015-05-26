package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.TrffInfo;

public class QRY050010Result extends BaseServiceInvocationResult
{
	private List<TrffInfo> trffInfo = new ArrayList<TrffInfo>();

	public void setTrffInfo(List<TrffInfo> trffInfo)
	{
		this.trffInfo = trffInfo;
	}

	public List<TrffInfo> getTrffInfo()
	{
		return this.trffInfo;
	}

}