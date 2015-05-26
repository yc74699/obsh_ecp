package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IRegistUserBirthday;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.RegistUserBirthdayClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL030008Result;


public class DEL030008Test
{
	private static final Logger logger = Logger.getLogger(DEL030008Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://10.32.122.166:10004/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13913814503");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13776632514");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13776632514");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "1419200008195160");  //2056200011182291
		
		
		lic.setContextParameter(ic);
		
		IRegistUserBirthday co = new RegistUserBirthdayClientImpl();
		//动感地带 13401312424  brand_id：11 city_id：17 user_id：1738200005062065
		//全球通 13913032424 user_id：1419200008195160
		//1、开通 2、关闭 3、变更
		//1、立即 2、次日 3、次月
		DEL030008Result re = co.registUserBirthday("13776632514", "1987-06-25", "Lunar_Calendar", "1");
		logger.info(" ====== 开始返回参数 ======"+re);
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
		}
	}
}
