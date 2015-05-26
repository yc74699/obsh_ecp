package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL610050Result;

public interface IPcustfullService {

	public DEL610050Result pcustFull(String userId, String custId,
			String ddrCity, String custIcNo, String custIcType,
			String custName, String custIcAddr, String custContactAdd)
			throws LIException;
}
