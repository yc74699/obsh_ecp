package com.xwtech.xwecp.test;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryScoreReserveDetailService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryScoreReserveDetailServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY030004Result;
import com.xwtech.xwecp.service.logic.pojo.ScoreReserve;

public class QRY030004Test {
private static final Logger logger = Logger.getLogger(QRY040004Test.class);
	
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
		lic.setUserMobile("13813382424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13813382424");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "2056200011182291");
		
		lic.setContextParameter(ic);
		
		IQueryScoreReserveDetailService service = new QueryScoreReserveDetailServiceClientImpl();
		QRY030004Result res = service.queryScoreReserveDetail("13813382424", "", "");
		List<ScoreReserve> scoreReserveList = res.getScoreReserve();
		ScoreReserve scoreReserve = null;
		Iterator iter = scoreReserveList.iterator();
		while(iter.hasNext())
		{
			scoreReserve = (ScoreReserve)iter.next();
			System.out.println(scoreReserve.getExchangeTime() + scoreReserve.getProduct()
					  + scoreReserve.getExchangeScore()
					  + scoreReserve.getExchangeState()
					  + scoreReserve.getSendTime()
					  + scoreReserve.getNum());
		}
		System.out.println("----END---");
		
	}
	
	
	
}
