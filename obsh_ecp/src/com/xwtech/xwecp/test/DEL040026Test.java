package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IHomeBillOpenService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.HomeBillOpenServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040026Result;
import com.xwtech.xwecp.service.logic.pojo.Memberdt;

/**
 * 
 * 家庭帐户开户
 *
 */
public class DEL040026Test
{
	private static final Logger logger = Logger.getLogger(DEL040026Test.class);
	
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
		lic.setUserMobile("15250890055");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15250890055");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "15250890055");
		ic.addContextParameter("context_loginiplock_login_ip", "127.0.0.1");
		ic.addContextParameter("ddr_city", "12");		
		lic.setContextParameter(ic);
		
		IHomeBillOpenService co = new HomeBillOpenServiceClientImpl();
		Memberdt dt = new Memberdt();
		dt.setMemberPhoneNum("13776632514");
		dt.setOpenType("1");
		
		List<Memberdt> memberList = new ArrayList<Memberdt>();
		memberList.add(dt);

		DEL040026Result re = co.openHomeBill("13813382424","1", "1", "0", memberList);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorCode ======" + re.getErrorCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
		}
	}
}
