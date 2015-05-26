package com.xwtech.xwecp.service.server.funcs;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.teletext.funcs.AbstractFunctionExecutor;

public class BusinessCode2DelBusinessType2Executor extends AbstractFunctionExecutor 
{
	private static final Logger logger = Logger
	               .getLogger(BusinessCode2DelBusinessType2Executor.class);
	
private WellFormedDAO wellFormedDAO;
	
	public WellFormedDAO getWellFormedDAO()
	{
		return wellFormedDAO;
	}
	
	public void setWellFormedDAO(WellFormedDAO wellFormedDAO)
	{
		this.wellFormedDAO = wellFormedDAO;
	}
	
	public BusinessCode2DelBusinessType2Executor()
	{
		super("del_business_typeof");
	}
	
	public String execute(String parameter)
	{
		String busiType = "";
		String[] parameters = parameter.split("\\|",2);
		try
		{
			busiType = this.wellFormedDAO.getBusiParm(parameters[0], "DEL010001", "")+"_"+parameters[1];
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		
		return busiType;
	}
}
