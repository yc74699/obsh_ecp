package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY030014Result;

public interface IScbQueryService
{
	public QRY030014Result scbQuery(String phoneNum, String oprNum, String beginTime, String endTime) throws LIException;

}