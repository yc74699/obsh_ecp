package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICCEIQryWriteCardStatusService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CCEIQryWriteCardStatusServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040092Result;

public class QRY040092Test {
	private static final Logger logger = Logger.getLogger(QRY040092Test.class);
	public static void main(String[] args)  throws Exception{
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
		lic.setUserCity("12");
		lic.setUserMobile("13515248887");
		lic.setUserMobile("13515248887");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13515248887");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		lic.setContextParameter(ic);
		/**
		 * <region>12</region>
<imsi>460027620215619</imsi>
<temptel></temptel>
<substel>15896170007</substel>
<rectype>KHHLINST</rectype>

		 */
		ICCEIQryWriteCardStatusService service = new CCEIQryWriteCardStatusServiceClientImpl();
		QRY040092Result re = service.qryWiterCardStatus("12", "460027620215640", "", "18606020687", "KHHLINST");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{	
			/**
			 * private String status;
	private String newimsi;
	private String newiccid;
	private String blankcardno;
	private String explain;
			 */
			System.out.println("====== getStatus ====================" + re.getStatus());
			System.out.println("====== getNewimsi ====================" + re.getNewimsi());
			System.out.println("====== getNewiccid ====================" + re.getNewiccid());
			System.out.println("====== getBlankcardno ====================" + re.getBlankcardno());
			System.out.println("====== getExplain ====================" + re.getExplain());
			System.out.println("====== getExplain ====================" + re.getRetcode());
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
		}
	}
}
