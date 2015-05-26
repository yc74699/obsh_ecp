package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryUserIncrementsService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryUserIncrementsServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040023Result;
import com.xwtech.xwecp.service.logic.pojo.UserIncrements;

public class QRY040023Test
{
	private static final Logger logger = Logger.getLogger(DEL040023Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
		props.put("platform.url", "http://127.0.0.1:8080/sms_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13913814503");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913814503");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		ic.addContextParameter("user_id", "1423200000471569");
		
		lic.setContextParameter(ic);
		
		IQueryUserIncrementsService co = new QueryUserIncrementsServiceClientImpl();
		
		QRY040023Result re = co.queryUserIncrements("13913814503","1");
		//QRY040023Result re = co.queryUserIncrementsList("13775546134");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			
			if (null != re.getUserIncrementsList()&& re.getUserIncrementsList().size() > 0)
			{
				logger.info(" ====== re.getUserIncrementsList.size() ====== " + re.getUserIncrementsList().size());
				for (UserIncrements dt : re.getUserIncrementsList())
				{
					logger.info(" ====== getRegName ======" +dt.getRegName());
					logger.info(" ====== getRegSpId ======" + dt.getRegSpId());
					logger.info(" ====== getRegSpName ======" + dt.getRegSpName());
					logger.info(" ====== getRegOrderId ======" + dt.getRegOrderId());
					logger.info(" ====== getRegTypeClass ======" + dt.getRegTypeClass());
					logger.info(" ====== getRegMonthFee ======" + dt.getRegMonthFee());
					logger.info(" ====== getRegBeginDate ======" + dt.getRegBeginDate());
					logger.info(" ====== getPayFlag ======" + dt.getPayFlag());
					logger.info(" ===================================");
				
					
				}
			}
		}
	}
	
}
