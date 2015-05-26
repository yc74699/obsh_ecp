package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IPackageChangeService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.PackageChangeServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040023Result;

public class DEL040023Test
{
	private static final Logger logger = Logger.getLogger(DEL040023Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13601400067");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913814503");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		ic.addContextParameter("user_id", "1423200000471569");
		
		lic.setContextParameter(ic);
		
		IPackageChangeService co = new PackageChangeServiceClientImpl();
		//全球通数据
//		QRY050028Result re = co.queryPackageDepend("13913814503", 
//				"100063", new String[]{"1031","1031"}, new String[]{"1741","1742"},  new String[]{"0","1"});
		
		//动感地带数据
//		DEL040023Result re = co.packageChange("13805157824", "14", "100070", "1018,1018", 
//				"4991,1146", "0,1", "", "", "");

		//全球通数据
//		DEL040023Result re = co.packageChange("13913814503", "14",
//				"100063","1031,1031","-1,-1", "1741,1743",  "0,1", "", "", "");
		
		DEL040023Result re = co.packageChange("13805157824", "NJDQ",
				"100070","TCBG_DGDDYYTC","TCBG_DGDDWLTC","1018");
	
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			}
		}
	
}
