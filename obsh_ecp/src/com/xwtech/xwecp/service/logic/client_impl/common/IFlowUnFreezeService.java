package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL610052Result;

/**
 * 流量解冻接口
 * @author wang.h
 *
 */
public interface IFlowUnFreezeService {
	public DEL610052Result flowUnfreeze(String servNumber , String formNum, 
			String frozeSrl, String flowValue) throws LIException;
}
