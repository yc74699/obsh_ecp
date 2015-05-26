package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryMExchangeDetailService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryMExchangeDetailServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.MExchange;
import com.xwtech.xwecp.service.logic.pojo.QRY030005Result;

public class QRY030005Test {
private static final Logger logger = Logger.getLogger(QRY040004Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://10.32.172.65:8089/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13814812424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13814812424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13814812424");
		ic.addContextParameter("ddr_city", "20");
		
		ic.addContextParameter("user_id", "2056200011182291");
		
		lic.setContextParameter(ic);
		
		IQueryMExchangeDetailService service = new QueryMExchangeDetailServiceClientImpl();
		QRY030005Result result = service.queryMExchangeDetail("13814812424", "20100101", "20100131");
		List<MExchange> mExchangeList = result.getMExchange();
		MExchange mExchange = null;
		for(int i = 0 ; i < mExchangeList.size() ; i ++)
		{
			mExchange = mExchangeList.get(i);
			System.out.println(mExchange.getExchangeDesc()
					+ "--" + mExchange.getExchangeTime()
					+ "--" + mExchange.getChannel()
					+ "--" + mExchange.getType());
		}
		
//////		IQueryMZoneMonthService service = new QueryMZoneMonthServiceClientImpl();
//////		QRY030007Result result = service.queryMZoneMonth("13404262424", "201002");
//////		System.out.println("--------------");
////		
//		IQueryScoreMonthService service = new QueryScoreMonthServiceClientImpl();
//		QRY030006Result result = service.queryScoreMonth("13485062424", "201002");
//		System.out.println("--------------");
	}
	
	
	
}
