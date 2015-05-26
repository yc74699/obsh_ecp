package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040082Result;

public interface IQueryAccountStatusAndDetailService {
	
	public QRY040082Result queryAccountStatusAndDetail(String phoneNum,String oprtype,String accountOrBookId,String bizNo,String beginDate,String endDate) throws LIException;
}
