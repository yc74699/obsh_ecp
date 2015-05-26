package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IPayBySCService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.PayBySCServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040006Result;

/**
 * 商城币充值卡充值
 * @author YangXQ
 * 2014-07-04
 */
public class DEL040006Test
{
	private static final Logger logger = Logger.getLogger(DEL040006Test.class);
	
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
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13615141156");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13615141156");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13615141156");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "13615141156");  
		lic.setContextParameter(ic);
		
		IPayBySCService co = new PayBySCServiceClientImpl();
		
		/**
		 * 手机号码  \ 被充值手机号码   \ 充值卡号码   \  1：为自己 2：为他人 
		 */
		DEL040006Result re = co.payBySC("13615141156", "13615141156", "11324687692659990","1");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getResultCode() ===== " + re.getResultCode());
			logger.info(" === re.getErrorCode() ====== " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
		}
	}
}
