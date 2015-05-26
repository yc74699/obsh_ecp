package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL610037Result;
/**
 * 2014-7-24
 * @author xufan
 *融合宽带变更CRM支付流水状态接口
 */
public interface IBandUnionRwdService
{
	public DEL610037Result updateBand(String region,String taskoid,String servtype) throws LIException;

}