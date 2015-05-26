package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryThreeYearsAgoScoreService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryThreeYearsAgoScoreServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY030009Result;

public class QRY030009Test
{
	private static final Logger logger = Logger.getLogger(QRY030009Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段  
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13952876895");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13952876895");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "18");
		ic.addContextParameter("ddr_city", "18");
//		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		ic.addContextParameter("user_id", "1843200002924570");
		
		
		lic.setContextParameter(ic);
		
		IQueryThreeYearsAgoScoreService co = new QueryThreeYearsAgoScoreServiceClientImpl();
		QRY030009Result re = co.queryThreeYearsAgoScore("13952876895", "0");  //13913032424   15195760728
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== Availscore ======" + re.getAvailscore());
		}
	}
}
