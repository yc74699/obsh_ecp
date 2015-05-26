package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryNewSpandProduseCHService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryNewSpandProduseCHServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040109Result;

public class QRY040109Test
{
	private static final Logger logger = Logger.getLogger(QRY040109Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
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
		lic.setUserMobile("15189570755");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15189570755");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("user_id", "1214300000796048");

		lic.setContextParameter(ic);

		IQueryNewSpandProduseCHService service = new QueryNewSpandProduseCHServiceClientImpl();

		QRY040109Result re = service.queryNewSpandProduseCH("1214300000796048","201503",2,"","1");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" === re.getResultCode() === " + re.getResultCode());
			System.out.println(" === re.getErrorCode() === " + re.getErrorCode());
			System.out.println(" === re.getErrorMessage() === " + re.getErrorMessage());
		}
	}
}
