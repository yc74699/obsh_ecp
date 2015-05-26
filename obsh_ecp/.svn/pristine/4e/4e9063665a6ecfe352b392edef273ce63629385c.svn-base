package com.xwtech.xwecp.teletext.funcs;

import org.apache.log4j.Logger;

public class ToOrderCode extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger
    .getLogger(ToOrderCode.class);
	
	public ToOrderCode()
	{
		super("to_order_code");
	}
	
	public String execute(String parameter)
	{
		String code = "11";
		
		try
		{
			if ("1".equals(parameter))
			{
				code = "11";
			}
			else if ("2".equals(parameter))
			{
				code = "12";
			}
			else
			{
				code = "13";
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			code = "11";
		}
		
		return code;
	}
}
