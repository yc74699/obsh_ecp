package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.UpdrecommService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.PdrecommServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL050002Result;

/**
 *
 */
public class DEL050002Test
{
	private static final Logger logger = Logger.getLogger(DEL050002Test.class);
	
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
		lic.setUserMobile("13601400067");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "18");
		
		
		lic.setContextParameter(ic);
		
		UpdrecommService service = new PdrecommServiceClientImpl();
		DEL050002Result result = service.updrecommService("15905290432", "963665321", "1","");
		System.out.println("返回状态为:" + result.getRetcode());
		System.out.println("返回信息为:" + result.getRetmsg());
	}
}
