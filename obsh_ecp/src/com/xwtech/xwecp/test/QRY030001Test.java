package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryExchangeListService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryExchangeListServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.Exchange;
import com.xwtech.xwecp.service.logic.pojo.QRY030001Result;

public class QRY030001Test {
private static final Logger logger = Logger.getLogger(QRY040004Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://10.32.172.65:8086/ecp/xwecp.do");
//		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13675149526");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13675149526");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13675149526");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "1419200009683023");
		
		lic.setContextParameter(ic);
		
		IQueryExchangeListService service = new QueryExchangeListServiceClientImpl();
		//""  M值可兑换话费列表   "5"为积分可兑换话费列表

//		QRY030001Result res = service.queryExchangeList("13813382424", "5");
		QRY030001Result res = service.queryExchangeList("13814812424", "");
		
		List<Exchange> exchangeList = res.getExchangeList();
		Exchange exchange = null;
		
		for(int i = 0 ; i < exchangeList.size() ; i ++)
		{
			exchange = (Exchange)exchangeList.get(i);
			System.out.println(exchange.getNeedScores() + "--"
					    + exchange.getBizId() + "--"
					    + exchange.getBizDesc() + "--"
					    + exchange.getExtNo());
		}
		
	}
	
	
	
}
