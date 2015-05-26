package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IInternetPwdResetService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.InternetPwdResetServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL030004Result;

public class DEL030004Test {
	private static final Logger logger = Logger.getLogger(DEL030004Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.100:10004/openapi_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.167:10004/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.103:10004/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.92:10008/sms_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("");
		lic.setUserCity("14");
		lic.setUserMobile("13701542424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13701542424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13701542424");
		lic.setContextParameter(ic);
		
		IInternetPwdResetService co = new InternetPwdResetServiceClientImpl();
		//838411
		DEL030004Result re = co.modifyAndResetMbandPwd("13701542424","111111","222222","1");
		logger.info(" ====== 开始返回参数 ======"+re);
		if (re != null)
		{
			 logger.info(" ====== getResultCode ======" + re.getResultCode());
			 logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
			 logger.info(" ====== getRandompwd ======" + re.getRandompwd());
			
		}
	}
}
