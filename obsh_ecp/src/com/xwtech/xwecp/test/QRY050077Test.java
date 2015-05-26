package com.xwtech.xwecp.test;

import com.xwtech.xwecp.service.logic.pojo.QRY050077Result;
import com.xwtech.xwecp.service.logic.client_impl.common.IchkForNetByNameService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ChkForNetByNameServiceClientImpl;
import org.apache.log4j.Logger;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import java.util.Properties;

public class QRY050077Test
{
	private static  Logger logger = Logger.getLogger(QRY050077Test.class);
	public static void main(String[] args) throws Exception
	{
	
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.166:10004/obsh_ecp/xwecp.do");
		//		props.put("platform.user", "xl");
		//		props.put("platform.password", "xl");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13770472424");
		lic.setUserMobile("13770472424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13770472424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		lic.setContextParameter(ic);
		String certId = "IdCard";
		String name = "321181195108065974";
		String certType = "23大版本测试";//0，校验通过增加返回值    
                    		//1，校验不通过，
                    		//101是：客户证件号在系统内已有两个及以上号码，并且登记姓名不一致 
                    		//102是：客户本次输入的姓名与系统内该证件号码对应的姓名不一致
		IchkForNetByNameService chkForNetByNameService= new ChkForNetByNameServiceClientImpl();
		QRY050077Result re = chkForNetByNameService.chkForNetByNameService(certId,name,certType);
			System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
			System.out.println("++++++++++++++++"+re.getBookId());
		}
	}
}