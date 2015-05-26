package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryFluxHisService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryFluxHisServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040071Result;
/**
 * 流量账单-套餐使用历史趋势查询
 * 
 * @author taogang
 * 2014-04-08
 */
public class QRY040071Test
{
	private static final Logger logger = Logger.getLogger(QRY040071Test.class);
	
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
		lic.setUserMobile("13773652424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13773652424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "20");
		ic.addContextParameter("ddr_city", "20");
		lic.setContextParameter(ic);
		IQueryFluxHisService co = new QueryFluxHisServiceClientImpl();
		QRY040071Result re = co.qryFluxHistory("201302","201304");
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
