package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IFlowUnFreezeService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.FlowUnFreezeServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL610052Result;

/**
 * 流量解冻接口
 * @author wang.h
 *
 */
public class DEL610052Test {
private static final Logger logger = Logger.getLogger(DEL610052Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("13");
		lic.setUserCity("13");
		lic.setUserMobile("13401801004");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13401801004");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "13");
		ic.addContextParameter("ddr_city", "13");
		
		lic.setContextParameter(ic);
		
		IFlowUnFreezeService ifuf = new FlowUnFreezeServiceClientImpl();
		
		DEL610052Result re = ifuf.flowUnfreeze( "13852320074", "20150416143402", "20150421095017747195", "500");
		if(re != null)
		{
			logger.error("=========== re.getResultCode ============" + re.getResultCode());
			logger.error("=========== re.getErrorMessage ============" + re.getErrorMessage());
		}
	}
}
