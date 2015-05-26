package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryBlanceService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryBlanceServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.BlanceInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY010012Result;

public class QRY010012Test {
private static final Logger logger = Logger.getLogger(QRY010012Test.class);
	
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
		lic.setUserMobile("13701457474");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13701457474");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13701457474");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "1419200008215809");
		
		lic.setContextParameter(ic);
		
		
		IQueryBlanceService  co = new QueryBlanceServiceClientImpl();
		Long l1 = System.currentTimeMillis();
		

		QRY010012Result result = co.queryBlance("13701457474", "201002");
		Long l2 = System.currentTimeMillis();
		System.out.println("--------" + (l2-l1));
		List<BlanceInfo> operDetail = result.getBlanceInfo();
		
		for(int i = 0 ; i < operDetail.size() ; i ++)
		{
			System.out.println(operDetail.get(i).getName() + "---" + operDetail.get(i).getBalance());
		}
		
	}
	
	public static String transferDate(String date)
	{
		
		String result1 = date.substring(0,4);
		String result2 = date.substring(4,6);
		String result3 = date.substring(6,8);
		String result = result1 + "-" + result2 + "-" + result3;
		return result;
	}
	
}
