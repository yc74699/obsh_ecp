package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.REC001Result;

public interface IGetRecommendInfoService
{
	public REC001Result getRecommendInfo(String phoneNum, String region,String privid) throws LIException;

}