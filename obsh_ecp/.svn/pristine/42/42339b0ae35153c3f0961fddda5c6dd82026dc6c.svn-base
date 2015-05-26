package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICheckUnionPayState;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CheckUnionPayStateClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050030Result;

public class QRY050030Test {
	private static final Logger logger = Logger.getLogger(QRY050030Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/xwecp/xwecp.do");
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
		ic.addContextParameter("login_msisdn", "13913032424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "1419200008195160");
		
		lic.setContextParameter(ic);
		
		ICheckUnionPayState checkUnionPayState = new CheckUnionPayStateClientImpl();
		QRY050030Result re = checkUnionPayState.checkUnionPayState("NJDQ", "192.168.0.46");
		
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info("是否开启:" + re.getState());
		}
	}
}