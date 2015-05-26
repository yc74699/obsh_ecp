package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IGNFNManageService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.GNFNManageServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040047Result;

public class DEL040047Test {
private static final Logger logger = Logger.getLogger(DEL040047Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10009/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("11");
		lic.setUserMobile("13402691414");  
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13402691414");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "11");
		ic.addContextParameter("ddr_city", "11");
		ic.addContextParameter("user_id", "1101300021123724");
		
		lic.setContextParameter(ic);
		
		IGNFNManageService co = new GNFNManageServiceClientImpl();
		DEL040047Result re = co.gnFNManage("13402691414", "13665202424", "0", "Type_Immediate","1");
		
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== 结果码 ======" + re.getResultCode());
			logger.info(" ====== 错误信息 ======" + re.getErrorMessage());
			logger.info(" ====== getSubMsisdn ======" + re.getProdInfos().get(0).getSubMsisdn());
			logger.info(" ====== getSubMsisdn1 ======" + re.getProdInfos().get(1).getSubMsisdn());
		}
	}
}
