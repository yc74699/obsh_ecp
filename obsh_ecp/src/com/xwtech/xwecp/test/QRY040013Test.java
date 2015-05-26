package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryGroupNumberService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryGroupNumberServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040013Result;

public class QRY040013Test
{
	private static final Logger logger = Logger.getLogger(QRY040013Test.class);
	
	public static void main(String[] args) throws Exception
	{
		
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		//props.put("platform.url", "http://127.0.0.1:8080/sms_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.65.223/obsh_ecp/xwecp.do");
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
		lic.setUserCity("14");
		lic.setUserMobile("13913814503");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913814503");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		lic.setContextParameter(ic);
		
		IQueryGroupNumberService service = new QueryGroupNumberServiceClientImpl();
		//查短号
		QRY040013Result result = service.queryGroupNumber("13913814503", "13400074343");
	
		logger.info(" ====== 开始返回参数 ======");
		if (result != null)
		{
			
			logger.info(" ====== 结果码 ======" + result.getResultCode());
			logger.info(" ======  ======" + result.getLongNum());
			logger.info(" ======  ======" + result.getShortNum());
			
		}
	}
}
