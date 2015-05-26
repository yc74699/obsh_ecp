package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICheckCreditService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CheckCreditServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040102Result;

/**
 * 信用开机校验
 * @author wang.h
 *
 */
public class QRY040102Test {
	private static final Logger logger = Logger.getLogger(QRY040102Test.class);
	
	public static void main(String[] args) throws Exception
	{
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
		lic.setUserMobile("13813382424");
		lic.setUserMobile("13813382424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13861427474");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("user_id", "1101200007185585");
		lic.setContextParameter(ic);
		
		String city = "12";
		String msisdn = "13401824074";
		ICheckCreditService ics= new CheckCreditServiceClientImpl();
		QRY040102Result re = ics.checkCredit(msisdn, city);
			System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
			System.out.println(" ====== getPrecreditval ======" + re.getPrecreditval());
		}
	}
}
