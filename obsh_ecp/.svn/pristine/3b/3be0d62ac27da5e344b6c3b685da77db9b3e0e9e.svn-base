package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryPayHistoryService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryPayHistoryServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.PayHistory;
import com.xwtech.xwecp.service.logic.pojo.QRY010008Result;
import com.xwtech.xwecp.service.logic.pojo.TerminalInventoryBean;

public class QRY010008Test
{
	private static final Logger logger = Logger.getLogger(QRY010008Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.166:10006/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("11");
		lic.setUserMobile("18795435560");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "18795435560");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "11");
		ic.addContextParameter("ddr_city", "11");
		
		lic.setContextParameter(ic);
		//
		List<TerminalInventoryBean> list =  new ArrayList<TerminalInventoryBean>();
	
		
		IQueryPayHistoryService co = new QueryPayHistoryServiceClientImpl();
		QRY010008Result re = co.queryPayHistory("18795435560", "20130901", "20130909");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== 返回结果码 ======" + re.getResultCode());
			System.out.println(" ====== 数量 ======" + re.getPayHistory().size());
			System.out.println(" ====== 错误编码 ======" + re.getErrorCode());
			System.out.println(" ====== 错误信息 ======" + re.getErrorMessage());
			
			if (null != re.getPayHistory() && re.getPayHistory().size() > 0)
			{
				for (PayHistory dt : re.getPayHistory())
				{
					System.out.println(" ====== --  ======" + dt.getPayMode());
					System.out.println(" ====== --  ======" + dt.getPayMoney());
					System.out.println(" ====== --  ======" + dt.getPayTime());
					System.out.println(" ====== --  ======" + dt.getPayType());
					System.out.println(" ====== --  ======" + dt.getState());
				 }
					
				
			}
		}
	}
}
