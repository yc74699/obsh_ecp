package com.xwtech.xwecp.service.logic.resolver;

import java.util.ArrayList;
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

public class GetSrvInfoResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(GetSrvInfoResolver.class);
	
	private WellFormedDAO wellFormedDAO;
	
	public GetSrvInfoResolver()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
//		GommonBusiness dt = null;
//		String bizId = "";
		
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
							if (b.getParm1().equals(g.getId()))
							{
								flag = false;
								g.setId(b.getBusiNum());
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
			
//			if (null == list || list.size() == 0)
//			{
//				dt = new GommonBusiness();
//				dt.setId(bizId);
//				dt.setState(1);
//				list = new ArrayList();
//				list.add(dt);
//				ret.setGommonBusiness(list);
//			}
//			
			for (GommonBusiness busi : list)
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
	
	/*public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		GommonBusiness dt = null;
		
		try
		{
			QRY020001Result ret = (QRY020001Result)result;
			List list = ret.getGommonBusiness();
			
			if (null == list || list.size() == 0)
			{
				dt = new GommonBusiness();
				dt.setState(1);
				list = new ArrayList();
				list.add(dt);
				ret.setGommonBusiness(list);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }*/
}
