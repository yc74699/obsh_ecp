package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050005Result;

public interface IQryExperCfgService
{
	public QRY050005Result qryExperCfg(String phoneNum, String ddrCity, int qryFlag) throws LIException;

}