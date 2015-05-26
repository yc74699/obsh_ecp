package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040065Result;

public interface IDoBankContractCharge
{
	public DEL040065Result doBankContractCharge(String phoneNum, String contractid, String chargetel, String fee, String opertype) throws LIException;

}