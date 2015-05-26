package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.FluxDetail;

public class QRY040070Result extends BaseServiceInvocationResult
{
	private List<FluxDetail> fluxDetailList = new ArrayList<FluxDetail>();

	public void setFluxDetailList(List<FluxDetail> fluxDetailList)
	{
		this.fluxDetailList = fluxDetailList;
	}

	public List<FluxDetail> getFluxDetailList()
	{
		return this.fluxDetailList;
	}

}