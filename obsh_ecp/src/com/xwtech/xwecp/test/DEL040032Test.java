package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IChgPkgForPro;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ChgPkgForProClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040032Result;

public class DEL040032Test
{
	private static final Logger logger = Logger.getLogger(DEL040032Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "sms_channel"); 
		//props.put("platform.url", "http://127.0.0.1:8080/sms_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.65.238:8081/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13814812424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13814812424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "13814812424");
		ic.addContextParameter("ddr_city", "11");
		ic.addContextParameter("user_id", "1101200007185596");  //13813382424-1419200008195116,13913032424-1419200008195160
		lic.setContextParameter(ic);
		
		IChgPkgForPro co = new ChgPkgForProClientImpl();
		String newCode = "1149";
		int inureMode = 3;
		DEL040032Result re = co.chgPkgForPro("13814812424", newCode,inureMode);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
		}
	}
}
