package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ILovesVoiceService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.LovesVoiceServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040057Result;

public class DEL040057Test {

	private static final Logger logger = Logger.getLogger(DEL040057Test.class);
	public static void main(String[] args) throws LIException {
//		初始化ecp客户端片段
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
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13912986834");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15861762947");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		
		lic.setContextParameter(ic);
		
		ILovesVoiceService is = new LovesVoiceServiceClientImpl();
		
		//DEL040057Result re =is.orderLovesVoice("15861762947", "2000002307", "13776727885");
		DEL040057Result re = is.cancelLovesVoice("15861762947", "2000002307", "1");//1：立即2：次日3：次月
		if(null != re){
			System.out.println("--resultCode--"+re.getResultCode());
			System.out.println("--desc--------"+re.getErrorMessage());
		}

	}

}
