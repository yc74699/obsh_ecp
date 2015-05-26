package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.B2B007Result;
import com.xwtech.xwecp.service.logic.pojo.DistributeInfo;

public interface IB2bCancelService {
	public B2B007Result b2bCancel(DistributeInfo distributeInfo,String disorderId) throws LIException;
}
