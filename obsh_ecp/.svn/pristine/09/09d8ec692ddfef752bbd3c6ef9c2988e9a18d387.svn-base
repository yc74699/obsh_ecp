package com.xwtech.xwecp.teletext.funcs;

import org.apache.log4j.Logger;

/**
 * 业务赠送 转换oprType操作类型
 * @author yuantao
 * 2010-01-26
 */
public class ChangeOprTypeExecutor extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger.getLogger(ToOprTypeExecutor.class);
	
	public ChangeOprTypeExecutor ()
	{
		super("changeOprType");
	}
	
	public String execute(String parameter)
	{
		String str = "";
		
		try
		{
			if (null != parameter && !"".equals(parameter))
			{
				if ("0".equals(parameter))  //取消赠送
				{
					str = "02";
				}
				else if ("1".equals(parameter))  //赠送
				{
					str = "01";
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		return str;
	}
}
