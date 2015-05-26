package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040067Result;

public interface IQueryBugGetStrService {
  
//	public QRY040067Result queryBugGetStr(String phoneNum, String cityId,String bankType,String payFlag
//			,String discounttype ) throws LIException;
	public QRY040067Result queryBugGetStr(String phoneNum, String cityId,String bankType,String payFlag
	 ) throws LIException;
}
