package com.xwtech.xwecp.teletext.funcs;

import org.apache.log4j.Logger;

public class ChooseFlagFunctionExecutor extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger.getLogger(ChooseFlagFunctionExecutor.class);
	
	public ChooseFlagFunctionExecutor()
	{
		super("choose_flag");
	}

	public String execute(String parameter)
	{
		String chooseFlag = "";
		
		if ("1".equals(parameter))
		{
			chooseFlag = "0";
		}
		else if ("3".equals(parameter))
		{
			chooseFlag = "1";
		}
		return chooseFlag;
	}
}
