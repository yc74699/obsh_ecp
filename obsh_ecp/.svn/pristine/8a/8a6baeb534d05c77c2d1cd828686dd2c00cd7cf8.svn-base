package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryMobileTypeService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryMobileTypeServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.MobileType;
import com.xwtech.xwecp.service.logic.pojo.QRY050008Result;

public class MobileTypeTestApp {
	
   
	public static void main(String[] args)
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1:80/ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13601400067");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13913032424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13913032424");
		ic.addContextParameter("ddr_city", "20");
		
		ic.addContextParameter("user_id", "2056200011182291");
		
		lic.setContextParameter(ic);
		
		IQueryMobileTypeService service = new QueryMobileTypeServiceClientImpl();
		try {
			QRY050008Result qRY050008Result = service.queryMobileType("28");
			
			List<MobileType> mobileType = qRY050008Result.getMobileType();
			
			for(int i = 0 ; i < mobileType.size() ; i ++)
			{
				System.out.println("类型ID:" + mobileType.get(i).getTypeId() + " 类型名称:" + mobileType.get(i).getTypeName());
			}
			
		} catch (LIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
}
