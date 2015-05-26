package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryUserTribeService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryUserTribeServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.MTribe;
import com.xwtech.xwecp.service.logic.pojo.QRY050011Result;

public class QRY050011Test {
private static final Logger logger = Logger.getLogger(QRY040014TestApp.class);
	
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
		lic.setUserCity("14");
		lic.setUserMobile("13646272637");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13665212424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "23");
		ic.addContextParameter("ddr_city", "23");
		
		ic.addContextParameter("user_id", "2371200004318871");
		
		lic.setContextParameter(ic);
		
		IQueryUserTribeService service = new QueryUserTribeServiceClientImpl();
		QRY050011Result res = service.queryUserTribe("13665212424");
		List<MTribe> mTribeList = res.getMTribeList();
		MTribe mTribe = null;
		for(int i = 0 ; i < mTribeList.size() ; i ++)
		{
			mTribe = mTribeList.get(i);
			System.out.println(mTribe.getTribeId() + "--" + mTribe.getTribeName() + "--" + mTribe.getState());
		}
		
		
	}
	
	
}
