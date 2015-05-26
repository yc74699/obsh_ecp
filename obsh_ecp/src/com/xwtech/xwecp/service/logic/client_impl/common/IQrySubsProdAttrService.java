package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY040085Result;

/**
 * 剩余流量转增关系查询，转增产品查询
 * @author 陶刚
 *
 */
public interface IQrySubsProdAttrService {
	public QRY040085Result qrySubsProdAttr(String phoneNum,String prodId) throws LIException;
}
