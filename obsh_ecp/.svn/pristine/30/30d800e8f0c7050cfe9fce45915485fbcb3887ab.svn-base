package com.xwtech.xwecp.service;


import java.util.List;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.config.ServiceConfig;


public interface ILogicalService
{
	/** 新大陆boss成功编码 */
	String BOSS_SUCCESS = "0000";
	
	/** 逻辑接口成功编码 */
	String LOGIC_SUCESS = "0";
	
	/** 逻辑接口失败编码 */
	String LOGIC_ERROR = "1";
	
	/** 逻辑接口返回提示 */
	String LOGIC_INFO = "2";
	
	/** 逻辑接口异常 */
	String LOGIC_EXCEPTION = "-99";
	
	/** 逻辑接口参数错误 */
	String LOGIC_PARAM_ERROR = "-100";
	
	/**
	 * 
	 * @param accessId 请求seq 记日志用
	 * @param config   逻辑接口配置
	 * @param params  参数
	 * @return
	 */
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params);
}
