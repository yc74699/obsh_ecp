package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryInterFeePayService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryInterFeePayServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.PayOnline;
import com.xwtech.xwecp.service.logic.pojo.QRY040019Result;

public class QRY040019Test {
private static final Logger logger = Logger.getLogger(QRY040014TestApp.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13646272637");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13685226662");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13685226662");
		ic.addContextParameter("ddr_city", "17");
		
		ic.addContextParameter("user_id", "2050200009867279");
		
		lic.setContextParameter(ic);
		
		IQueryInterFeePayService service = new QueryInterFeePayServiceClientImpl();
		
		//1.宽带上网账号
		//2.随E行上网账号
		
		QRY040019Result result = service.queryInterFeePay("13813382424", "7089F5FD852898A6", "1");
		List<PayOnline> payOnlineList = result.getPayOnlineList();
		PayOnline payOnline = null;
		for(int i = 0 ; i < payOnlineList.size() ; i ++)
		{
			payOnline = payOnlineList.get(i);
			System.out.println(payOnline.getPayedMsisdn() + "--" + payOnline.getStartDate() + "--" + payOnline.getEndDate());
		}
		
		System.out.println("-----end-----------");
		
	
	}
	
	
}
