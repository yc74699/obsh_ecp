package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IGetNumResourceService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.GetNumResourceServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050015Result;

public class QRY050015Test {
	private static final Logger logger = Logger.getLogger(QRY050015Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
//		props.put("platform.url", "http://127.0.0.1/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.69:10004/mall_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.122.167:10004/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238:8081/sms_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("12");
		lic.setUserCity("14");
		lic.setUserMobile("15150556323");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15150556323");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "15005156863");
		
		
		lic.setContextParameter(ic);
		
		IGetNumResourceService co = new GetNumResourceServiceClientImpl();
		QRY050015Result re = co.getNumResource("NJDQ","NJ");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
			logger.info(" ====== size ======" + re.getNumResource().size());
			
		}
	}
}
