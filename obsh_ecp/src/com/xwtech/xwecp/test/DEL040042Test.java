package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.pojo.DEL040042Result;
import com.xwtech.xwecp.service.logic.client_impl.common.IOrderHomeBill;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.OrderHomeBillClientImpl;



public class DEL040042Test
{
	private static final Logger logger = Logger.getLogger(DEL040042Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
		props.put("platform.url", "http://127.0.0.1:8080/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("13");
		lic.setUserMobile("13951248700");  
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13951248700");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "13");
		ic.addContextParameter("ddr_city", "13");
		ic.addContextParameter("user_id", "1219200008921823");
		
		lic.setContextParameter(ic);
	
		
		
		IOrderHomeBill co = new OrderHomeBillClientImpl();
		DEL040042Result re = co.orderHomeBill("15951371555", "0", "0");
		
		
		
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== 结果码 ======" + re.getResultCode());
			logger.info(" ====== 错误信息 ======" + re.getErrorMessage());


		}
	}
}
