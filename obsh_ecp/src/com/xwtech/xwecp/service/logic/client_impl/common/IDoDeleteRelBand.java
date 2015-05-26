package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040064Result;

public interface IDoDeleteRelBand
{
	public DEL040064Result doDeleteRelBand(String phoneNum, String vtelnum, String vtelinfo, String opertype, String accessnumber, String channeltype) throws LIException;

}