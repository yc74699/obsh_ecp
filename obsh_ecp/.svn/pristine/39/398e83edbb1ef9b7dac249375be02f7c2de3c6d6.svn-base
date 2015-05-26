package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryEmailBizStateService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryEmailBizStateServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY020004Result;
import com.xwtech.xwecp.service.logic.pojo.SendMailList;

public class QRY020004Test
{
	private static final Logger logger = Logger.getLogger(QRY020004Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1:8080/xwecp/xwecp.do");
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
		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13813382424");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1419200008195116");
		
		lic.setContextParameter(ic);
		
		IQueryEmailBizStateService co = new QueryEmailBizStateServiceClientImpl();
		QRY020004Result re = co.queryEmailBizState("13813382424");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
			
			if (null != re.getSendMailList() && re.getSendMailList().size() > 0)
			{
				for (SendMailList dt : re.getSendMailList())
				{
					logger.info(" ====== getSendType ======" + dt.getSendType());
					logger.info(" ================== ");
				}
			}
			
			logger.info(" ====== getSendState ======" + re.getSendState());
			logger.info(" ====== getEmail ======" + re.getEmail());
			logger.info(" ====== getRecvFlag ======" + re.getRecvFlag());
		}
	}
}
