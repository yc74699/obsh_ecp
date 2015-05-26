package com.xwtech.xwecp.service.logic.resolver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;
import com.xwtech.xwecp.service.logic.pojo.Monternet;
import com.xwtech.xwecp.service.logic.pojo.QRY050002Result;

public class QrySpuserRegMoResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(QrySpuserRegMoResolver.class);
	
	private WellFormedDAO wellFormedDAO;
	
	public QrySpuserRegMoResolver()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		List<Monternet> list = null;
		List<BossParmDT> bList = null;
		String bizId = "";
		
		try
		{
			QRY050002Result ret = (QRY050002Result)result;
			list = ret.getMonternet();
			
			if (null != param && param.size() > 0)
			{
				for (RequestParameter p : param)
				{
					if (p.getParameterName().equals("bizId"))
					{
						bizId = String.valueOf(p.getParameterValue());
					}
				}
			}
			
			if (null != list && list.size() > 0)
			{
				if (null != bizId && !"".equals(bizId))
				{
					bList = (List)this.wellFormedDAO.getSubBossParmList(bizId);
				}
				
				if (null != bList && bList.size() > 0)
				{
					for (BossParmDT bDt : bList)
					{
						for (Monternet mDt : list)
						{
							if (bDt.getParm2().equals(mDt.getBizCode()) && bDt.getParm1().equals(mDt.getSpId()))
							{
								mDt.setBizCode(bDt.getBusiNum());
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }
}
