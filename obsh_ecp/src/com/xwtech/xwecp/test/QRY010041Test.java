package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryRealtimeService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryRealtimeServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY010041Result;
/**
 * 根据CRM工号查询出员工姓名等详细信息
 * @author xufan
 * 20140918
 */
public class QRY010041Test
{
	private static final Logger logger = Logger.getLogger(QRY010041Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel"); 
		props.put("platform.url", "http://10.32.65.238:8182/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.64:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.69:10006/mall_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
//		LIInvocationContext lic = LIInvocationContext.getContext();
//		lic.setBizCode("biz_code_19234");
//		lic.setOpType("开通/关闭/查询/变更");
//		lic.setUserBrand("动感地带");
//		lic.setUserCity("12");
//		lic.setUserMobile("13951073551");
//		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "13951073551");
//		ic.addContextParameter("route_type", "1");
//		ic.addContextParameter("route_value", "12");
////		ic.addContextParameter("ddr_city", "12");
//		ic.addContextParameter("user_id", "1425200001139630");  //13813382424-1419200008195116,13913032424-1419200008195160
//		lic.setContextParameter(ic);
		IQueryRealtimeService co = new QueryRealtimeServiceClientImpl();
//		QRY010041Result re = co.qryRealtime("1432771");
		QRY010041Result re = co.qryRealtime("1432771","12");

		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
		}
	}
}
