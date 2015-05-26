package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;

import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryMoneyAvailCountService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryMoneyAvailCountServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040038Result;


public class QRY040038Test
{
	private static final Logger logger = Logger.getLogger(QRY040038Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("11");
		lic.setUserMobile("13584822174");  
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13584822174");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "11");
		ic.addContextParameter("ddr_city", "11");
		ic.addContextParameter("user_id", "1101200014349813");
		
		lic.setContextParameter(ic);
		
		IQueryMoneyAvailCountService co = new QueryMoneyAvailCountServiceClientImpl();
		QRY040038Result re = co.queryMoneyAvailCount("13584822174", "10000");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== 结果码 ======" + re.getResultCode());
			logger.info(" ====== 票个数 ======" + re.getAvailCount());
			logger.info("=======是否可以兑换====" + re.getIfCanUse());
		}
	}
}
