package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryMobileGameService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryMobileGameServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.MobileGameInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY020010Result;

public class QRY020010Test
{
	private static final Logger logger = Logger.getLogger(QRY020010Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://localhost/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13805157824");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13805157824");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		ic.addContextParameter("user_id", "1419200019149270");
		
		lic.setContextParameter(ic);
		
		IQueryMobileGameService service = new QueryMobileGameServiceClientImpl();
		QRY020010Result result = service.queryMobileGame("13913814503");
		logger.info(" ====== 开始返回参数 ======");
		if (result != null)
		{
			List<MobileGameInfo> list = result.getMobileGameInfos();
			for(MobileGameInfo info : list){
				System.out.println("*******************************");
				System.out.println("Bizcode:   " + info.getBizCode());
				System.out.println("Biztype:   " + info.getBizType());
				System.out.println("busiName:  " + info.getBusiName());
				System.out.println("Enddate:   " + info.getEndDate());
				System.out.println("Isopen:    " + info.getIsOpen());
				System.out.println("Price:     " + info.getPrice());
				System.out.println("Spid:      " + info.getSpId());
				System.out.println("Spname:    " + info.getSpName());
				System.out.println("Startdate: " + info.getStartDate());
				System.out.println("*******************************");
			}
		}
	}
}
