package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICheckPasswordService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CheckPasswordServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040003Result;

public class QRY040003Test
{
	private static final Logger logger = Logger.getLogger(QRY040003Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/xwecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13770472424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13770472424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13770472424");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "1738200005062065");  //2056200011182291
		
		lic.setContextParameter(ic);
		
		ICheckPasswordService checkPasswordService = new CheckPasswordServiceClientImpl();
		QRY040003Result result = checkPasswordService.checkPassword("13770472424", "100866");
		//动感地带 13401312424  brand_id：11 city_id：17 user_id：1738200005062065
		//全球通 13913032424 user_id：1419200008195160
		/*
		QRY040003Result result = checkPasswordService.checkPassword(phoneNum, password);
		logger.info(" ====== 开始返回参数 ======");
		 */
		if (result != null)
		{
			logger.info(" ====== getResultCode ======" + result.getResultCode());
			logger.info(" ====== getErrorCode ======" + result.getErrorCode());
			logger.info(" ====== getErrorMessage ======" + result.getErrorMessage());
		}
	}
}
