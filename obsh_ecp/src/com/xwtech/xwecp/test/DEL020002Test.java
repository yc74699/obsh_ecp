package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.IMExchangeBizService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.MExchangeBizServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL020002Result;

public class DEL020002Test {
private static final Logger logger = Logger.getLogger(QRY040004Test.class);
	
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
		lic.setUserMobile("13665212424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13665212424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13665212424");
		ic.addContextParameter("ddr_city", "23");
		
		ic.addContextParameter("user_id", "2056200011182291");
		
		lic.setContextParameter(ic);
		
		IMExchangeBizService service = new MExchangeBizServiceClientImpl();
		
		DEL020002Result result = service.mExchangeBiz("13665212424", "5000", "1", 1, 0,15);
//(mobile, modle.getExtNo(), modle
//				.getExtChangeTypeId(), modle.getNum(),
//				modle.getCloseFlag());
		logger.info(" ====== 开始返回参数 ======");
		if (result != null)
		{
			logger.info(" === re.getResultCode() === " + result.getResultCode());
			logger.info(" === re.getErrorCode() === " + result.getErrorCode());

			logger.info(" === re.getErrorMessage() === " + result.getErrorMessage());
		}
		
	}
	
	
	
}
