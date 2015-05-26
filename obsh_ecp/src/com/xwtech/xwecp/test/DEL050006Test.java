package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICancelYkdhService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CancelYkdhService;
import com.xwtech.xwecp.service.logic.pojo.DEL050006Result;
public class DEL050006Test
{
	/*
	 * DEL050006测试类
	 */
	private static final Logger logger = Logger.getLogger(DEL050006Test.class);
	
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
		lic.setUserBrand("12");
		lic.setUserCity("14");
		lic.setUserMobile("13915170950");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13915170950");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("user_id", "1208200008843825");
		
		lic.setContextParameter(ic);
		
		ICancelYkdhService cancel = new CancelYkdhService();
		
		//4，表示退订所有副号，如果只退订其中一个，请传递序号1，2，3中的1个
		DEL050006Result re = cancel.cancelYkdh("13915170950", "4");
		logger.info(" ====== 开始返回参数 ======");
		if(re != null)
		{
			logger.info("====== getRet_code ===" + re.getRet_code());
			logger.info("====== getRet_msg ===" + re.getRet_msg());
		}
	}
}
