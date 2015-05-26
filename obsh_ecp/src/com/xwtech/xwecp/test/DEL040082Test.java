package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ILeftGprsTranstosubNumService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.LeftGprsTranstosubNumServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040082Result;

/**
 *	剩余流量转赠
 * @author TG
 * 2014-7-28
 */
public class DEL040082Test
{
	private static final Logger logger = Logger.getLogger(DEL040082Test.class);

	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
//		props.put("platform.url", "http://10.32.65.238:8080/wap_ecp/xwecp.do");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13913814503");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913814503");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("user_id", "1423200000471569");
		lic.setContextParameter(ic);
		
		ILeftGprsTranstosubNumService query = new LeftGprsTranstosubNumServiceClientImpl();
		DEL040082Result result = query.leftGprsTranstosubNum("","","");
		if (result != null)
		{
			logger.info(" ====== getResultCode ========" + result.getResultCode());
			logger.info(" ====== getErrorCode =========" + result.getErrorCode());
			logger.info(" ====== getErrorMessage ======" + result.getErrorMessage());
		}
	}
}
