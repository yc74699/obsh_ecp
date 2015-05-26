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

public class FindPackageResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(FindPackageResolver.class);
	
	private WellFormedDAO wellFormedDAO;
	
	//（动感）非常假期 业务编码
	private static final String[] MZONE_HOLIDAY_CODES =
	{   "2125", "2132", "2126", "2143", "2131", "2130", "2129", "2123", "2114", "2120",
		"2134", "2139", "2135", "2124", "2115", "2136", "2117", "2122", "2121", "2128",
		"2141", "2140", "2119", "2118", "2127", "2142", "2138", "2137", "2133", "2116",
		"2101", "2102", "2103", "2104", "2105", "2106", "2107", "2108", "2109", "2110",
		"2111", "2112", "2113"	};
	
	public FindPackageResolver()
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
		List<BossParmDT> bList = null;
		RequestParameter reqDT = null;
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
							//bossDT = (BossParmDT)this.wellFormedDAO.getBossParm(bizId);
							bList = this.wellFormedDAO.getSubBossParmList(bizId);
							break;
						}
					}
				}
			}
			
			if (null != list && list.size() > 0)
			{
				if (null != bList && bList.size() > 0)
				{
					for (BossParmDT bDt : bList)
					{
						boolean close = true;
						for (GommonBusiness g : list)
						{
							g.setState(2);
							if(bDt.getBusiNum().equals("BLACKBERRY")){
								
								if(g.getReserve1().equals("2235") || g.getReserve1().equals("2236")){
									close = false;
									g.setId(bDt.getBusiNum());
									reList.add(g);
								}
								
							}
							/*********************add 苏州用户使用不同的业务编码规则 2012-2-21 by 1069 begin***************/
							if(bDt.getBusiNum().length() >=7 &&   bDt.getBusiNum().substring(0, 7).equals("SZ_LCYJ")) {
								String newBusiNum = bDt.getParm2().substring(6, 10);
								if (g.getReserve1().equals(newBusiNum))
								{
									close = false;
									g.setId(bDt.getBusiNum());
									reList.add(g);
								}
							}
							/*********************add 苏州用户使用不同的业务编码规则 2012-2-21 by 1069 end***************/
							else{
								if (g.getReserve1().equals(bDt.getParm2()))
								{
									close = false;
									g.setId(bDt.getBusiNum());
									reList.add(g);
								}
							}
						}
						if (close)
						{
							dt = new GommonBusiness();
							dt.setId(bDt.getBusiNum());
							dt.setState(1);
							reList.add(dt);
						}
					}
				}
				else
				{
					reList = list;
				}
			}
			else
			{
				if (null != bList && bList.size() > 0)
				{
					for (BossParmDT bDt : bList)
					{
						dt = new GommonBusiness();
						dt.setId(bDt.getBusiNum());
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
				if (null != busi.getBeginDate() && !"".equals(busi.getBeginDate()))
				{
					if (busi.getBeginDate().equals(getFirstdayOfNextMonth()) || busi.getBeginDate().equals(getNextDayOfMonth())) {
						busi.setState(3);
					}
					else if (null != busi.getEndDate() && !"".equals(busi.getEndDate()))
					{
						busi.setState(4);
					}
				}
			}
			
			ret.setGommonBusiness(reList);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }
	
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