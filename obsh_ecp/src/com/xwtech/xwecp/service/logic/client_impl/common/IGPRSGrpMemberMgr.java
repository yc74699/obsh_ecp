package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL040101Result;
/**
*定向白名单向后业务
*/
public interface IGPRSGrpMemberMgr
{
	public DEL040101Result gPRSGrpMemberMgr(String SERVNUMBER, String serviceid,String opertype, String prodid, String statusdate,String cityNum) throws LIException;

}