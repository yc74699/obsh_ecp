package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

/**
 * 国内一卡多号主号退订副号（只支持虚拟副号）
 * @author Administrator
 *
 */
public class DEL050006Result extends BaseServiceInvocationResult
{
	/**
	 * 错误代码
	 */
	private Long ret_code;
	
	/**
	 * 错误描述
	 */
	private Long ret_msg;

	public long getRet_code()
	{
		return ret_code;
	}

	public void setRet_code(long retCode)
	{
		ret_code = retCode;
	}

	public long getRet_msg()
	{
		return ret_msg;
	}

	public void setRet_msg(long retMsg)
	{
		ret_msg = retMsg;
	}
	
	
}
