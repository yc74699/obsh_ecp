package com.xwtech.xwecp.teletext.funcs;

import org.apache.log4j.Logger;

public class ToOprTypeFunctionExecutor extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger
	              .getLogger(ToOprTypeFunctionExecutor.class);
	
		
	public ToOprTypeFunctionExecutor()
	{
		super("to_opr_type");
	}
	
	public String execute(String parameter)
	{
		String oprType = "";
		if ("1".equals(parameter))
		{
			oprType = "02";
		}
		else if ("2".equals(parameter))
		{
			oprType = "01";
		}
		return oprType;
	}
}
