package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IABCPayService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ABCPayServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040036Result;

public class DEL040036Test
{
	private static final Logger logger = Logger.getLogger(DEL040036Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel"); 
		props.put("platform.url", "http://127.0.0.1/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8081/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13861591353");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13861591353");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13861591353");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("user_id", "1210200010573100");  //13813382424-1419200008195116,13913032424-1419200008195160
		lic.setContextParameter(ic);
		
		IABCPayService co = new ABCPayServiceClientImpl();
		DEL040036Result re = co.bankABCPay("13861591353", "1","1");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
		}
	}
}
