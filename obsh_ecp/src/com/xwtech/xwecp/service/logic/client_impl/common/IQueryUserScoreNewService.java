package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY030011Result;

public interface IQueryUserScoreNewService
{
	public QRY030011Result queryUserScoreNew(String phoneNum, String flag, String beginYear, String endYear) throws LIException;

}