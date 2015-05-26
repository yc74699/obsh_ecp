package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryDbiInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryDbiInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY010014Result;

public class QRY010014Test {
	private static final Logger logger = Logger.getLogger(QRY010014Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.74:8080/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13815890413");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13815890413");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13815890413");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "1419300019772126");
		
		lic.setContextParameter(ic);
		
		IQueryDbiInfoService co = new QueryDbiInfoServiceClientImpl();

		QRY010014Result re = co.queryDbiInfo("13815890413", "201312");

		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			
		}
	}
}
