package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IKKHLWRITECARDAPPLYService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.KKHLWRITECARDAPPLTYServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040108Result;

public class DEL040108Test {
	private static final Logger logger = Logger.getLogger(DEL040108Test.class);
	public static void main(String[] args) throws Exception
	{
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
		lic.setUserMobile("13912077691");
		lic.setUserMobile("13912077691");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13912077691");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		lic.setContextParameter(ic);
		
		/**
		 * <orderid>88122545631194</orderid>
                <operid></operid>
                <busifee>10</busifee>
        </content>

		 */
		
		String orderId = "88278207545917";
		String operId = "";
		String busifee = "10";
		
		IKKHLWRITECARDAPPLYService service = new KKHLWRITECARDAPPLTYServiceClientImpl();
		DEL040108Result re = service.kkhlApply("12", "11802050004665", "460027620215640", "18606020687");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
		}
	}
}
