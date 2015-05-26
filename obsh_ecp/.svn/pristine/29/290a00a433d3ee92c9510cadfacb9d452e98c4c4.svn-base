package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryNetUserInfoOrFixedPhoneUserInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryNetUserInfoOrFixedPhoneUserInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040021Result;
import com.xwtech.xwecp.util.DESEncrypt;

public class QRY040021Test
{
	private static final Logger logger = Logger.getLogger(QRY040021Test.class);
	
	private static byte[] BOSS_SECRET_KEY = {
		0x0b,0x33,(byte)0xe7,(byte)0xb2,0x51,0x0d,0x75,(byte)0xc3,0x4e,
		(byte)0xdd,(byte)0x3b,(byte)0x51,0x24,0x36,(byte)0xa8,(byte)0x28,
		0x0b,0x33,(byte)0xe7,(byte)0xb2,0x51,0x0d,0x75,(byte)0xc3	
	};
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
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("80773280");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "80773280");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "80773280");
		
		lic.setContextParameter(ic);
		
		IQueryNetUserInfoOrFixedPhoneUserInfoService re = new QueryNetUserInfoOrFixedPhoneUserInfoServiceClientImpl();
		
		//宽带账号测试
		//QRY040021Result result = re.queryNetUserInfoOrFixedPhoneUserInfo("nj_13901582904", "EEE50BF544BCD3EE", "", "1");
		
		//固定电话测试
		QRY040021Result result = re.queryNetUserInfoOrFixedPhoneUserInfo("80773280", DESEncrypt.desString("666888", BOSS_SECRET_KEY), "HADQ", "2");
		
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
		}
	}
}
