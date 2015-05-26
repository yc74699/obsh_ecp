package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryUserBirthday;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryUserBirthdayClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040043Result;

public class QRY040043Test
{
	private static final Logger logger = Logger.getLogger(QRY040043Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
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
		lic.setUserMobile("13701542424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13701542424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13701542424");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "1401200007185596");  //2056200011182291
		
		lic.setContextParameter(ic);
		
		IQueryUserBirthday service = new QueryUserBirthdayClientImpl();
		
		QRY040043Result result = service.queryUserBirthday("13701542424");
		
		//查长号
		//result = service.queryGroupNumber("13912986834", "68556", "1");
		
		logger.info(" ====== 开始返回参数 ======");
		if (result != null)
		{
			System.out.println("shortNumber: " + result.getBossCode());
			System.out.println("longNumber: " + result.getBirthday());
			System.out.println("longNumber: " + result.getBirthType());
		}
	}
}
