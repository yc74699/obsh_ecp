package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryCcQryAddmarketService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryCcQryAddmarketServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY060004Result;

/**
 * 营销案可办理业务查询
 * @author YangXQ
 * 2015-04-15
 */
public class QRY060004Test {
	private static final Logger logger = Logger.getLogger(QRY060004Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
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
		lic.setUserCity("12");  
		
		InvocationContext ic = new InvocationContext(); 
		ic.addContextParameter("route_type", "1");            
		ic.addContextParameter("route_value", "12");  
		
		lic.setContextParameter(ic);
		 
		IQueryCcQryAddmarketService co = new QueryCcQryAddmarketServiceClientImpl();
		/**
		 * 地市路由
		 * 手机号码
		 * 活动编码
		 * 档次编码
		 * 渠道，网厅传4
		 */
//		如果无可办理的奖品信息，返回值ret_code返回-1，否则返回0
//		返回可用的奖品信息
		QRY060004Result re = co.queryCcQryAddmarket("12","13952307454","3001048126","300001508194","4");
//		奖品产品与现有产品互斥
//		QRY060004Result re = co.queryCcQryAddmarket("12","13605232127","3001056114","300001512172","19");
		
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());		 
		}
	}
}
