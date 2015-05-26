package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY610001Result;


public interface IQryVirtualFamilyService
{
	/**
	 * 用户查询归属V网信息
	 * @param subsid 用户编号
	 * @param pkgprodid 增值产品包编码
	 * @return
	 * @throws LIException
	 */
	public QRY610001Result qryVirtualFamily(String subsid,String pkgprodid) throws LIException;
}
