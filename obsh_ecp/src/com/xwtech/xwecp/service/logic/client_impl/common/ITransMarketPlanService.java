package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL100005Result;

public interface ITransMarketPlanService
{ 
	public DEL100005Result transMarketPlan(String functionType, String servnumber, String actid, String privid, String packid, String busidpackid, String gettype, String orderid,String checkFlag,String operid,String rewardList) throws LIException;
 
}