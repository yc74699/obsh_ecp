package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQryPlanValidDateService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QryPlanValidDateServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY070008Result;

public class QRY070008Test {
	private static final Logger logger = Logger.getLogger(QRY070007Test.class);

	public static void main(String[] args) throws Exception {
		// 初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "terminal_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8081/obsh_ecp/xwecp.do");
		// props.put("platform.url",
		// "http://10.32.229.80:10008/sms_ecp/xwecp.do");
		// props.put("platform.url",
		// "http://10.32.122.152:10004/obsh_ecp/xwecp.do");
		// props.put("platform.url",
		// "http://10.32.65.238:8081/sms_ecp/xwecp.do");
		// props.put("platform.url",
		// "http://10.32.229.86:8080/openapi_ecp/xwecp.do");
		props.put("platform.user", "tyl");
		props.put("platform.password", "tyl");

		XWECPLIClient client = XWECPLIClient.createInstance(props);
		// 逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("");
		lic.setUserBrand("");
		lic.setUserCity("12");
		lic.setUserMobile("13505230001");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13505230001");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");

		lic.setContextParameter(ic);

		IQryPlanValidDateService co = new QryPlanValidDateServiceClientImpl();
		QRY070008Result re = co.qryPlanValidDate(12,13505230001l,"120100013835");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null) {
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorCode() ======" + re.getErrorCode());
			logger.info(" ====== getErrorMessage ======"+ re.getErrorMessage());
			logger.info(" ====== getRet_code() ======" + re.getRet_code());
			logger.info(" ====== getAccessId() ======" + re.getAccessId());
			logger.info(" ====== getBossCode() ======" + re.getBossCode());
			logger.info(" ====== getNewprivenddate() ======" + re.getNewprivenddate());
			logger.info(" ====== getNewprivid() ======" + re.getNewprivid());
			logger.info(" ====== getNewprivname() ======" + re.getNewprivname());
			logger.info(" ====== getNewprivstartdate() ======" + re.getNewprivstartdate());
			logger.info(" ====== getOldprivenddate() ======" + re.getOldprivenddate());
			logger.info(" ====== getOldprivid() ======" + re.getOldprivid());
			logger.info(" ====== getOldprivname() ======" + re.getOldprivname());
		}
	}
}
