package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IFusionUnionPayService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.FusionUnionPayServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL610038Result;

public class DEL610038Test
{
	private static final Logger logger = Logger.getLogger(DEL610038Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
//		props.put("platform.url", "http://10.32.65.67:8080/obsh_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.122.167:10005/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("22");
		lic.setUserMobile("13951552546");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13951552546");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "22");
		ic.addContextParameter("ddr_city", "22");
		

		lic.setContextParameter(ic);

		IFusionUnionPayService service = new FusionUnionPayServiceClientImpl();
		
		
		DEL610038Result re = service.fusionUnionPay("13951552546", 1, "1","2288231419039762");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" === re.getResultCode() === " + re.getResultCode());
			System.out.println(" === re.getErrorCode() === " + re.getErrorCode());
			System.out.println(" === re.getErrorMessage() === " + re.getErrorMessage());
			
			
		}
	}
}
