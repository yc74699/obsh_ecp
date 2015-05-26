package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryPayHistoryFlagService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryPayHistoryService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryPayHistoryFlagServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryPayHistoryServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.PayHistory;
import com.xwtech.xwecp.service.logic.pojo.QRY010008Result;
import com.xwtech.xwecp.service.logic.pojo.QRY010042Result;
import com.xwtech.xwecp.service.logic.pojo.TerminalInventoryBean;

public class QRY010042Test
{
	private static final Logger logger = Logger.getLogger(QRY010042Test.class);
	/**
	 * 电子渠道历史记录查询
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
//		props.put("platform.url", "http://10.32.65.67:8080/obsh_ecp_test/xwecp.do");
//		props.put("platform.url", "http://10.32.122.166:10006/obsh_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.65.64:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("17");
		lic.setUserMobile("18351002158");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "18351002158");
		ic.addContextParameter("route_type", "3");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		
		lic.setContextParameter(ic);
		//
		List<TerminalInventoryBean> list =  new ArrayList<TerminalInventoryBean>();
	
		
		IQueryPayHistoryFlagService co = new QueryPayHistoryFlagServiceClientImpl();
		QRY010042Result re = co.queryPayHistoryFlag("18351002158", "20140701", "20140720");
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
