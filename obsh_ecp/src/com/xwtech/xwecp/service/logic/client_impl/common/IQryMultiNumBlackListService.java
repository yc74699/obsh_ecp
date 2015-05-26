package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY040056Result;

public interface IQryMultiNumBlackListService {
	public QRY040056Result qryMultiNumBlackList(String userCity,String userSid,String vnumSid)throws LIException;
}
