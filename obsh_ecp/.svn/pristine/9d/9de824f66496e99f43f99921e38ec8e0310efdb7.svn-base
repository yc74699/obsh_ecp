package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IChangeUserPassService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ChangeUserPassServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040006Result;

public class QRY040006Test
{
	private static final Logger logger = Logger.getLogger(QRY040006Test.class);
	
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
		lic.setUserCity("用户县市");
		lic.setUserMobile("13913032424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913032424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("context_loginiplock_login_ip", "127.0.0.1");
		ic.addContextParameter("user_id", "1423200000471569");  //2056200011182291
		lic.setContextParameter(ic);
		
		//组装自有业务的list
		IChangeUserPassService co = new ChangeUserPassServiceClientImpl();
		QRY040006Result re = co.changeUserPass("13913032424", "", "100866", 0, 2);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
			logger.info(" === re.getDefaultPwd() === " + re.getDefaultPwd());
		}
	}
}
