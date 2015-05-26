package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IUpdatezxrworderpicService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.UpdatezxrworderpicServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL010024Result;

/**
 * 在线入网订单实名制图片更新接口
 * @author YangXQ
 * 2014-10-24
 */
public class DEL010024Test
{
	private static final Logger logger = Logger.getLogger(DEL010024Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "terminal_channel"); 
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");  
//		props.put("platform.url", "http://10.32.229.69:10006/mall_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8182/obsh_ecp/xwecp.do"); 
		props.put("platform.user", "tyl");
		props.put("platform.password", "tyl");

		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12"); 
		lic.setUserMobile("");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "");	
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12"); 
		ic.addContextParameter("user_id", "");   			
		lic.setContextParameter(ic);//13641582424 - 2157200003124230
		IUpdatezxrworderpicService co = new UpdatezxrworderpicServiceClientImpl();
		
        //入参：  订单号,地区编码,本人手持身份证内容,身份证正面内容,身份证反面内容,本人手持身份证格式,身份证正面格式,身份证反面格式  
		DEL010024Result re = co.updatezxrworderpic("212201408122502554136","12","1","1","1","jpg","jpg","jpg");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== " + re.getResultCode());
			logger.info(" ====== " + re.getErrorMessage());
			logger.info(" ====== " + re.getErrorCode());
			
			logger.info(" === re.getRet_code()   === " + re.getRet_code());
			logger.info(" === re.getRet_msg()    === " + re.getRet_msg()); 
		}
	}
}
