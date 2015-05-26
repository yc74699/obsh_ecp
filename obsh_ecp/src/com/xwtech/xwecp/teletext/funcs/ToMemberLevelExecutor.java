package com.xwtech.xwecp.teletext.funcs;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

/**
 * 转换会员级别
 * @author yuantao
 *
 */
public class ToMemberLevelExecutor extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger
    .getLogger(ToMemberLevelExecutor.class);
	
	public ToMemberLevelExecutor()
	{
		super("to_member_level");
	}
	
	public String execute(String parameter)
	{
		String lev = "1";
		String [] level = {"100", "102", "104", "106", "111"};
		
		try
		{
			return ArrayUtils.contains(level, parameter)?"1":"2";
		}
		catch (Exception e)
		{
			logger.error(e, e);
			lev = "1";
		}
		
		return lev;
	}
}
