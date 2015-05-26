package com.xwtech.xwecp.service.logic.client_impl.common;



import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010033Result;

public interface IQueryreinforceinfoService
{
	public QRY010033Result qryreInforceInfo(String cardId,String seqno, int flag) throws LIException;

}