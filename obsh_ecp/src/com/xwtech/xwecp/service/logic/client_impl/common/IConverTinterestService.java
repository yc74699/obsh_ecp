package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040079Result;

/**
 * 新增余额利息转流量兑换功能
 * @author YangXQ
 * 2014-7-15
 */
public interface IConverTinterestService
{
	public DEL040079Result converTinterest(String userid, String productid,String convertnumber, 
										String convertuserid,String formnum) throws LIException;
}