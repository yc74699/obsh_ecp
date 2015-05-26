package com.xwtech.xwecp.teletext.funcs;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;

public class DelBusiParmFunctionExecutor extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger
	              .getLogger(DelBusiParmFunctionExecutor.class);
	
	private WellFormedDAO wellFormedDAO;
	
	public WellFormedDAO getWellFormedDAO()
	{
		return wellFormedDAO;
	}
	
	public void setWellFormedDAO(WellFormedDAO wellFormedDAO)
	{
		this.wellFormedDAO = wellFormedDAO;
	}
	
	public DelBusiParmFunctionExecutor()
	{
		super("to_del_busi_code");
	}
	
	public String execute(String parameter)
	{
		try
		{
			if (!"".equals(parameter))
			{
				BossParmDT dt = this.wellFormedDAO.getBossParm(parameter);
				//this.wellFormedDAO.getBusiParm(f_business_num, f_li_number, "");
				return dt.getParm1();
			}
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		
		return "";
	}
}
