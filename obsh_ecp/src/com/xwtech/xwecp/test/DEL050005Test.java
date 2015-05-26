package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IRecRecommService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.RecRecommServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL050005Result;

/**
 *
 */
public class DEL050005Test
{
	private static final Logger logger = Logger.getLogger(DEL050005Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "data_channel");
		props.put("platform.url", "http://127.0.0.1/obsh_ecp/xwecp.do");
		props.put("platform.user", "lw");
		props.put("platform.password", "lw");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13813319034");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		
		
		lic.setContextParameter(ic);
		
		IRecRecommService service = new RecRecommServiceClientImpl();
		DEL050005Result result = service.recRecommService("13401802778", "43031126", "3","test");
		System.out.println("返回状态为:" + result.getResultCode());
		System.out.println("getRetcode:" + result.getRetcode());
		System.out.println("返回信息为:" + result.getRetmsg());
	}
}
