package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040087Result;

/**
 * 20元封顶套餐查询接口
 * @author YangXQ
 * 2014-09-25
 */
public interface IQuery20YFDTCService
{
	public QRY040087Result query20YFDTC(String msisdn) throws LIException;

}