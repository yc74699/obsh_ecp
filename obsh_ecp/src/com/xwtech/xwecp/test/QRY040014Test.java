package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryUserEPackageService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryUserEPackageServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.EPkgDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY040014Result;
import com.xwtech.xwecp.service.logic.pojo.TerminalInventoryBean;

public class QRY040014Test
{
	private static final Logger logger = Logger.getLogger(QRY040014Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
		//props.put("platform.url", "http://10.32.229.74:8080/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		props.put("platform.url", "http://127.0.0.1:8080/sms_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13913814503");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913814503");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1423200000471569");
		
		lic.setContextParameter(ic);
		//
		List<TerminalInventoryBean> list =  new ArrayList<TerminalInventoryBean>();
	
		
		IQueryUserEPackageService co = new QueryUserEPackageServiceClientImpl();
		QRY040014Result re = co.queryUserEPackage("13913814503");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== 返回结果码 ======" + re.getResultCode());
			System.out.println(" ====== list size ======" + re.getEPkgDetail().size());
			for(EPkgDetail pd : re.getEPkgDetail())
			{
				System.out.println(" ====== getPkgCode ======" + pd.getPkgCode());
				System.out.println(" ====== getPkgName ======" + pd.getPkgName());
				System.out.println(" ====== getBeginDate ======" + pd.getBeginDate());
				System.out.println(" ====== getUse ======" + pd.getUse());
				System.out.println(" ====== getFlag ======" + pd.getFlag());



			}
			
		}
	}
}
