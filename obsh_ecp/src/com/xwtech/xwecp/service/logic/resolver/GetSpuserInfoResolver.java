package com.xwtech.xwecp.service.logic.resolver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;

public class GetSpuserInfoResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(GetSpuserInfoResolver.class);
	
	private WellFormedDAO wellFormedDAO;
	
	public GetSpuserInfoResolver()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		GommonBusiness dt = null;
		List<GommonBusiness> list = null;
		List<GommonBusiness> reList = new ArrayList();
		List<BossParmDT> parList = null;
		String bizId = "";
		
		try
		{
			QRY020001Result ret = (QRY020001Result)result;
			list = ret.getGommonBusiness();
			
			if (null != param && param.size() > 0)
			{
				for (RequestParameter p : param)
				{
					if (p.getParameterName().equals("bizId"))
					{
						bizId = String.valueOf(p.getParameterValue());
						if (!"".equals(bizId))
						{
							parList = (List)this.wellFormedDAO.getSubBossParmList(bizId);
						}
					}
				}
			}
			
			if (null != list && list.size() > 0)
			{
				if (null != parList && parList.size() > 0)
				{
					for (BossParmDT b : parList)
					{
						boolean flag = true;
						for (GommonBusiness g : list)
						{
							if (b.getParm1().equals(g.getReserve1()) 
								       && b.getParm2().equals(g.getName()) 
								       && checkShortCode(b.getParm3(),g.getReserve2()))
							{
								flag = false;
								g.setId(b.getBusiNum());
								
								if (null != g.getBeginDate() && !"".equals(g.getBeginDate()))
								{
									if (g.getBeginDate().equals(getFirstdayOfNextMonth()) || g.getBeginDate().equals(getNextDayOfMonth())) {
										g.setState(3);
									}else{
										g.setState(2);
									}
									
								}
								reList.add(g);
							}
						}
						
						if (flag)
						{
							dt = new GommonBusiness();
							dt.setId(b.getBusiNum());
							dt.setState(1);
							reList.add(dt);
						}
					}
				}
			}
			else
			{
				if (null != parList)
				{
					for (BossParmDT bDT : parList)
					{
						dt = new GommonBusiness();
						dt.setId(bDT.getBusiNum());
						dt.setState(1);
						reList.add(dt);
					}
				}
				else
				{
					dt = new GommonBusiness();
					dt.setId(bizId);
					dt.setState(1);
					reList.add(dt);
				}
			}
			
			for (GommonBusiness busi : reList)
			{
				if (null != busi.getEndDate() && !"".equals(busi.getEndDate()))
				{
					busi.setState(4);
				}
			}
			
			ret.setGommonBusiness(reList);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }
	
	/**
	 * taogang
	 * @param code
	 * @param sCode
	 * @return
	 */
	private boolean checkShortCode(String code,String sCode)
	{
		String tmpCode = "";
		if(code.substring(0, 4).equals("2300"))
		{
			tmpCode = code.substring(4);
		}
		if((!"".equals(tmpCode) && tmpCode.equals(sCode)) || code.equals(sCode))
		{
			return true;
		}
		return false;
	}
	
	/*public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		GommonBusiness dt = null;
		List list = null;
		List<GommonBusiness> reList = null;
		List<BossParmDT> parList = null;
		BossParmDT bossDT = null;
		RequestParameter reqDT = null;
		
		try
		{
			QRY020001Result ret = (QRY020001Result)result;
			list = ret.getGommonBusiness();
			if (null != list && list.size() > 0)
			{
				if (null != param && param.size() > 0)
				{
					for (int i = 0; i < param.size(); i++)
					{
						reqDT = (RequestParameter)param.get(i);
						if (reqDT.getParameterName().equals("bizId"))
						{
							//parList = (List)this.wellFormedDAO.getBossParmList(String.valueOf(reqDT.getParameterValue()));
							parList = (List)this.wellFormedDAO.getSubBossParmList(String.valueOf(reqDT.getParameterValue()));
						}
					}
				}
				
				if (null != parList && parList.size() > 0)
				{
					reList = new ArrayList();
					for (int i = 0; i < parList.size(); i++)
					{
						bossDT = (BossParmDT)parList.get(i);
						for (int j = 0; j < list.size(); j++)
						{
							dt = (GommonBusiness)list.get(j);
							if (bossDT.getParm1().equals(dt.getReserve1()) 
							       && bossDT.getParm2().equals(dt.getName()) 
							       && bossDT.getParm3().equals(dt.getReserve2()))
							{
								dt.setId(bossDT.getBusiNum());
								reList.add(dt);
							}
						}
					}
					
					if (reList.size() != parList.size())
					{
						for (BossParmDT bDT : parList)
						{
							boolean flag = true;
							for (GommonBusiness gDT : reList)
							{
								if (bDT.getParm1().equals(gDT.getReserve1())
										&& bDT.getParm2().equals(gDT.getName())
										&& bDT.getParm3().equals(gDT.getReserve2()))
								{
									flag = false;
								}
							}
							if (flag)
							{
								dt = new GommonBusiness();
								dt.setId(bDT.getBusiNum());
								dt.setState(1);
								reList.add(dt);
							}
						}
					}
					
					if (null == reList || reList.size() == 0)
					{
						reList = new ArrayList();
						dt = new GommonBusiness();
						dt.setState(1);
						reList.add(dt);
					}
				}
			}
			else
			{
				if (null != parList)
				{
					for (BossParmDT bDT : parList)
					{
						dt = new GommonBusiness();
						dt.setId(bDT.getBusiNum());
						dt.setState(1);
						reList.add(dt);
					}
				}
				reList = new ArrayList();
				dt = new GommonBusiness();
				dt.setState(1);
				reList.add(dt);
			}
			
			ret.setGommonBusiness(reList);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }*/
	private String getNextDayOfMonth() {
		String str = "";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		str = sf.format(cal.getTime());
		str += "000000";
		return str;
	}
	
	private String getFirstdayOfNextMonth() {
		String str = "";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		str = sf.format(cal.getTime());
		str += "000000";
		return str;
	}
}
