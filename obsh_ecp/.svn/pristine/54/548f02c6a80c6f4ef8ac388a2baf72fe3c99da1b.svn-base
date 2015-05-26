package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryFlowfreezebalService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryFlowfreezebalServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY610048Result;

/**
 * 流量冻结流水查询接口
 * @author YangXQ
 * 2015-04-22
 */
public class QRY610048Test {
	private static final Logger logger = Logger.getLogger(QRY610048Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "wap_channel");  
//		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");  
		props.put("platform.url", "http://10.32.65.238:8080/wap_ecp/xwecp.do"); 
		props.put("platform.user", "xl"); 
		props.put("platform.password", "xl"); 
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand(""); 
		lic.setUserCity("12"); 
		lic.setUserMobile("");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "");
		ic.addContextParameter("route_type", "1");           
		ic.addContextParameter("route_value", "12"); 
		lic.setContextParameter(ic);
		 
		IQueryFlowfreezebalService co = new QueryFlowfreezebalServiceClientImpl();
		/**
		 * 业务流水
		 * 冻结流水,为冻结时返回的formnum冻结流水
		 */
		QRY610048Result re = co.flowfreezebal("","20150420114703746546");
		
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());		 
		}
	}
}
