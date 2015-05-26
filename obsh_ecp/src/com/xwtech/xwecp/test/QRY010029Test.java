package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryFmyNewWestSpandProduseService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryFmyNewWestSpandProduseServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.PkgInfoBean;
import com.xwtech.xwecp.service.logic.pojo.QRY010029Result;

public class QRY010029Test {
	private static final Logger logger = Logger.getLogger(QRY010029Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13951692340");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13951692340");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		
		lic.setContextParameter(ic);
		
		IQueryFmyNewWestSpandProduseService co = new QueryFmyNewWestSpandProduseServiceClientImpl();
		QRY010029Result re = co.queryNewWestFmySpandProduse("1419200000479364","201309",2,"1419200000479364");
		System.out.println(re.getSubSid());
		System.out.println(re.getCycle());
		for(PkgInfoBean pk: re.getPkgInfoList()){
			System.out.println(pk.getFreeitem_desc());
			System.out.println("----------------");
		}
		for(List<String> r :re.getDateStrList()){
			for(String s:r){
				System.out.println(s);
				
				}
			System.out.println("-----------------");
		}
	
		System.out.println(re.getResultCode());
	}
}
