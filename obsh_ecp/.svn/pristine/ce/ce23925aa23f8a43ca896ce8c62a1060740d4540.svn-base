package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQryGetBaseOrgInfoByTelnumService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QryGetBaseOrgInfoByTelnumServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.CorginfoDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY040093Result;

public class QRY040093Test {
	private static final Logger logger = Logger.getLogger(QRY040093Test.class);
	public static void main(String[] args)  throws Exception{
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
		lic.setUserMobile("13515248850");
		lic.setUserMobile("13515248850");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13515248850");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		lic.setContextParameter(ic);
		
		IQryGetBaseOrgInfoByTelnumService service = new QryGetBaseOrgInfoByTelnumServiceClientImpl();
		QRY040093Result re = service.qryBaseOrgInfoByTelnum("12", "13515248850");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{	
			System.out.println("====== getRetCode=======" +  re.getRetCode());
			System.out.println("====== getRetMsg=======" +  re.getRetMsg());
			for(CorginfoDetail temp : re.getList()){
				System.out.println("===== getCname ======" + temp.getCname());
				System.out.println("===== getCorgaName ======" + temp.getCorgaName());
				System.out.println("===== getTelnum ======" + temp.getTelnum());
			}
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
		}
	}
}
