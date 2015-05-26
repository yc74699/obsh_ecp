package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY030002Result;

public interface IQueryUserScoreService
{
	public QRY030002Result queryUserScore(String phoneNum, int type) throws LIException;

}