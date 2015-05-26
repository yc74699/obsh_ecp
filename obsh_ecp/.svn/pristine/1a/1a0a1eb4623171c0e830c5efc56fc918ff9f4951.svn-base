package com.xwtech.xwecp.msg;


import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 这个类是放在data里的, 表示接口调用上下文环境, 这个上下文环境最终会被作为输入参数合并为BOSS真正的报文, 但是, 这些参数会被以
 * context_作为前缀以防止和输入参数重名
 * 
 * */
public class InvocationContext
{
	private static final Logger logger = Logger.getLogger(InvocationContext.class);
	
	protected Map<String, Object> contextParam = new HashMap<String, Object>();
	
	public void addContextParameter(String name, Object obj)
	{
		this.contextParam.put(name, obj);
	}
	
	public void removeContextParameter(String name)
	{
		this.contextParam.remove(name);
	}
	
	public Map<String, Object> toContextParameter()
	{
		return this.contextParam;
	}
	public String getContextParameter(String name)
	{
		return (String)this.contextParam.get(name);
	}

	public Map<String, Object> getContextParam() {
		return contextParam;
	}

	public void setContextParam(Map<String, Object> contextParam) {
		this.contextParam = contextParam;
	}

//	public static Logger getLogger() {
//		return logger;
//	}
	
}
