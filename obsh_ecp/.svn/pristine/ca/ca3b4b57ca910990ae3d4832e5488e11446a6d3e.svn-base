package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryFluxHisCHService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryFluxHisCHServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040113Result;

public class QRY040113Test {
private static final Logger logger = Logger.getLogger(QRY040113Test.class);
	
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
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13852280375");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13852280375");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("context_user_id", "1422200000198380");
		ic.addContextParameter("user_id", "1422200000198380");
		lic.setContextParameter(ic);
		IQueryFluxHisCHService co = new QueryFluxHisCHServiceClientImpl();
		QRY040113Result re = co.qryFluxHistoryCH("201501","201505","1");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== 返回结果码 ======" + re.getResultCode());
			System.out.println(" ====== 错误编码 ======" + re.getErrorCode());
			System.out.println(" ====== 错误信息 ======" + re.getErrorMessage());
			System.out.println(" ====== size ======" + re.getPkgFluxHisInfoList().size());
		}
	}
}
