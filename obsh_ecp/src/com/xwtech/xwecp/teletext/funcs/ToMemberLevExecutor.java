package com.xwtech.xwecp.teletext.funcs;

import org.apache.log4j.Logger;

/**
 * 转换会员级别
 * @author yuantao
 *
 */
public class ToMemberLevExecutor extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger
    .getLogger(ToMemberLevExecutor.class);
	
	public ToMemberLevExecutor()
	{
		super("to_member_lev");
	}
	
	public String execute(String parameter)
	{
		String lev = "1";
		
		try
		{
			return "WXYYJLB_PTHY".equals(parameter)?"1":"2";
		}
		catch (Exception e)
		{
			logger.error(e, e);
			lev = "1";
		}
		
		return lev;
	}
}
