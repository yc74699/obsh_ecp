package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryProdAttrByProdIdService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryProdAttrByProdIdServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050066Result;

public class QRY050066Test {
	private static final Logger logger = Logger.getLogger(QRY050066Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13776440824");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13776440824");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13776440824");
		ic.addContextParameter("ddr_city", "13");
		
//		ic.addContextParameter("user_id", "1419200008195160");
		
		lic.setContextParameter(ic);
		
		IQueryProdAttrByProdIdService co = new QueryProdAttrByProdIdServiceClientImpl();
		QRY050066Result re = co.queryProdAttrByProdId("13776440824", "2610620005","12");
		
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			
//			if(re.getAttributeInfo()!=null)
//			{
//				logger.info(" ====== getAttrId ======" + re.getAttributeInfo().getAttrId());
//				logger.info(" ====== getAttrName ======" + re.getAttributeInfo().getAttrName());
//				logger.info(" ====== getMustFlag ======" + re.getAttributeInfo().getMustFlag());
//				logger.info(" ====== getOptValue ======" + re.getAttributeInfo().getOptValue());
//				logger.info(" ====== getDefValue ======" + re.getAttributeInfo().getDefValue());
//
//			}
			
		}
	}
}