package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IGetSuperProdService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.GetSuperProdServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY060075Result;
import com.xwtech.xwecp.service.logic.pojo.SuperProdList;

public class QRY060075Test
{
	private static final Logger logger = Logger.getLogger(QRY060075Test.class);
	
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
		lic.setUserCity("14");
		lic.setUserMobile("13776632514");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13776632514");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		ic.addContextParameter("user_id", "1419200008195160");
		
		lic.setContextParameter(ic);
		
		IGetSuperProdService co = new GetSuperProdServiceClientImpl();
		List<SuperProdList> usersuperpkg = new ArrayList<SuperProdList>();
		SuperProdList tt = new SuperProdList();
		//tt.setAttr(attr);
		QRY060075Result re = co.getSuperPkg("", "");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			for(SuperProdList pd : re.getSuperProdList()){
			
			}
			
		}
	}
}
