package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.TransIncrementInfo;

public class QRY050045Result extends BaseServiceInvocationResult
{
	private List<TransIncrementInfo> incrementInfos = new ArrayList<TransIncrementInfo>();

	public void setIncrementInfos(List<TransIncrementInfo> incrementInfos)
	{
		this.incrementInfos = incrementInfos;
	}

	public List<TransIncrementInfo> getIncrementInfos()
	{
		return this.incrementInfos;
	}

}