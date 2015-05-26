package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010042Result;

public interface IQueryPayHistoryFlagService
{
	/**
	 * 新增充值渠道历史记录查询
	 * @param args
	 * @throws Exception
	 */
	public QRY010042Result queryPayHistoryFlag(String phoneNum, String sDate, String eDate) throws LIException;

}