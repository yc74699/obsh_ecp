package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IvrNetBookService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.VrNetBookServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040024Result;

/**
 *
 */
public class DEL040024Test
{
	private static final Logger logger = Logger.getLogger(DEL040024Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13601400067");
		InvocationContext ic = new InvocationContext();
		lic.setContextParameter(ic);
		
		
		IvrNetBookService service = new VrNetBookServiceClientImpl();
		DEL040024Result result = service.vrNetBookService("13611502973", "NJDQ", "11111111111111111", "50", "");
		System.out.println(result);
	}
}
