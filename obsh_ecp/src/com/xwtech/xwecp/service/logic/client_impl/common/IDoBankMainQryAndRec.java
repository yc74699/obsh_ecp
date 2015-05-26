package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050070Result;

public interface IDoBankMainQryAndRec {
	public QRY050070Result doBankMainQryAndRec(String operType,String phoneNum,String region,String manageType,String manageBlack)throws LIException ;
}
