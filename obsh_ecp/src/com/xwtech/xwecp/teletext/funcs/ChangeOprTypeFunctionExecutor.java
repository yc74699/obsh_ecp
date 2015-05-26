package com.xwtech.xwecp.teletext.funcs;

import org.apache.log4j.Logger;

public class ChangeOprTypeFunctionExecutor extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger
                        .getLogger(ChangeOprTypeFunctionExecutor.class);
	
	public ChangeOprTypeFunctionExecutor()
	{
		super("change_oprType");
	}

	public String execute(String parameter)
	{
		String oprType = parameter;
		if ("2".equals(parameter))
		{
			oprType = "3";
		}
		else if ("3".equals(parameter))
		{
			oprType = "2";
		}
		
		return oprType;
	}
}
