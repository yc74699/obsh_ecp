package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IChangeScoreService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ChangeScoreServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL610053Result;

/**
 * 积分扣减接口
 * @author 张斌
 * 2015-4-27
 */
public class DEL610053Test {
private static final Logger logger = Logger.getLogger(DEL610053Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("13");
		lic.setUserCity("13");
		lic.setUserMobile("13401801004");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13401801004");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "13");
		ic.addContextParameter("ddr_city", "13");
		
		lic.setContextParameter(ic);
		
		IChangeScoreService ics = new ChangeScoreServiceClientImpl();
		
		DEL610053Result re = ics.changeScore("13515292424", "10", "1", "订单号", "201504271000", "");
		if(re != null)
		{
			logger.error("=========== re.getResultCode ============" + re.getResultCode());
			logger.error("=========== re.getErrorMessage ============" + re.getErrorMessage());
			logger.error("=========== re.getOperatingSrl ============" + re.getOperatingSrl());
			logger.error("=========== re.getScoreBal ============" + re.getScoreBal());
		}
	}
}
