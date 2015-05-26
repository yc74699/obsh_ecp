package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY060002Result;

public interface IQueryRecommendBizNewService
{
	public QRY060002Result queryRecommendBiz(String phoneNum, String actId, String isRecOrd, String isSplitChk,String channelId) throws LIException;

}