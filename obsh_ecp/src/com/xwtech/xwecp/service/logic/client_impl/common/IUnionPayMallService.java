package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040111Result;

/**
 * 
 * @author wang.huan
 *
 */
public interface IUnionPayMallService {
	
	public DEL040111Result unionPayMall(String phoneNum, int payFlag, String amount) throws LIException;
}
