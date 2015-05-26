package com.xwtech.xwecp.teletext.funcs;

import org.apache.log4j.Logger;

public class ChangeChooseFlagFunctionTSBExecutor extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger
                        .getLogger(ChangeChooseFlagFunctionTSBExecutor.class);
	
	public ChangeChooseFlagFunctionTSBExecutor()
	{
		super("change_oprType");
	}

	public String execute(String parameter)
	{
		String oprType = parameter;
		if ("1".equals(parameter)){
			oprType = "0";
		}else if("2".equals(parameter)){
			oprType = "1";
		}else if("3".equals(parameter)){
			oprType = "2";
		}
		return oprType;
	}
}
