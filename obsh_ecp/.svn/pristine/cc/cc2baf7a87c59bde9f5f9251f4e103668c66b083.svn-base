package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryGPRSPkgFluxService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryGPRSPkgFluxServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040045Result;

public class QRY040045Test
{
	private static final Logger logger = Logger.getLogger(QRY040045Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		//props.put("platform.url", "http://10.32.229.74:8080/js_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.166:10006/obsh_ecp/xwecp.do");
		props.put("platform.url", "http://127.0.0.1/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13505239567");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13505239567");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("user_id", "1211200010165511");
		
		lic.setContextParameter(ic);
		//
		
		IQueryGPRSPkgFluxService co = new QueryGPRSPkgFluxServiceClientImpl();
		QRY040045Result re = co.qryGPRSPkgFlux("1211200010165511","201207");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getNotebookTotalFlux ======" + re.getNotebookTotalFlux());
			System.out.println(" ====== getNotebookUserFlux ======" + re.getNotebookUserFlux());
			System.out.println(" ====== getGprsTotalFlux ======" + re.getGprsTotalFlux());
			System.out.println(" ====== getGprsUserdFlux ======" + re.getGprsUserdFlux());

		}
	}
}
