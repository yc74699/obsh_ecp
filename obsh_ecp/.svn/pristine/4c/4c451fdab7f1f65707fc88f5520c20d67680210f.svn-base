package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IHBScoreFluxGprsService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.HBScoreFluxGprsServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040075Result;

/**
 * QRY040002、QRY040048、QRY040065 合成 
 * @author YangXQ
 * 2014-6-30
 */
public class QRY040075Test
{
	private static final Logger logger = Logger.getLogger(QRY040075Test.class);

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
		lic.setUserCity("14");
		lic.setUserMobile("13913814503");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913814503");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1423200000471569");
		lic.setContextParameter(ic);
		
		IHBScoreFluxGprsService query = new HBScoreFluxGprsServiceClientImpl();
		QRY040075Result result = query.hbScoreFluxGprs("13913814503", "1","201406");
		if (result != null)
		{
			//Qry040002Result
			logger.info(" ================================ Qry040002Result ================================");
			logger.info(" ====== getBalance =======" + result.getBalance());
			logger.info(" ====== getResultCode ========" + result.getResultCode());
			logger.info(" ====== getErrorCode =========" + result.getErrorCode());
			logger.info(" ====== getErrorMessage ======" + result.getErrorMessage());
			logger.info("");
			
			//Qry040048Result
			logger.info(" ================================ Qry040048Result ================================");
			logger.info(" ====== getResultCode ========" + result.getResultCode());
			logger.info(" ====== getErrorMessage ======" + result.getErrorMessage());
			logger.info(" ====== getNotebookTotalFlux =" + result.getIsUnlimitedBandwidth());
			logger.info(" ====== getNotebookUserFlux ==" + result.getIsPlayAt());
			logger.info(" ====== getTotalFlow =========" + result.getPackageFlow().getTotalFlow());
			logger.info(" ====== getUsedFlow ==========" + result.getPackageFlow().getUsedFlow());
			logger.info(" ====== getTotalFlow =========" + result.getHalfFlow().getTotalFlow());
			logger.info(" ====== getUsedFlow ==========" + result.getHalfFlow().getUsedFlow());
			logger.info("");
			
			//Qry040065Result
			logger.info(" ================================ Qry040065Result ================================");
			logger.info(" ====== getResultCode ========" + result.getResultCode());
			logger.info(" ====== getResultCode ========" + result.getErrorMessage());
			logger.info(" ====== getTwoNetFlux ========" + result.getTwoNetFlux());
			logger.info(" ====== getThreeNetFlux ======" + result.getThreeNetFlux());
			logger.info(" ====== getFourNetFlux =======" + result.getFourNetFlux());
			logger.info("");
		}
	}
}
