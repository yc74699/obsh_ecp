package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040083Result;

public interface IQueryTbankDetailService {
	
	public QRY040083Result queryTbankDetail(String phoneNum, String oprType, String drawType,String beginDate,String endDate) throws LIException;
}
