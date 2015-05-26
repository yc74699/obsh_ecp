package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.MarketPlanInfo;

public class QRY110001Result extends BaseServiceInvocationResult
{
	private List<MarketPlanInfo> marketPlanInfos = new ArrayList<MarketPlanInfo>();

	public void setMarketPlanInfos(List<MarketPlanInfo> marketPlanInfos)
	{
		this.marketPlanInfos = marketPlanInfos;
	}

	public List<MarketPlanInfo> getMarketPlanInfos()
	{
		return this.marketPlanInfos;
	}

}