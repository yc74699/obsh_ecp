package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryUserXpService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryUserXpServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY020005Result;
import com.xwtech.xwecp.service.logic.pojo.UserXp;

public class QRY020005Test
{
	private static final Logger logger = Logger.getLogger(QRY020005Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13601400067");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913032424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13913032424");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "1419200008195160");  //2056200011182291
		
		lic.setContextParameter(ic);
		
		IQueryUserXpService co = new QueryUserXpServiceClientImpl();
		//动感地带 13401312424  brand_id：11 city_id：17 user_id：1738200005062065
		//全球通 13913032424 user_id：1419200008195160
		QRY020005Result re = co.queryUserXp("13913032424", 1);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			if (null != re.getUserXp() && re.getUserXp().size() > 0)
			{
				logger.info(" ====== getUserXp.size ======" + re.getUserXp().size());
				for (UserXp g : re.getUserXp())
				{
					logger.info(" ====== g.getDealCode ======" + g.getDealCode());
					logger.info(" ====== g.getDictCode ======" + g.getDictCode());
					logger.info(" ====== g.getUserId ======" + g.getUserId());
					logger.info(" ====== g.getCardId ====== " + g.getCardId());
					logger.info(" ====== g.getUseDate ======" + g.getUseDate());
					logger.info(" ====== g.getEndDate ====== " + g.getEndDate());
					logger.info(" ====== g.getRemark ====== " + g.getRemark());
					logger.info(" =========================================== ");
				}
			}
		}
	}
}
