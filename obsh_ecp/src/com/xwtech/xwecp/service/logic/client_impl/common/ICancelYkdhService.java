package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL050006Result;

/**
 * 国内一卡多号主号退订副号（只支持虚拟副号）
 * @author Administrator
 *
 */
public interface ICancelYkdhService
{
	/**
	 * 
	 * @param telnum 主号号码
	 * @param serialNum 退订号码的类型
	 * @return QRY050006出参结果集
	 * @throws LIException
	 */
	public DEL050006Result cancelYkdh(String telnum,String serialNum) throws LIException;
}
