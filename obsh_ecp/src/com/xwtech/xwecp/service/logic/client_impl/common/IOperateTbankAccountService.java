package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040080Result;

public interface IOperateTbankAccountService {
	public DEL040080Result operateTbankAccount(String phoneNum, String accountType,String operType) throws LIException;
}
