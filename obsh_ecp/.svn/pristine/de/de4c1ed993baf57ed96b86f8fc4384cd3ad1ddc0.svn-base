package com.xwtech.xwecp.test;

import java.util.Properties;
import org.apache.log4j.Logger;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQrygprsdayfluxService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QrygprsdayfluxServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040064Result;

public class QRY040064Test
{
	private static final Logger logger = Logger.getLogger(QRY040064Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段  
		Properties props = new Properties();
		props.put("client.channel", "jsmcc_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
		props.put("platform.user", "mfw");
		props.put("platform.password", "mfw");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13815890413");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");		
		ic.addContextParameter("user_id", "1419300019772126");
		
		
		lic.setContextParameter(ic);
		
		IQrygprsdayfluxService co = new QrygprsdayfluxServiceClientImpl();
		QRY040064Result re = co.qryGprsDayFlux("201401");
			//co.scbQueryNew("15151539700","1","201312","201405");
		logger.info(" ====== 开始返回参数 ======");
		logger.info(" ====== getResultCode ======" + re.getResultCode());
	}
}