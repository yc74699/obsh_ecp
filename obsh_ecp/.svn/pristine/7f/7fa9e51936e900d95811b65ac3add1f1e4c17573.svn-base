package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryPhoneBalanceService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryPhoneBalanceServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY010016Result;

public class QRY010016Test {
	private static final Logger logger = Logger.getLogger(QRY010016Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13601400067");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13401312424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "NJDQ");
		ic.addContextParameter("ddr_city", "NJDQ");
		
		ic.addContextParameter("user_id", "1842200007838968");
		ic.addContextParameter("loginiplock_login_ip", "192.168.0.46");
		
		
		lic.setContextParameter(ic);
		
		IQueryPhoneBalanceService co = new QueryPhoneBalanceServiceClientImpl();
		QRY010016Result re = co.queryPhoneBalance("88080014");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
			
			logger.info(" ====== getNewBalance ======" + re.getNewBalance());
			logger.info(" ====== getBalance ======" + re.getBalance());
			logger.info(" ====== getAccountExpireDate ======" + re.getAccountExpireDate());
		}
	}
}
