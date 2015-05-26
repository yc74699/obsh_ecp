package com.xwtech.xwecp.test;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryScoreExchangeDetailService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryScoreExchangeDetailServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY030003Result;
import com.xwtech.xwecp.service.logic.pojo.ScoreExchange;

public class QRY030003Test {
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
		lic.setUserCity("14");
		lic.setUserMobile("13913814503");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13913814503");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13913814503");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "");
		
		lic.setContextParameter(ic);
		
		IQueryScoreExchangeDetailService service = new QueryScoreExchangeDetailServiceClientImpl();
		QRY030003Result res = service.queryScoreExchangeDetail("13913814503", "", "");
		List<ScoreExchange> scoreExchangeList = res.getScoreExchange();
		ScoreExchange soreExchange = null;
		Iterator iter = scoreExchangeList.iterator();
		
		logger.info(" ====== 开始返回参数 ======");
		if (res != null)
		{
			logger.info(" === re.getResultCode() === " + res.getResultCode());
			logger.info(" === re.getErrorCode() === " + res.getErrorCode());

			logger.info(" === re.getErrorMessage() === " + res.getErrorMessage());
			while(iter.hasNext())
			{
				soreExchange = (ScoreExchange)iter.next();
				System.out.println(soreExchange.getExchangeTime() + "--"
						           + soreExchange.getExchangeType() + "--"
						           + soreExchange.getExchangeScore() + "--"
						           + soreExchange.getExchangeFee() + "--"
						           + soreExchange.getExchangeChannel());
			}
		}
		
		
	}
	
	
	
}
