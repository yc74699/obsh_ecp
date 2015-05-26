package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IDealExtCardService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQrygprsSectionfluxService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryTelnumSchoolService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.DealExtCardServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QrygprsSectionfluxServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryTelnumSchoolServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040065Result;
import com.xwtech.xwecp.service.logic.pojo.QRY610037Result;
import com.xwtech.xwecp.util.DateTimeUtil;

public class QRY610037Test {
	private static final Logger logger = Logger.getLogger(QRY610037Test.class);
	
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
		lic.setUserCity("13");
		lic.setUserMobile("15195917420");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15195917420");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1425300016047771");
		
		lic.setContextParameter(ic);
		// 
		
		IQueryTelnumSchoolService co = new QueryTelnumSchoolServiceClientImpl();
		QRY610037Result re = co.qryTelnumSchool("15195917420", "123");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getResultCode ======" + re.getErrorMessage());
			System.out.println(" ====== getIsactive ======" + re.getIsactive());
			System.out.println(" ====== getSchoolname ======" + re.getSchoolname());
		}
	}
}
