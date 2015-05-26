package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.sms.IMgrZoneMScoreService;
import com.xwtech.xwecp.service.logic.client_impl.sms.impl.MgrZoneMScoreServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL020005Result;
import com.xwtech.xwecp.service.logic.pojo.ZoneMValueInfo;

public class DEL020005Test 
{
	private static final Logger logger = Logger.getLogger(DEL020005Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
//		props.put("platform.url", "http://10.32.229.81:10000/sms_ecp/xwecp.do");
		props.put("platform.url", "http://127.0.0.1:8080/sms_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13805157824");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "15996300522");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "15996300522");
		ic.addContextParameter("ddr_city", "14");
		
//		ic.addContextParameter("user_id", "1419200009683023");
		
		lic.setContextParameter(ic);
		
		IMgrZoneMScoreService service = new MgrZoneMScoreServiceClientImpl();
		DEL020005Result re = service.mgrZoneMScore("15996300522","15050547190","40","888639","1");
		
		logger.info(" ====== getResultCode ======" + re.getResultCode());
		logger.info(" ====== getErrorCode ======" + re.getErrorCode());
		logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
		
		if(re.getZoneMValueInfo() != null)
		{
			for(ZoneMValueInfo ysd : re.getZoneMValueInfo())
			{
				logger.info(" ====== totalMValue ======" +ysd.getTotalMValue());
				logger.info(" ====== redeemAmount ======" + ysd.getRedeemAmount());
				logger.info(" ====== usedMvalue ======" + ysd.getUsedMvalue());
				logger.info(" ====== randowPwd ======" + ysd.getRandowPwd());	
			}
		}
	}
}
