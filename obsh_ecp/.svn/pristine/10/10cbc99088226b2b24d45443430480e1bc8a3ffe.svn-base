package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQuerConcerTinterestService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QuerConcerTinterestServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040077Result;

/**
 * 新增兑换信息查询
 * @author YangXQ
 * 2014-7-15
 */
public class QRY040077Test
{
	private static final Logger logger = Logger.getLogger(QRY040077Test.class);

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
		
		IQuerConcerTinterestService query = new QuerConcerTinterestServiceClientImpl();
		QRY040077Result result = query.queryConcerTinterest("1423200000471569", "20140624","20140627");
		if (result != null)
		{
			logger.info(" ====== getResultCode ========" + result.getResultCode());
			logger.info(" ====== getErrorCode =========" + result.getErrorCode());
			logger.info(" ====== getErrorMessage ======" + result.getErrorMessage());
		}
	}
}
