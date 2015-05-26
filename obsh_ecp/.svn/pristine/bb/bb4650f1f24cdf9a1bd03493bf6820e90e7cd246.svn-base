package com.xwtech.xwecp.service.logic.resolver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;
import com.xwtech.xwecp.util.StringUtil;

/**
 * 2011-12-01 查询当月短信累计量
 * @author chenxiaoming
 *
 */
public class FindCallSumResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(FindCallSumResolver.class);
	
	private static int MONTH_CONSANT = 0;
	private static int DAY_CONSANT = 1;

	public FindCallSumResolver()
	{
		
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		GommonBusiness dt = null;
		List<GommonBusiness> list = null;
		List<GommonBusiness> reList = new ArrayList();
		
		try
		{
			QRY020001Result ret = (QRY020001Result)result;
			list = ret.getGommonBusiness();
			if (null != list && list.size() > 0)
			{
				//取出当月的记录数
				getCurrentMonthRecord(list,reList);
			}
			ret.setGommonBusiness(reList);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }
	
	private List<GommonBusiness> getCurrentMonthRecord(List<GommonBusiness> list, List<GommonBusiness> reList) 
	{
		int preDay = 1;
		for(GommonBusiness business : list)
		{
			String statDate = business.getBeginDate();
			String month = getMonthOrDay4Date(statDate,MONTH_CONSANT);
			String day = getMonthOrDay4Date(statDate,DAY_CONSANT);
			int currentDay = Integer.parseInt(day);
			if(null != statDate && !"".equals(statDate))
			{
				if(!"".equals(month) && month.equals(String.valueOf(getcurrentMonthOrDay(MONTH_CONSANT))))
				{
					if(preDay < Integer.parseInt(day))
					{
						preDay = currentDay;
					}
				}
			}
		}
		
		for(GommonBusiness business : list)
		{
			String statDate = business.getBeginDate();
			String month = getMonthOrDay4Date(statDate,MONTH_CONSANT);
			String day = getMonthOrDay4Date(statDate,DAY_CONSANT);
			int currentDay = Integer.parseInt(day);
			if(null != statDate && !"".equals(statDate))
			{
				if(!"".equals(month) && month.equals(String.valueOf(getcurrentMonthOrDay(MONTH_CONSANT))))
				{
					if(preDay == currentDay)
					{
						business.setId(null);
						business.setReserve1(statDate.substring(0,8));
						reList.add(business);
						break;
					}
				}
			}
		}
		return reList;
	}

	private String getMonthOrDay4Date(String date,int isMonth)
	{
		String month = "";
		if(date.length() >= 8)
		{
			if(isMonth == MONTH_CONSANT)
			{
				return date.substring(4,6);
			}
			else
			{
				return date.substring(6,8);
			}
		}
		return month;
	}
	
	private int getcurrentMonthOrDay(int isMonth) 
	{
		String str = "";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		str = sf.format(cal.getTime());
		if(!StringUtil.isNull(str)){
			if(isMonth == MONTH_CONSANT)
			{
				return Integer.valueOf(str.substring(4,6)).intValue();
			}
			else
			{
				return Integer.valueOf(str.substring(6,8)).intValue();
			}
		}
		else return 2;
	}
}