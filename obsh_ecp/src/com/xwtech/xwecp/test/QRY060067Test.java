package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryOrderForNetService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryOrderForNetServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.PreConInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY060067Result;


public class QRY060067Test {
	private static final Logger logger = Logger.getLogger(QRY060067Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		
		lic.setContextParameter(ic);
		
		IQueryOrderForNetService co = new QueryOrderForNetServiceClientImpl();
		QRY060067Result re = co.queryOrderForNet("13809032214", "20120101", "20120901");
		System.out.println(re.getResultCode());
		System.out.println(re.getBossCode());
		
		for(PreConInfo  r: re.getPreContractInfo()){
			System.out.println("订单号："+r.getOrderId());
			System.out.println("状态："+r.getStatus());
			System.out.println("金额："+r.getAmount());
		}
		
	}
}
