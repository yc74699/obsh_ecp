package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IGetQryFreeByIdService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.GetQryFreeByIdServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050064Result;
import com.xwtech.xwecp.service.logic.pojo.QryContractInfoBean;
    
public class QRY050064Test
{
	
	public static void main(String[] args) throws Exception
	{
		
		String host = "http://127.0.0.1/js_ecp/xwecp.do";
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", host);
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		 
		@SuppressWarnings("unused")
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("");
		lic.setUserBrand("");
		lic.setUserCity("12");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		
		lic.setContextParameter(ic);
		IGetQryFreeByIdService service = new GetQryFreeByIdServiceClientImpl();
		
		QRY050064Result result = service.getQryFreeById("12", "12110519333144722");
		if (result != null)
		{
			System.out.println(" === re.getRetCode() === " + result.getRet_code());
			List<QryContractInfoBean> qryContractInfo = result.getQryContractInfo();
			for(QryContractInfoBean bean : qryContractInfo){
				System.out.println(bean.getInvoiceitemid());
				System.out.println(bean.getInvoicecontent());
			}
		}
	}
}
