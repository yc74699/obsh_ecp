package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IOrderUserBirthDayPkgService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.OrderUserBirthDayPkgServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040048Result;




public class DEL040048Test
{
	private static final Logger logger = Logger.getLogger(DEL040048Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1:8080/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("11");
		lic.setUserMobile("13511601414");  
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13511601414");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "11");
		ic.addContextParameter("ddr_city", "11");
		ic.addContextParameter("user_id", "");
		
		lic.setContextParameter(ic);
		
		
		IOrderUserBirthDayPkgService co = new OrderUserBirthDayPkgServiceClientImpl();
		DEL040048Result re = co.orderUserBirthdayPkg("13511601414", "QQTSRTC", "1", "1");
		
		
		
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== 结果码 ======" + re.getResultCode());
			logger.info(" ====== 错误信息 ======" + re.getErrorMessage());


		}
	}
}
