package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IChgFamilyMainProdService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ChgFamilyMainProdServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.ChgProdInfo;
import com.xwtech.xwecp.service.logic.pojo.DEL080003Result;

public class DEL080003Test
{
	private static final Logger logger = Logger.getLogger(DEL080003Test.class);
	
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

		IChgFamilyMainProdService co = new ChgFamilyMainProdServiceClientImpl();
        List<ChgProdInfo> chgProdInfos = new ArrayList<ChgProdInfo>();
		
        ChgProdInfo chgProdInfo = new ChgProdInfo();
     
        
        chgProdInfos.add(chgProdInfo);
        
        DEL080003Result re = co.chgFamilyMainProd("", "", "", chgProdInfos);
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
		}
	}
}