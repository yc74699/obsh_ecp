package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IScoreExchangeCoinService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ScoreExchangeCoinServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL020003Result;
import com.xwtech.xwecp.service.logic.pojo.ScoreChangeCoin;
    
public class DEL020003Test
{
	private static final Logger logger = Logger.getLogger(DEL020003Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片�?
		Properties props = new Properties();
		props.put("client.channel", "openapi_channel");
		props.put("platform.url", "http://127.0.0.1:8080/openApi_ecp/xwecp.do");
		props.put("platform.user", "fgh");
		props.put("platform.password", "fgh");
		 
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("�?�?/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("18");
		lic.setUserMobile("13815472424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13815472424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13815472424");
		//ic.addContextParameter("user_pwd", "7089F5FD852898A6");
		
		//ic.addContextParameter("user_id", "1419200008195160");
		
		lic.setContextParameter(ic);
		
		 List<ScoreChangeCoin> scoreChangeCoins = new ArrayList<ScoreChangeCoin>();
		 ScoreChangeCoin scc = new ScoreChangeCoin();
		 scc.setScoreChangeChangeNum("30");
		 scc.setScoreChangeFees("");
		 scc.setScoreChangeOeartingSrl("");
		 scc.setScoreChangeOScorepeartingSrl("");
		 scc.setScoreChangeRedeemedDate("");
		 scc.setScoreChangeRedeemedNumber("100");
		 scc.setScoreChangeRedeemedType("3");
		 scc.setScoreChangeUserId("");
		 scoreChangeCoins.add(scc);
		
		IScoreExchangeCoinService service = new ScoreExchangeCoinServiceClientImpl();
		DEL020003Result re = service.scoreExchangeCoin("", "1", "11", "13815472424", "", "", scoreChangeCoins);
		
		logger.info(" ====== �?始返回参�? ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorCode ======" + re.getErrorCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
		}
	}
}
