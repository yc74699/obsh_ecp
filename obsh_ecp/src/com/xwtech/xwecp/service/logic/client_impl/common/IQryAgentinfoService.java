package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040101Result;

/**
 * 查询渠道网点信息
 * @author YangXQ
 * 2015-03-17
 */
public interface IQryAgentinfoService
{
	public QRY040101Result qryAgentinfo(String region,String agentid) throws LIException;

}