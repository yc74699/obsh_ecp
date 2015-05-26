package com.xwtech.xwecp.service.server.funcs;


import org.apache.log4j.Logger;

import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.teletext.funcs.AbstractFunctionExecutor;


public class BusinessCode2BusinessTypeExecutor extends AbstractFunctionExecutor 
{
	private static final Logger logger = Logger
			.getLogger(BusinessCode2BusinessTypeExecutor.class);

	private WellFormedDAO wellFormedDAO;
	
	public WellFormedDAO getWellFormedDAO()
	{
		return wellFormedDAO;
	}
	
	public void setWellFormedDAO(WellFormedDAO wellFormedDAO)
	{
		this.wellFormedDAO = wellFormedDAO;
	}
	
	public BusinessCode2BusinessTypeExecutor()
	{
		super("qry_business_typeof");
	}
	
	public String execute(String parameter)
	{
		String busiType = "";
		
		try
		{
			busiType = this.wellFormedDAO.getBusiParm(parameter, "QRY020001", "");
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		//logger.info(" ====== busiType ====== " + busiType);
		return busiType;
	}
}
