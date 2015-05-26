package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IGetUsrTransTemListService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.GetUsrTransTemListServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050051Result;
import com.xwtech.xwecp.service.logic.pojo.TransProTempleteBean;

public class QRY050051Test
{
	private static final Logger logger = Logger.getLogger(QRY050051Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("11");
		lic.setUserMobile("13770666945");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15189615211");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13913814503");
		ic.addContextParameter("ddr_city", "12");
		
		lic.setContextParameter(ic);
		//

		
//		terminalInventoryBean bean3 = new terminalInventoryBean();
//		bean3.setMobilebrand("NOKIA");
//		bean3.setMobiletype("8210");
//		list.add(bean3);
		
		IGetUsrTransTemListService co = new GetUsrTransTemListServiceClientImpl();
		QRY050051Result re = co.getUsrTransTemList("13851347124", "1000100158", "14");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== 返回结果码 ======" + re.getResultCode());
			System.out.println(" ====== listsize ======" + re.getTransProTempleteList().size());
			System.out.println(" ====== 错误编码 ======" + re.getErrorCode());
			System.out.println(" ====== 错误信息 ======" + re.getErrorMessage());
			
			if (null != re.getTransProTempleteList() && re.getTransProTempleteList().size() > 0)
			{
				for (TransProTempleteBean dt : re.getTransProTempleteList())
				{
					System.out.println(" ====== -- 模板号 ======" + dt.getTemp_id());
					System.out.println(" ====== -- 模板名称 ======" + dt.getTemp_name());
				 }
					
				
			}
		}
	}
}
