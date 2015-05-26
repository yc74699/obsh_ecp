package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQryNetcardChgService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QryNetcardChgServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040069Result;
/**
 * 4G换卡结果查询
 * 
 * @author xufan
 * 2014-04-08
 */
public class QRY040069Test
{
	private static final Logger logger = Logger.getLogger(QRY040069Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "wap_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp_test/xwecp.do");
		props.put("platform.user", "xl");
		props.put("platform.password", "xl");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13805178294");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13805178294");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		lic.setContextParameter(ic);
		IQryNetcardChgService co = new QryNetcardChgServiceClientImpl();
		QRY040069Result re = co.qryNetcardChg("13805178294","","");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== 返回结果码 ======" + re.getResultCode());
			System.out.println(" ====== 结果 ======" + re.getChgsimcard_result());
			System.out.println(" ====== 错误编码 ======" + re.getErrorCode());
			System.out.println(" ====== 错误信息 ======" + re.getErrorMessage());
			
		}
	}
}
