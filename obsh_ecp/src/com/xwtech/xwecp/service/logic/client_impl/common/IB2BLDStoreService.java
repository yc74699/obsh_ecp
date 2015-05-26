package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.B2B009Result;
import com.xwtech.xwecp.service.logic.pojo.DistributeInfo;
import com.xwtech.xwecp.service.logic.LIException;

public interface IB2BLDStoreService
{
	public B2B009Result b2bLDStore(DistributeInfo distributeInfo) throws LIException;

}