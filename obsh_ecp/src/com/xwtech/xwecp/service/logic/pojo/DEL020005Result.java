package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.ZoneMValueInfo;

public class DEL020005Result extends BaseServiceInvocationResult
{
	private List<ZoneMValueInfo> zoneMValueInfo = new ArrayList<ZoneMValueInfo>();

	public void setZoneMValueInfo(List<ZoneMValueInfo> zoneMValueInfo)
	{
		this.zoneMValueInfo = zoneMValueInfo;
	}

	public List<ZoneMValueInfo> getZoneMValueInfo()
	{
		return this.zoneMValueInfo;
	}

}