package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL100008Result;

public interface ITransMarketPlanBroadBandService
{ 
	public DEL100008Result transMarketPlan(String servnumber, String actid, String privid, String packid, String busidpackid, String gettype, String orderid,String operid,String rewardList) throws LIException;
 
}