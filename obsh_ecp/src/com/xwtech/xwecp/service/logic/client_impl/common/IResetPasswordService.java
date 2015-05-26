package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040113Result;

/**
 * 密码重置
 * @author wang.huan
 *
 */
public interface IResetPasswordService {
	public DEL040113Result resetPassword(String agentid , String phoneno,String passwordNew) throws LIException;
}
