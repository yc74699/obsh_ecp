package com.xwtech.xwecp.teletext.funcs;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.dao.WellFormedDAO;

/**
 * 系统地市编码转boss地市编码
 * @author 吴宗德
 *
 */
public class ToBossCityExecutor extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger.getLogger(ToBossCityExecutor.class);
	
	private WellFormedDAO wellFormedDAO;
	
	public WellFormedDAO getWellFormedDAO()
	{
		return wellFormedDAO;
	}
	
	public void setWellFormedDAO(WellFormedDAO wellFormedDAO)
	{
		this.wellFormedDAO = wellFormedDAO;
	}
	
	public ToBossCityExecutor()
	{
		super("to_boss_city");
	}
	
	public String execute(String parameter)
	{
		String strRet = "";
		
		try
		{
			if (!"".equals(parameter))
			{
				strRet = this.wellFormedDAO.getBossCity(parameter);
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
