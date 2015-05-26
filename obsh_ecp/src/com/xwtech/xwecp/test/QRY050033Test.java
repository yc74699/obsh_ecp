package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryMktAct;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryMktActClientImpl;
import com.xwtech.xwecp.service.logic.pojo.ActivityBean;
import com.xwtech.xwecp.service.logic.pojo.QRY050033Result;

public class QRY050033Test {
	private static final Logger logger = Logger.getLogger(QRY050033Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13770482424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13770482424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		
		//ic.addContextParameter("user_id", "2055200007046734");
		
		lic.setContextParameter(ic);
		IQueryMktAct queryMktAct = new QueryMktActClientImpl();
		
		QRY050033Result result = queryMktAct.queryMktAct("13770482424");
		
		if(result != null){
			for(ActivityBean bean : result.getActivityBean()){
				System.out.println(bean.getActName());
				System.out.println(bean.getBizId());
				System.out.println(bean.getBizName());
			}
		}
	}
}