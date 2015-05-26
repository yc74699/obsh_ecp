package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IFNConfrimService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.FNConfrimServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL010017Result;

public class DEL010017Test
{
	private static final Logger logger = Logger.getLogger(DEL010017Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
		props.put("platform.url", "http://127.0.0.1:8080/js_ecp_new/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8081/sms_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13606132424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13606132424");
		ic.addContextParameter("route_type", "2");              //13606132424/1101200007185585
		ic.addContextParameter("route_value", "13606132424");   //13861437474/1527200002161873
		ic.addContextParameter("ddr_city", "11");                //13815877214/1419200005993055
		ic.addContextParameter("user_id", "1101200007185585");  //13813382424-1419200008195116,13913032424-1419200008195160
		lic.setContextParameter(ic);                            //13665232424/2371200004318856
		
		IFNConfrimService co = new FNConfrimServiceClientImpl();
		//isConfrim 1:确认 2：拒绝
		String isConfrim = "1";
		
		DEL010017Result re = co.fnConfrim("13913824943",isConfrim);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
		}
	}
}
