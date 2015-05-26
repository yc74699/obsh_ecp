package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL110002Result;

public interface IMarketPlanApply
{
	public DEL110002Result marketPlanApply(String planid, String subsid, String drawFlag, String rewardList, String goodsPackId, String busiPackId) throws LIException;

}