package com.xwtech.xwecp.service.logic.client_impl.common;



import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050055Result;

public interface IQryReinforceInfoService
{
	public QRY050055Result qryreInforceInfo(String cardNum) throws LIException;

}