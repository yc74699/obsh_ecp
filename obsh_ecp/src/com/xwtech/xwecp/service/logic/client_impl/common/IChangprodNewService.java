package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL040035Result;

public interface IChangprodNewService
{
	public DEL040035Result changeProdNew(String city, String rectype, String prodId, String addProductSet, String delProductSet, String affectTime, String mobile, String serviceId) throws LIException;

}