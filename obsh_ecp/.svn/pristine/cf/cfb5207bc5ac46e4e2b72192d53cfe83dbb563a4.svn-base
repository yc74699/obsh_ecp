package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.ISMS2TimesSure;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.SMS2TimesSureClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL010013Result;

public class DEL010013Test
{
	private static final Logger logger = Logger.getLogger(DEL010013Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
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
		lic.setUserMobile("18305162083");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "18305162083");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
//		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
//		ic.addContextParameter("user_id", "");
		
		lic.setContextParameter(ic);
		
		ISMS2TimesSure service = new SMS2TimesSureClientImpl();

		String msisdn = "18305162083";
		String mmsSendCode = "100865033452";
		String replayContent = "12345";
		

		DEL010013Result result = service.sms2TimesSure(msisdn, "14", mmsSendCode,replayContent,"2");

		logger.info(" ====== 开始返回参数 ======");
		
		if (result != null)
		{
			logger.info(" ====== getResultCode ======" + result.getResultCode());
			logger.info(" ====== getErrorCode ======" + result.getErrorCode());
			logger.info("========getErrorMessage==="+ result.getErrorMessage());
		}
	}
}
