package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICancelIncrementService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CancelIncrementServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL010006Result;

public class DEL010006Test
{
	private static final Logger logger = Logger.getLogger(DEL040023Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
		props.put("platform.url", "http://127.0.0.1:8080/js_ecp_new/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13606132424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13606132424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13606132424");
		ic.addContextParameter("ddr_city", "11");
		ic.addContextParameter("user_id", "1101200007185585");
		
		lic.setContextParameter(ic);
		
		ICancelIncrementService co = new CancelIncrementServiceClientImpl();
		
		DEL010006Result re = co.cancelIncrement("13606132424", "3");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
		}
	}
	
}
