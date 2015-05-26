package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryGoodsDetailByPkgIdService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryGoodsDetailByPkgIdServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.GoodsPkgInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY070007Result;

public class QRY070007Test {
	private static final Logger logger = Logger.getLogger(QRY070007Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "terminal_channel");
		//props.put("platform.url", "http://127.0.0.1/obsh_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.65.223:8081/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.80:10008/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.152:10004/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238:8081/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.86:8080/openapi_ecp/xwecp.do");
		props.put("platform.user", "tyl");
		props.put("platform.password", "tyl");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("");
		lic.setUserBrand("");
		lic.setUserCity("14");
		lic.setUserMobile("13913814503");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15312081165");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		
		
		lic.setContextParameter(ic);
		
		IQueryGoodsDetailByPkgIdService co = new QueryGoodsDetailByPkgIdServiceClientImpl();
		QRY070007Result re = co.queryGoodsDetailByPkgId("2512102601", "12100004287");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
			for(GoodsPkgInfo g : re.getGoodsPkg()){
				logger.info(" ====== g.getGoodsId() ======" + g.getGoodsId());
				logger.info(" ====== g.getGoodsName() ======" + g.getGoodsName());
				logger.info(" ====== g.getGoodsType() ======" + g.getGoodsType());
			}
			
//			logger.info(" ====== getAppDate ======" + re.getAppDate());
//			logger.info(" ====== getBrand ======" + re.getBrand());
//			logger.info(" ====== getCity ======" + re.getCity());
			
		}
	}
}
