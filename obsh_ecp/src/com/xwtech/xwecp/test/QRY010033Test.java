package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryreinforceinfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryReinforceInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY010033Result;
import com.xwtech.xwecp.service.logic.pojo.QryDetail;
/**
 * 全国充值卡查询与查询请求test
 * 
 * @author xufan
 * 2014-04-17
 */
public class QRY010033Test
{
	private static final Logger logger = Logger.getLogger(QRY010033Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
		props.put("platform.url", "http://10.32.65.67:8080/obsh_ecp_test/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8080/sms_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.167:10005/obsh_ecp/xwecp.do");

		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("18351002158");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "18351002158");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		lic.setContextParameter(ic);
		IQueryreinforceinfoService co = new QueryReinforceInfoServiceClientImpl();
//		QRY010033Result re = co.qryreInforceInfo("","20140417165073111243",2);
		QRY010033Result re = co.qryreInforceInfo("11073104000292374","",1);
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== 返回结果码 ======" + re.getResultCode());
			System.out.println(" ====== 错误编码 ======" + re.getErrorCode());
			System.out.println(" ====== 错误信息 ======" + re.getErrorMessage());
			
		}
	}
}
