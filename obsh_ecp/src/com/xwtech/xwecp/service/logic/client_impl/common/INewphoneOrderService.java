package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException; 
import com.xwtech.xwecp.service.logic.pojo.DEL010023Result;

/**
 * 带附加属性产品（来话管家）订购
 * @author YangXQ
 * 2014-10-15
 */
public interface INewphoneOrderService
{
	public DEL010023Result newphoneOrder(String msisdn, String prodid, String attrid, String attrvalue,String busi_code) throws LIException;

}