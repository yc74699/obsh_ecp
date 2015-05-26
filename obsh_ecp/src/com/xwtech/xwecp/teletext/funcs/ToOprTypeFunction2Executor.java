package com.xwtech.xwecp.teletext.funcs;

import org.apache.log4j.Logger;

public class ToOprTypeFunction2Executor extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger
	              .getLogger(ToOprTypeFunction2Executor.class);
	
		
	public ToOprTypeFunction2Executor()
	{
		super("to_opr_type2");
	}
	
	public String execute(String parameter)
	{
		String oprType = "";
		if ("1".equals(parameter))
		{
			oprType = "01";
		}
		else if ("2".equals(parameter))
		{
			oprType = "02";
		}
		return oprType;
	}
}
