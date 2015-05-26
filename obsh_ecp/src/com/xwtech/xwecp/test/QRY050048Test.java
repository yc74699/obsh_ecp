package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICheckNetIntsall;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CheckNetIntsallClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050048Result;

/**
 * 
 * @author 杨可帆
 * 
 * 创建于：Jun 22, 2011
 * 描述：手机宽带查询服务
 */
public class QRY050048Test
{
	private static final Logger logger = Logger.getLogger(QRY050048Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1:80/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13952395959");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13952395959");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13952395959");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("ddrCity", "14");  //2056200011182291
		ic.addContextParameter("userMsisdn", "15949152677");
		lic.setContextParameter(ic);
		
		ICheckNetIntsall co = new CheckNetIntsallClientImpl();
		//动感地带 13401312424  brand_id：11 city_id：17 user_id：1738200005062065
		//全球通 13913032424 user_id：1419200008195160
		QRY050048Result re = co.checkNetIntsall("14", "13813382424");
//		logger.info(" ====== 开始返回参数 ======");
//		if (re != null)
//		{
//			logger.info(" ====== getResultCode ======" + re.getRetCode());
//			
//		}
	}
}
