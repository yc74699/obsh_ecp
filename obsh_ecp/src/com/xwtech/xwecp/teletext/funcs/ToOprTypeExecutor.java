package com.xwtech.xwecp.teletext.funcs;

import org.apache.log4j.Logger;

/**
 * 切换操作类型
 * @author yuantao
 * 2010-01-18
 */
public class ToOprTypeExecutor extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger.getLogger(ToOprTypeExecutor.class);
	
	public ToOprTypeExecutor ()
	{
		super("toOprType");
	}
	
	public String execute(String parameter)
	{
		String str = "";
		
		if (null != parameter && !"".equals(parameter))
		{
			//开通
			if ("1".equals(parameter))
			{
				str = "7";
			}  //关闭
			else if ("2".equals(parameter))
			{
				str = "8";
			}
		}
		
		return str;
	}
}
