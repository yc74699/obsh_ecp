package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;


import com.xwtech.xwecp.service.BaseServiceInvocationResult;
/**
 * 家庭V网短号套餐订购
 * @author Administrator
 *
 */
public class DEL610047Result extends BaseServiceInvocationResult implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String ret_code;//错误代码
	
	private String ret_msg;//错误描述

	public String getRet_code()
	{
		return ret_code;
	}

	public void setRet_code(String retCode)
	{
		ret_code = retCode;
	}

	public String getRet_msg()
	{
		return ret_msg;
	}

	public void setRet_msg(String retMsg)
	{
		ret_msg = retMsg;
	}
	
	
	
	
}
