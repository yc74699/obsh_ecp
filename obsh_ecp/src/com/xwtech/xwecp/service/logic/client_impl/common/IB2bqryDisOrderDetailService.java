package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.B2B006Result;

public interface IB2bqryDisOrderDetailService {
	public B2B006Result b2bqryDisOrderDetail(String region, String operId, String queryType,String disorderId, String pageSize,String pageIndex) throws LIException;
}
