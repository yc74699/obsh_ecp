package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040084Result;

public interface ITBankWithDrawAccountService {
	
	public DEL040084Result withDrawAccount(String phoneNum, String oprType,String bizNo,String amount) throws LIException;
}
