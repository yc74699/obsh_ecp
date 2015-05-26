package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IMoveIntegralService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.MoveIntegralServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL010025Result;

/**
 * 积分转增接口
 * @author YangXQ
 * 2015-1-7
 */
public class DEL010025Test
{
	private static final Logger logger = Logger.getLogger(DEL010025Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel"); 
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");  
//		props.put("platform.url", "http://10.32.229.69:10006/mall_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8182/obsh_ecp/xwecp.do"); 
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");

		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12"); 
		
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12"); 			
		lic.setContextParameter(ic); 
		
		IMoveIntegralService co = new MoveIntegralServiceClientImpl();	
        //入参：  
		DEL010025Result re = co.moveIntegral(0,"13505249904","1299400007817763",1,"18251284535","1214300000892629",2,"积分转增",100);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getRet_code()   === " + re.getRet_code());
			logger.info(" === re.getRet_msg()    === " + re.getRet_msg()); 
		}
	}
}
