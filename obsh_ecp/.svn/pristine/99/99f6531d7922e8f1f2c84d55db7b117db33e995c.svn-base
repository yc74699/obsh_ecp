package com.xwtech.xwecp.test;

import java.util.Properties;
import org.apache.log4j.Logger;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryLanOrderInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryLanOrderInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.LanOrderInfo;

public class QRY050075Test
{
	private static final Logger logger = Logger.getLogger(QRY050075Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
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
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13813382424");
		ic.addContextParameter("ddr_city", "13813382424");
		ic.addContextParameter("user_id", "1419200008195116");  //13813382424-1419200008195116,13913032424-1419200008195160
		lic.setContextParameter(ic);
		IQueryLanOrderInfoService co = new QueryLanOrderInfoServiceClientImpl();
		LanOrderInfo re2 = co.queryLanOrderInfo("0");
		
		if (re2 != null)
		{
			logger.info(" === re.getResultCode() === " + re2.getResultCode());
			logger.info(" === re.getErrorCode() === " + re2.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re2.getErrorMessage());
		}
	}
}
