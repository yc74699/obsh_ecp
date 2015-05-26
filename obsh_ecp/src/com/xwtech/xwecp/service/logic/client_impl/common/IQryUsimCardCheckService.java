package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY040091Result;

public interface IQryUsimCardCheckService {
	public QRY040091Result qryUsimCardCheck(String cardsn , String password) throws LIException;
}
