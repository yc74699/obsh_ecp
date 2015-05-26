package com.xwtech.xwecp.teletext.funcs;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;

public class BusinessCode2BossCode3FunctionExecutor extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger
    .getLogger(BusinessCode2BossCode3FunctionExecutor.class);
	
	private WellFormedDAO wellFormedDAO;

	public WellFormedDAO getWellFormedDAO()
	{
		return wellFormedDAO;
	}

	public void setWellFormedDAO(WellFormedDAO wellFormedDAO)
	{
		this.wellFormedDAO = wellFormedDAO;
	}

	public BusinessCode2BossCode3FunctionExecutor()
	{
		super("to_boss_code3");
	}

	/**
	* 获取业务对应码3
	*/
	public String execute(String parameter)
	{
		try
		{
			if (!"".equals(parameter))
			{
				BossParmDT dt = this.wellFormedDAO.getBossParm(parameter);
				return dt.getParm3();
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}

		return "";
	}
}
