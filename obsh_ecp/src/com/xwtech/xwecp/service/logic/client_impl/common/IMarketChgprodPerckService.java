package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040098Result;

/**
 * 营销案方案产品变更查询
 * @author wang.huan
 *
 */
public interface IMarketChgprodPerckService {
	/**
	 * ddr_city	1	long		地市路由
servnumber	1	string		手机号码
pkgProdID	?	string		产品包编码   传空 不操作
prodID	1	string		产品编码
operType	1	string		产品编码，目前只支持ADD

	 */
	public QRY040098Result qryMarketChgProdCk(String city, String phoneNum , String prodID, String operType) throws LIException;
}
