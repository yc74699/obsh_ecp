package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryPayIsOver100;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryPayListService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryPayIsOver100ClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryPayListServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.PayHistory;
import com.xwtech.xwecp.service.logic.pojo.QRY010034Result;
import com.xwtech.xwecp.service.logic.pojo.TerminalInventoryBean;
/**
 * 查询一个用户在半年期间内的充值是否有大于100元
 * @author xwtec
 *
 */
public class QRY010100Test
{
	private static final Logger logger = Logger.getLogger(QRY010100Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://localhost:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.166:10006/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8080/wap_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13813382424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		
		lic.setContextParameter(ic);
		
		IQueryPayIsOver100 co = new QueryPayIsOver100ClientImpl();
		QRY010034Result re = co.queryPayIsOver100("13813382424");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== 返回结果码 ======" + re.getResultCode());
			System.out.println(" ====== 状态（0为没有大于100，1为大于100）： ======" + re.getTypeReuslt());
			System.out.println(" ====== 错误编码 ======" + re.getErrorCode());
			System.out.println(" ====== 错误信息 ======" + re.getErrorMessage());
			
		}
	}
}
