package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ITransactFamilyNetService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.TransactFamilyNetServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL080002Result;
import com.xwtech.xwecp.service.logic.pojo.FamilyShortInfo;

public class DEL080002Test
{
	private static final Logger logger = Logger.getLogger(DEL080002Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("全球通");
		lic.setUserCity("12");
		lic.setUserMobile("13952325168");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13952325168");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("context_loginiplock_login_ip", "127.0.0.1");
		
		ic.addContextParameter("user_id", "1208200003094385");
		
		
		lic.setContextParameter(ic);

		ITransactFamilyNetService co = new TransactFamilyNetServiceClientImpl();
        List<FamilyShortInfo> familyShortInfos = new ArrayList<FamilyShortInfo>();
		
        FamilyShortInfo familyShortInfo1 = new FamilyShortInfo();
        familyShortInfo1.setUserMobileNum("13952325168");
        familyShortInfo1.setUserMobileShortNum("760");
        
        FamilyShortInfo familyShortInfo2 = new FamilyShortInfo();
        familyShortInfo2.setUserMobileNum("18762071736");
        familyShortInfo2.setUserMobileShortNum("766");
        
        
        familyShortInfos.add(familyShortInfo1);
        familyShortInfos.add(familyShortInfo2);
        
		DEL080002Result re = co.transactFamilyNet("01", "0", "1208300001106751", "2000002386", familyShortInfos);
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
		}
	}
}
