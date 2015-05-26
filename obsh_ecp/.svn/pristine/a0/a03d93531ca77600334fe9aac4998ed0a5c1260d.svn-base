package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IGetEdtByCtrService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.GetEdtByCtrServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.EdtinfoBean;
import com.xwtech.xwecp.service.logic.pojo.QRY050065Result;
    
public class QRY050065Test
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
		IGetEdtByCtrService service = new GetEdtByCtrServiceClientImpl();
		
		QRY050065Result result = service.getEdtByCtr("12", "12110519333144722","88011018946986");
		if (result != null)
		{
			System.out.println(" === re.getRetCode() === " + result.getRet_code());
			List<EdtinfoBean> edtInfo = result.getEdtInfo();
			for(EdtinfoBean bean : edtInfo){
				System.out.println(bean.getFilename());
				System.out.println(bean.getFilepath());
			}
		}
	}
}
