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
import com.xwtech.xwecp.util.DateTimeUtil;

public class GetMusicDicResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(GetMusicDicResolver.class);

	private WellFormedDAO wellFormedDAO;

	public GetMusicDicResolver()
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
							bList = this.wellFormedDAO.getSubBossParmList(bizId);
							break;
						}
					}
				}
			}
			//把格式为2007-06-07  12:12:12改成格式为yyyyMMddHHmmss
			if (null != list && list.size() > 0)
			{
				for(GommonBusiness g : list)
				{
					g.setBeginDate(DateTimeUtil.formatDateToStr(g.getBeginDate()));
					g.setEndDate(DateTimeUtil.formatDateToStr(g.getEndDate()));
				}
			}
			if (null != list && list.size() > 0)
			{
				if (null != bList && bList.size() > 0)
				{
					for (BossParmDT bDt : bList)
					{
						boolean flag = true;
						for (GommonBusiness g : list)
						{
							if (bDt.getParm1().equals(g.getId()))
							{
								flag = false;
								g.setState(2);
								g.setId(bDt.getBusiNum());
								reList.add(g);
							}
						}
						
						if (flag)
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
						busi.setEndDate(DateTimeUtil.formatDateToStr(busi.getEndDate()));
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

	private String getNextDayOfMonth(){
		String str = "";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		str = sf.format(cal.getTime());
		str += "000000";
		return str;
	}

	private String getFirstdayOfNextMonth(){
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