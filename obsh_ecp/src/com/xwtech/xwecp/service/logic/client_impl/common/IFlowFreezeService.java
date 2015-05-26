package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL610051Result;

/**
 * 流量冻结接口
 * @author wang.h
 *
 */
public interface IFlowFreezeService {
	public DEL610051Result flowFreeze (String formNum, String servNumber, 
			String paySubType, String flowValue) throws LIException;
}
