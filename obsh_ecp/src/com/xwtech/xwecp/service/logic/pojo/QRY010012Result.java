package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.BlanceInfo;

public class QRY010012Result extends BaseServiceInvocationResult
{
	private List<BlanceInfo> blanceInfo = new ArrayList<BlanceInfo>();

	private long zyzh;

	public void setBlanceInfo(List<BlanceInfo> blanceInfo)
	{
		this.blanceInfo = blanceInfo;
	}

	public List<BlanceInfo> getBlanceInfo()
	{
		return this.blanceInfo;
	}

	public void setZyzh(long zyzh)
	{
		this.zyzh = zyzh;
	}

	public long getZyzh()
	{
		return this.zyzh;
	}

}