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
import com.xwtech.xwecp.service.logic.pojo.QRY020005Result;
import com.xwtech.xwecp.service.logic.pojo.UserXp;

/**
 * 体验业务信息查询
 * @author yuantao
 * 2010-01-28
 */
public class QryExperimentBusResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(QryExperimentBusResolver.class);
	
	private WellFormedDAO wellFormedDAO;
	
	public QryExperimentBusResolver()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, List<RequestParameter> param) throws Exception
	{
		QRY020005Result res = (QRY020005Result)result;
		List<BossParmDT> bList = null;
		List<UserXp> list = res.getUserXp();  //获取业务信息
		List<UserXp> reList = null;  //返回列表
		UserXp dt = null;  //业务信息
				
		try
		{
			//获取子业务信息  体验业务
			bList = this.wellFormedDAO.getSubBossParmList("TY");
			//存在子业务信息
			if (null != bList && bList.size() > 0)
			{
				reList = new ArrayList ();
				for (BossParmDT bDt : bList)
				{
					boolean oprBoolean = true;  //开通标识
					if (null != list && list.size() > 0)
					{
						for (UserXp xp : list)
						{
							if (bDt.getParm1().equals(xp.getDictCode()))  //对比字典代码
							{
								oprBoolean = false;  //已开通标识
								xp.setStatus("2");  //已开通
								xp.setDealCode(bDt.getBusiNum());
								reList.add(xp);
							}
						}
						if (oprBoolean)  //该业务未开通
						{
							dt = new UserXp ();
							dt.setDealCode(bDt.getBusiNum());
							dt.setStatus("1");  //未开通
							reList.add(dt);
						}
					}
					else  //未开通业务
					{
						dt = new UserXp ();
						dt.setDealCode(bDt.getBusiNum());
						dt.setStatus("1");  //未开通
						reList.add(dt);
					}
				}
			}
			//设置返回业务信息
			res.setUserXp(reList);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		
	}
}
