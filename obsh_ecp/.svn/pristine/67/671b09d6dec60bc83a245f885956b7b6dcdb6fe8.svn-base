package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQryUsimCardCheckService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QryUsimCardCheckServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.OrderDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY040091Result;

public class QRY040091Test {
	private static final Logger logger = Logger.getLogger(QRY040091Test.class);
	public static void main(String[] args)  throws Exception{
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
		lic.setUserMobile("13515248887");
		lic.setUserMobile("13515248887");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13515248887");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		lic.setContextParameter(ic);
		
		IQryUsimCardCheckService service = new QryUsimCardCheckServiceClientImpl();
		/**
		 * cardsn    卡号 
		 * password  密码
		 */
		QRY040091Result re = service.qryUsimCardCheck("11802050004665", "100006");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println("====== getRetCode ====================" + re.getRetCode());
			System.out.println("====== getCheckStatus ====================" + re.getCheckStatus());
			System.out.println("====== getImsi ====================" + re.getImsi());
			System.out.println("====== getRegion ====================" + re.getRegion());
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
		}
	}
}
