package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ITransTerminalSaleChangeService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.TransTerminalSaleChangeServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL100003Result;

public class DEL100003Test
{
	private static final Logger logger = Logger.getLogger(DEL100003Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "terminal_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "tyl");
		props.put("platform.password", "tyl");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13921078544");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13921078544");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		

		lic.setContextParameter(ic);

		ITransTerminalSaleChangeService service = new TransTerminalSaleChangeServiceClientImpl();
		
		//出货退货操作
		//DEL100003Result re = service.transTerminalSaleChange("1", "20110923152311", "20110923152310", "rsclM.24.2420036407", "rsclM.24.2420036407", "351510043081754", "", "OUTSTORE", "100000");
		//换货
		DEL100003Result re = service.transTerminalSaleChange("1", "1111", "20110925000002", "rsclM", "rsclM.26.2620031139", "201107130000005", "201107130000003", "11", "22","1");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" === re.getResultCode() === " + re.getResultCode());
			System.out.println(" === re.getErrorCode() === " + re.getErrorCode());
			System.out.println(" === re.getErrorMessage() === " + re.getErrorMessage());
			
			
		}
	}
}
