package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IFlowTransService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.FlowTransServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY610050Result;

/**
 * 流量转移分发接口
 * @author 张斌
 * 2015-4-22
 */
public class QRY610050Test {
	private static final Logger logger = Logger.getLogger(QRY610050Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
//		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.65.114:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("1");
		lic.setUserCity("12");
		lic.setUserMobile("13852320074");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13852320074");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		lic.setContextParameter(ic);
		
		IFlowTransService co = new FlowTransServiceClientImpl();
		
		QRY610050Result re = co.flowTrans("13852320074", "20150416143402", "20150421095017747195", "15805239434", "19", "1001", "50", "");
		System.out.println(" ====== 开始返回参数 ======"+re);
		if (re != null)
		{
			System.out.println(re.getResultCode());
			System.out.println(re.getErrorCode());
			System.out.println(re.getErrorMessage());
			System.out.println(re.getOperatingSrl());

		}
	}
}
