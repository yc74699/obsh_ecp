package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryFamilyVnetInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryFamilyVnetInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY090002Result;

public class QRY090002Test
{
   private static final Logger logger = Logger.getLogger(QRY090001Test.class);
	
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
		lic.setUserBrand("全球通");
		lic.setUserCity("14");
		lic.setUserMobile("13915974318");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13915974318");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("context_loginiplock_login_ip", "127.0.0.1");
		
		ic.addContextParameter("user_id", "1419200003463923");
		
		
		lic.setContextParameter(ic);

		
		IQueryFamilyVnetInfoService co = new QueryFamilyVnetInfoServiceClientImpl();
		QRY090002Result re = co.queryFamilyVnetInfo("13915974318", "1200000005");
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
		}
	}
}
