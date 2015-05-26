package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IConverTinterestService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ConverTinterestServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040079Result;

/**
 * 新增余额利息转流量兑换功能
 * @author YangXQ
 * 2014-7-15
 */
public class DEL040079Test
{
	private static final Logger logger = Logger.getLogger(DEL040079Test.class);

	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
//		props.put("platform.url", "http://10.32.65.238:8080/wap_ecp/xwecp.do");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13913814503");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913814503");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("user_id", "1423200000471569");
		lic.setContextParameter(ic);
		
		IConverTinterestService query = new ConverTinterestServiceClientImpl();
		DEL040079Result result = query.converTinterest( "1400000000000007","2200009007","1000","1400000000000007","11111");
		if (result != null)
		{
			logger.info(" ====== getResultCode ========" + result.getResultCode());
			logger.info(" ====== getErrorCode =========" + result.getErrorCode());
			logger.info(" ====== getErrorMessage ======" + result.getErrorMessage());
		}
	}
}
