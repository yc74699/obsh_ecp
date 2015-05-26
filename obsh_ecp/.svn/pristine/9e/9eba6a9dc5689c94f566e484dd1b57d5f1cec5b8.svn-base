package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryCgetschoolProdinfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryCgetschoolProdinfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY610041Result;

/**
 * 新生随机码查询产品信息接口
 * @author YangXQ
 * 2015-04-14
 */
public class QRY610041Test {
	private static final Logger logger = Logger.getLogger(QRY610041Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");  
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");  
		props.put("platform.user", "jhr"); 
		props.put("platform.password", "jhr"); 
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand(""); 
		lic.setUserCity("12"); 
		lic.setUserMobile("");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "");
		ic.addContextParameter("route_type", "1");           
		ic.addContextParameter("route_value", "12"); 
		lic.setContextParameter(ic);
		 
		IQueryCgetschoolProdinfoService co = new QueryCgetschoolProdinfoServiceClientImpl();
		/**新生随机码*/
		QRY610041Result re = co.queryCgetschoolinfo("");
		
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());			
			logger.info(" ====== SCHOOLCODE ======" + re.getSCHOOLNAME());
			logger.info(" ====== SCHOOLNAME ======" + re.getSCHOOLNAME());
			logger.info(" ====== Prodinfo ======" + re.getProdinfo());
		}
	}
}
