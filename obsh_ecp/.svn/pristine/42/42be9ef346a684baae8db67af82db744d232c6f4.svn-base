package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryYxfaYktService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryYxfaYktServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY070006Result;
import com.xwtech.xwecp.service.logic.pojo.YxfaYktBaseInfo;
import com.xwtech.xwecp.service.logic.pojo.YxfaYktRefInfo;

public class QRY070006Test {
	private static final Logger logger = Logger.getLogger(QRY070006Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1:8081/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		 lic.setBizCode("biz_code_19234");
		 lic.setOpType("开通/关闭/查询/变更");
		 lic.setUserBrand("动感地带");
		 lic.setUserCity("用户县市");
		 lic.setUserMobile("13601400067");
		InvocationContext ic = new InvocationContext();
		//ic.addContextParameter("login_msisdn", "13913032424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		//ic.addContextParameter("request_seq", "1_1");
		//ic.addContextParameter("request_time", "20090728024911");
		ic.addContextParameter("ddr_city", "14");
		
		//ic.addContextParameter("user_id", "2056200011182291");
		
		lic.setContextParameter(ic);
		 
		IQueryYxfaYktService co = new QueryYxfaYktServiceClientImpl();
 
		QRY070006Result  re = co.queryYxfaYkt("14", "1419200013404839");
		 
		for (YxfaYktBaseInfo p : re.getYxfaYktBaseInfo())
		{
		System.out.println("记录数：" +p.getUsermarketingbaseinfo_inure_date());
		 
		}
		
		for (YxfaYktRefInfo p : re.getYxfaYktRefInfo())
		{
		System.out.println("记录数：" +p.getMarketingplan_name());
		 
		}
		logger.info(" ====== 开始返回参数 ======");
	}
}
