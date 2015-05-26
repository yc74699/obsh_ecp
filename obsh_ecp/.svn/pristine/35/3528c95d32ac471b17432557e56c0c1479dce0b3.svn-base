package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryBalScoreService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryBalScoreServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040002Result;

public class QRY040002Test
{
	private static final Logger logger = Logger.getLogger(QRY040002Test.class);
	
	/**
	 * 新大陆提供的密钥，需要每两位转成1个字节
	 */
	private static byte[] BOSS_SECRET_KEY = {
		0x0b,0x33,(byte)0xe7,(byte)0xb2,0x51,0x0d,0x75,(byte)0xc3,0x4e,
		(byte)0xdd,(byte)0x3b,(byte)0x51,0x24,0x36,(byte)0xa8,(byte)0x28,
		0x0b,0x33,(byte)0xe7,(byte)0xb2,0x51,0x0d,0x75,(byte)0xc3	
	};
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://10.32.122.167:10000/js_ecp/xwecp.do");
		
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13905166045");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "");
		ic.addContextParameter("route_type", "");
		ic.addContextParameter("route_value", "");
		ic.addContextParameter("ddr_city", "");
		
		ic.addContextParameter("user_id", "");  //2056200011182291
		
		lic.setContextParameter(ic);
		
		IQueryBalScoreService queryBalScoreService = new QueryBalScoreServiceClientImpl();
		QRY040002Result result = queryBalScoreService.queryBalScore("13913814503", "1");
		if (result != null)
		{
			logger.info(" ====== getResultCode1 ======" + result.getBalance());
			logger.info(" ====== getResultCode ======" + result.getResultCode());
			logger.info(" ====== getErrorCode ======" + result.getErrorCode());
			logger.info(" ====== getErrorMessage ======" + result.getErrorMessage());
		}
	}
}
