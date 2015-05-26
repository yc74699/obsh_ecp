package com.xwtech.xwecp.teletext.funcs;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.teletext.IExternalFunctionExecutor;

public abstract class AbstractFunctionExecutor implements IExternalFunctionExecutor
{
	private static final Logger logger = Logger
			.getLogger(AbstractFunctionExecutor.class);

	protected String functionName;

	protected AbstractFunctionExecutor(String functionName)
	{
		this.functionName = functionName;
	}

	public String getFunctionName()
	{
		return functionName;
	}

	public void setFunctionName(String functionName)
	{
		this.functionName = functionName;
	}

}
