package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryLoverSMSPackageService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryLoverSMSPackageServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY020011Result;

public class QRY020011Test
{
	private static final Logger logger = Logger.getLogger(QRY020011Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://localhost/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13805157824");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13805157824");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		ic.addContextParameter("user_id", "1419200001869070");
		
		lic.setContextParameter(ic);
		
		IQueryLoverSMSPackageService loverSMSPackageService = new QueryLoverSMSPackageServiceClientImpl();
		QRY020011Result result = loverSMSPackageService.queryLoverSMSPackageService("1419200001869070");
		System.out.println(result);
	}
}
