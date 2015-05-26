package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQryExperCfgService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QryExperCfgServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.ExperCfg;
import com.xwtech.xwecp.service.logic.pojo.QRY050005Result;

public class QRY050005Test
{
	private static final Logger logger = Logger.getLogger(QRY050005Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1:8080/xwecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
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
		
		IQryExperCfgService co = new QryExperCfgServiceClientImpl();
		//动感地带 13401312424  brand_id：11 city_id：17 user_id：1738200005062065
		//全球通 13913032424 user_id：1419200008195160
		QRY050005Result re = co.qryExperCfg("13913032424", "14", 1);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			if (null != re.getExperCfg() && re.getExperCfg().size() > 0)
			{
				logger.info(" ====== getExperCfg.size ======" + re.getExperCfg().size());
				for (ExperCfg g : re.getExperCfg())
				{
					logger.info(" ====== g.getDealCode ======" + g.getDealCode());
					logger.info(" ====== g.getDictCode ======" + g.getDictCode());
					logger.info(" ====== g.getEalType ======" + g.getEalType());
					logger.info(" ====== g.getReserve1 ====== " + g.getReserve1());
					logger.info(" ====== g.getReserve2 ======" + g.getReserve2());
					logger.info(" =========================================== ");
				}
			}
		}
	}
}
