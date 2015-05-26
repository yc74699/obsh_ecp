package com.xwtech.xwecp.teletext.funcs;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.dao.WellFormedDAO;

/**
 * 县市编码转组织机构ID编码
 * @author 杨光
 *
 */
public class ToOrgIdExecutor extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger.getLogger(ToOrgIdExecutor.class);
	
	private WellFormedDAO wellFormedDAO;
	
	public WellFormedDAO getWellFormedDAO()
	{
		return wellFormedDAO;
	}
	
	public void setWellFormedDAO(WellFormedDAO wellFormedDAO)
	{
		this.wellFormedDAO = wellFormedDAO;
	}
	
	public ToOrgIdExecutor()
	{
		super("to_boss_orgid");
	}
	
	public String execute(String parameter)
	{
		String strRet = "";
		
		try
		{
			if (!"".equals(parameter))
			{
				strRet = this.wellFormedDAO.getBossOrgid(parameter);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			strRet = "";
		}
		
		return strRet;
	}
}
