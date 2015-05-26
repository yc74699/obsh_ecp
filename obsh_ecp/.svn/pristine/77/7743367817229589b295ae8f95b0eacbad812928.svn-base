package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryBugGetStrService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryCardTypeService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryBugGetStrServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryCardTypeServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040067Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040068Result;

public class QRY040068Test
{
	private static final Logger logger = Logger.getLogger(QRY040068Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel"); 
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.103:10004/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("18762020592");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "18762020592");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		//ic.addContextParameter("user_id", "1738200000581927");  //13813382424-1419200008195116,13913032424-1419200008195160
		lic.setContextParameter(ic);
		
		IQueryCardTypeService co = new QueryCardTypeServiceClientImpl();
		QRY040068Result re = co.queryCardType("18762020592");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
			logger.info(" === getResTypeId() === " + re.getResTypeId());
		}
	}
}
