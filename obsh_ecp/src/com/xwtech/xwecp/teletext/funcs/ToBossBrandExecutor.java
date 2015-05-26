package com.xwtech.xwecp.teletext.funcs;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.dao.WellFormedDAO;

/**
 * 系统品牌编码转boss品牌编码
 * @author 吴宗德
 *
 */
public class ToBossBrandExecutor extends AbstractFunctionExecutor
{
	private static final Logger logger = Logger.getLogger(ToBossBrandExecutor.class);
	
	private WellFormedDAO wellFormedDAO;
	
	public WellFormedDAO getWellFormedDAO()
	{
		return wellFormedDAO;
	}
	
	public void setWellFormedDAO(WellFormedDAO wellFormedDAO)
	{
		this.wellFormedDAO = wellFormedDAO;
	}
	
	public ToBossBrandExecutor()
	{
		super("to_boss_brand");
	}
	
	public String execute(String parameter)
	{
		String strRet = "";
		
		try
		{
			if (!"".equals(parameter))
			{
				strRet = this.wellFormedDAO.getBossBrand(parameter);
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
