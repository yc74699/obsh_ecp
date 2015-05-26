package com.xwtech.xwecp.teletext.funcs;


import org.apache.log4j.Logger;

import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;

public class BusinessCode2BossCodeFunctionExecutor extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger
			.getLogger(BusinessCode2BossCodeFunctionExecutor.class);
	
	private WellFormedDAO wellFormedDAO;
	
	public WellFormedDAO getWellFormedDAO()
	{
		return wellFormedDAO;
	}
	
	public void setWellFormedDAO(WellFormedDAO wellFormedDAO)
	{
		this.wellFormedDAO = wellFormedDAO;
	}
	
	public BusinessCode2BossCodeFunctionExecutor()
	{
		super("to_boss_code");
	}
	
	public String execute(String parameter)
	{				
		try
		{
			if (!"".equals(parameter))
			{
				BossParmDT dt = this.wellFormedDAO.getBossParm(parameter);
				return dt == null ? parameter : dt.getParm1();
			}
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		
		return "";
	}
}
