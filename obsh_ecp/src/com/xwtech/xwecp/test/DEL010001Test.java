package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.ITransactBusinessService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.TransactBusinessServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL010001Result;

public class DEL010001Test
{
	private static final Logger logger = Logger.getLogger(DEL010001Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://10.32.65.67:8080/obsh_ecp_test/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8080/wap_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.81:10008/sms_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.167:10004/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.86:8080/openapi_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13813382424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1419200008195116");  //13813382424-1419200008195116,13913032424-1419200008195160
		lic.setContextParameter(ic);
		ITransactBusinessService co = new TransactBusinessServiceClientImpl();
		//动感地带 13401312424  brand_id：11 city_id：17 user_id：1738200005062065
		//全球通 13913032424 user_id：1419200008195160
		//1、开通 2、关闭 3、变更 \
		//1、立即 2、次日 3、次月
		DEL010001Result re = co.transactBusiness("13813382424", "WLANX_20Y",1, 1, "", "", "", "");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());

			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
		}
	}
}
