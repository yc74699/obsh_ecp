package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryRecommendBizNewService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryRecommendBizNewServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.CombineBean;
import com.xwtech.xwecp.service.logic.pojo.QRY060002Result;
import com.xwtech.xwecp.service.logic.pojo.RecommendBizInfo;

public class QRY060002Test {
	private static final Logger logger = Logger.getLogger(QRY060002Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "data_channel");
		//props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.100:10004/openapi_ecp/xwecp.do");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.73:10006/openapi_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.79:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.92:10008/sms_ecp/xwecp.do");
		props.put("platform.user", "lw");
		props.put("platform.password", "lw");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("");
		lic.setUserCity("14");
		lic.setUserMobile("15005156863");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15005156863");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		lic.setContextParameter(ic);
		
		IQueryRecommendBizNewService co = new QueryRecommendBizNewServiceClientImpl();
		
		QRY060002Result re = co.queryRecommendBiz("15005156863","", "1", "1","7");
		logger.info(" ====== 开始返回参数 ======"+re);
		if (re != null)
		{
			System.out.println(re.getResultCode());
			System.out.println(re.getErrorCode());
			System.out.println(re.getErrorMessage());
			System.out.println("size: "+re.getComBineLs().size());

		}
		
		for(CombineBean info : re.getComBineLs())
		{
			System.out.println(info.getScene());
		}
	}
}
