package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryInternetFeeListService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryInternetFeeListServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.FixedPhone;
import com.xwtech.xwecp.service.logic.pojo.QRY010010Result;

public class QRY010010Test {
	private static final Logger logger = Logger.getLogger(QRY010010Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		//props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13921909348");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13921909348");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13921909348");
		ic.addContextParameter("ddr_city", "23");
		ic.addContextParameter("user_password", "2A032E310906109F");
		ic.addContextParameter("user_id", "2371200001226345");
		
		lic.setContextParameter(ic);
		
		IQueryInternetFeeListService co = new QueryInternetFeeListServiceClientImpl();

		QRY010010Result re = co.queryInternetFeeList("13921909348", "201109");

		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			
			if (null != re.getFixedPhoneList())
			{
			
				for (FixedPhone info : re.getFixedPhoneList()) {
					System.out.println(" ====== info.getCode ======" + info.getStartTime());
					System.out.println(" ====== info.getCode ======" + info.getVisitArea());
					System.out.println(" ====== info.getCode ======" + info.getCallDuration());
					System.out.println(" ====== ============================== ======");
				}
	
			}
		}
	}
}
