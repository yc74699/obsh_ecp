package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryPhoneNumBySimService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryPhoneNumBySimServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040009Result;

public class QRY040009Test {
private static final Logger logger = Logger.getLogger(QRY040009Test.class);
	
	public static void main(String[] args) throws Exception
	{
	//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/xwecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "20");
		ic.addContextParameter("ddr_city", "20");
		
		
		lic.setContextParameter(ic);
		
		IQueryPhoneNumBySimService service = new QueryPhoneNumBySimServiceClientImpl();
		
	                          
	    QRY040009Result result = service.queryPhoneNumBySim("89860064100750068329" , "EEE50BF544BCD3EE", "NTDQ");
	    System.out.println(result.getPhoneNum());
		
	}
	
	
}
