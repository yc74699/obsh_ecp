package com.xwtech.xwecp.test;

import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IMExchangeBizService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryExchangeBizListService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.MExchangeBizServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryExchangeBizListServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY030008Result;


public class QRY030008Test {
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		//props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		//props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.122.166:10004/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13665212424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13951919980");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13951919980");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1419200000541452");
		lic.setContextParameter(ic);
		IQueryExchangeBizListService ib= new QueryExchangeBizListServiceClientImpl();
		QRY030008Result result =ib.queryExchangeBizList();
		for(int i=0;i<result.getExchangeBizInfos().size();i++){
			System.out.println(result.getExchangeBizInfos().get(i).getExchangeCode());
		}
	}

}
