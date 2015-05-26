package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IB2BloginService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryB2BloginServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040100Result;

/**
 * B2B登录接口
 * @author YangXQ
 * 2015-03-17
 */
public class QRY040100Test {
	private static final Logger logger = Logger.getLogger(QRY040100Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");  
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");  
		props.put("platform.user", "jhr"); 
		props.put("platform.password", "jhr"); 
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand(""); 
		lic.setUserCity("11"); 
		lic.setUserMobile("");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "");
		ic.addContextParameter("route_type", "1");           
		ic.addContextParameter("route_value", "12"); 
		lic.setContextParameter(ic);
		 
		IB2BloginService co = new QueryB2BloginServiceClientImpl();
		/** 请求来源, 操作员编码, 登陆密码*/
		QRY040100Result re = co.B2Blogin("","12900004","12312312");
		
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());			
			logger.info(" ====== oper_id ======" + re.getOper_id());
			logger.info(" ====== oper_name ======" + re.getOper_name());
			logger.info(" ====== Oper_mobile ======" + re.getOper_mobile());
			logger.info(" ====== Region_num ======" + re.getRegion_num());
			logger.info(" ====== Region_name ======" + re.getRegion_name());
			logger.info(" ====== Area_num ======" + re.getArea_num());
			logger.info(" ====== Area_name ======" + re.getArea_name());
			logger.info(" ====== oper_type ======" + re.getOper_type());
			logger.info(" ====== role_id ======" + re.getRole_id());
			logger.info(" ====== role_name ======" + re.getRole_name());
			logger.info(" ====== outlet_id ======" + re.getOutlet_id());
			logger.info(" ====== outlet_name ======" + re.getOutlet_name());
			logger.info(" ====== distributors_rogid ======" + re.getDistributors_rogid());
			logger.info(" ====== distributors_name ======" + re.getDistributors_name());
			logger.info(" ====== channel_typename ======" + re.getChannel_typename());
			logger.info(" ====== channel_type ======" + re.getChannel_type());
			logger.info(" ====== contact ======" + re.getContact());
			logger.info(" ====== contact_number ======" + re.getContact_number());
			logger.info(" ====== address ======" + re.getAddress());
			logger.info(" ====== email ======" + re.getEmail());
			logger.info(" ====== chain ======" + re.getChain());
		}
	}
}
