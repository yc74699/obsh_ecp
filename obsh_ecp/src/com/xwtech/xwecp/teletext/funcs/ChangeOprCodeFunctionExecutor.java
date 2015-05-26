package com.xwtech.xwecp.teletext.funcs;

import org.apache.log4j.Logger;

public class ChangeOprCodeFunctionExecutor extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger.getLogger(ChangeOprCodeFunctionExecutor.class);
	
	public ChangeOprCodeFunctionExecutor()
	{
		super("change_oprCode");
	}

	public String execute(String parameter)
	{
		String oprCode = parameter;
		oprCode = "0" + parameter;
		if ("3".equals(parameter))
		{
			oprCode = "10";
		}
		return oprCode;
	}
}
