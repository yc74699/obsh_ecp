package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040002Result;

public interface IQueryBalScoreService
{
	public QRY040002Result queryBalScore(String phoneNum, String brand) throws LIException;

}