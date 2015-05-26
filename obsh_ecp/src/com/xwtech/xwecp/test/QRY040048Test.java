package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryGPSFluxInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryGPSFluxInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040048Result;

public class QRY040048Test {
	
	private static final Logger logger = Logger.getLogger(QRY040048Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "icc_channel");
		//props.put("platform.url", "http://10.32.229.74:8080/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10006/obsh_ecp/xwecp.do");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
		props.put("platform.user", "yt");
		props.put("platform.password", "yt");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13813382746");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13813382746");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1419200010139095");
		
		lic.setContextParameter(ic);
		
		IQueryGPSFluxInfoService co = new QueryGPSFluxInfoServiceClientImpl();
		QRY040048Result re = co.queryGPSFluxInfo("13813382746", "201301");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
			System.out.println(" ====== getNotebookTotalFlux ======" + re.getIsUnlimitedBandwidth());
			System.out.println(" ====== getNotebookUserFlux ======" + re.getIsPlayAt());
			System.out.println(" ====== getPackageFlow  getTotalFlow======" + re.getPackageFlow().getTotalFlow());
			System.out.println(" ====== getPackageFlow  getUsedFlow======" + re.getPackageFlow().getUsedFlow());
			System.out.println(" ====== getHalfFlow  getTotalFlow======" + re.getHalfFlow().getTotalFlow());
			System.out.println(" ====== getHalfFlow  getUsedFlow======" + re.getHalfFlow().getUsedFlow());
		}
	}
}