package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040089Result;

/**
 * GPRS套餐流量查询
 * @author YangXQ
 * 2014-11-04
 */
public interface IQrygprspkgfluxService
{
	public QRY040089Result queryOrderStatus(String subsid,String cycle,String cdrtype,String interfacetype) throws LIException;

}