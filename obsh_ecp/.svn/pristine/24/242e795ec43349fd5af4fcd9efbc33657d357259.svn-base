package com.xwtech.xwecp.teletext.funcs;

import org.apache.log4j.Logger;

public class ChangeOprTypeFunctionTSBExecutor extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger
                        .getLogger(ChangeOprTypeFunctionTSBExecutor.class);
	
	public ChangeOprTypeFunctionTSBExecutor()
	{
		super("change_oprType");
	}

	public String execute(String parameter)
	{
		String oprType = parameter;
		if ("2".equals(parameter))
		{
			oprType = "0";
		}
		
		return oprType;
	}
}
