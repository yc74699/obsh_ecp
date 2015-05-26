package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY610049Result;

/**
 * 流量账户余额明细查询
 * @author YangXQ
 * 2015-04-22
 */
public interface IQueryFlowbalanceService
{
	public QRY610049Result flowbalance(
			String 	servnumber,
			String 	accttype,
			String  subjectid) throws LIException;

}