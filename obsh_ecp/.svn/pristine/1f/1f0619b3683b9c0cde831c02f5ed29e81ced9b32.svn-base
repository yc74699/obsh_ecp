package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICashPaymentService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CashPaymentServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY610046Result;

public class QRY610046Test {
	private static final Logger logger = Logger.getLogger(QRY610046Test.class);
	public static void main(String[] args) throws com.xwtech.xwecp.service.logic.client_impl.common.LIException{
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
		lic.setUserMobile("13852320074");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13852320074");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("user_id", "1225300016047771");
		
		lic.setContextParameter(ic);
		ICashPaymentService co = new CashPaymentServiceClientImpl();
		QRY610046Result re = co.cashPayment("12", "13852320074", "20150417143401", "0", "19", "levelone", "20", "1001", "1024", "20150501", "20150503");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getResultCode ======" + re.getErrorMessage());
			System.out.println(" ====== getret_code ======" + re.getRet_code());
			System.out.println(" ====== getoperating_srl ======" + re.getOperating_srl());
			System.out.println(" ====== gettaskoid ======" + re.getTaskoid());
		}
		
	}
	
}
