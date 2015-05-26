package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.IChgProFamailyMsisdnService;
import com.xwtech.xwecp.service.logic.client_impl.common.IChgProTenDefMemberService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ChgProFamailyMsisdnServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ChgProTenDefMemberServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL010012Result;
import com.xwtech.xwecp.service.logic.pojo.DEL040017Result;

public class DEL010012Test 
{
	private static final Logger logger = Logger.getLogger(DEL010012Test.class);
	
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
		lic.setUserMobile("13813382424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		
		lic.setContextParameter(ic);
		
		IChgProTenDefMemberService service = new ChgProTenDefMemberServiceClientImpl();

		String mainNum = "13913814503";
		String subNum = "13813382424";
		int oprType = 0; //0、成员(邀请)1、成员(退出) 
		int chooseFlag = 0; // 0、立即 1、次日 2、次月, 不传按(立即)处理
		
		DEL010012Result result = service.chgProTenDefMember(mainNum, subNum, oprType, chooseFlag);
		logger.info(" ====== 开始返回参数 ======");
		
		logger.info(" ====== result ======" + result);
		if (result != null)
		{
			logger.info(" ====== getResultCode ======" + result.getResultCode());
			logger.info(" ====== getErrorCode ======" + result.getErrorCode());
			logger.info("========getErrorMessage==="+ result.getErrorMessage());
		}
	}
}
