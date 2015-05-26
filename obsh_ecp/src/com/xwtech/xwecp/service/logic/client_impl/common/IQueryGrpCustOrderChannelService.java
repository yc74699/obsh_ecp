package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010035Result;
/**
 * 查询集团渠道权限开放情况
 * 
 * @author xufan
 * 2014-05-08
 */
public interface IQueryGrpCustOrderChannelService
{
	public QRY010035Result qryGrpCustOrderChannel(String custid,String qryChannel) throws LIException;

}