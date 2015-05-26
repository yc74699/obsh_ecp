package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.ICustomEmailBillService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CustomEmailBillServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL011001Result;
import com.xwtech.xwecp.service.logic.pojo.SendMailList;

public class DEL011001Test {
	private static final Logger logger = Logger.getLogger(DEL011001Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
//		props.put("platform.url", "http://127.0.0.1:8080/js_ecp_new/xwecp.do");
		props.put("platform.url", "http://10.32.65.238:8081/sms_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13913814503");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913814503");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13913814503");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1423200000471569");
		
		lic.setContextParameter(ic);
		
		List<SendMailList> list = new ArrayList();
		SendMailList dt = null;
		dt = new SendMailList();
		dt.setSendType("1");
		list.add(dt);
		ICustomEmailBillService co = new CustomEmailBillServiceClientImpl();
		DEL011001Result re = co.customEmailBill("13913814503", list, "123@139.com", 0, 2);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorCode ======" + re.getErrorCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
		}
	}
}
