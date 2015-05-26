package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryUserLimitTimeActService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryUserLimitTimeActServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY060070Result;

public class QRY060070Test {
	private static final Logger logger = Logger.getLogger(QRY060070Test.class);

	public static void main(String[] args) throws Exception {
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");

		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("11");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "11");
		ic.addContextParameter("ddr_city", "11");
		ic.addContextParameter("user_id", "1105200019486742");
		lic.setContextParameter(ic);

		IQueryUserLimitTimeActService co = new QueryUserLimitTimeActServiceClientImpl();

		QRY060070Result re = co.queryUserLimitTimeAct("15150268495");

		if (null != re)
		{
			logger.info(" ====== getBossCode ======" + re.getResultCode());
			logger.info(" ====== getBossCode ======" + re.getErrorMessage());
		
		}

	}
}
