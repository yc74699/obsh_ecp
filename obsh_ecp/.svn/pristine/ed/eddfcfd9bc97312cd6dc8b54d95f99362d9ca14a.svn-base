package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IChangeNumService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ChangeNumServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040062Result;

/**
 * 
 * 增加成员
 *
 */
public class QRY040062Test
{
	private static final Logger logger = Logger.getLogger(QRY040062Test.class);
	
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
		lic.setUserCity("11");
		lic.setUserMobile("13511600004");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13511600004");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13511600004");
		ic.addContextParameter("ddr_city", "11");
		ic.addContextParameter("user_id", "1101300005632214");
		
		lic.setContextParameter(ic);
		
		IChangeNumService  is = new ChangeNumServiceClientImpl();
		QRY040062Result re = is.checkOut("GPRSLL_60YJB", "88022064903405");
		if(null != re){
			System.out.println(re.getResultCode()+"--getResultCode");
			System.out.println(re.getErrorCode()+"--getErrorCode");
			System.out.println(re.getErrorMessage()+"--getErrorMessage");
			
		}
		
	}
}
