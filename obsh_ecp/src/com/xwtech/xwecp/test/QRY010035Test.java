package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryGrpCustOrderChannelService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryGrpCustOrderChannelServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY010035Result;
import com.xwtech.xwecp.service.logic.pojo.QryDetail;
/**
 * 查询集团渠道权限开放情况测试
 * 
 * @author xufan
 * 2014-05-08
 */
public class QRY010035Test
{
	private static final Logger logger = Logger.getLogger(QRY010035Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp_test/xwecp.do");
//		props.put("platform.url", "http://10.32.122.167:10005/obsh_ecp/xwecp.do");

		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13815575493");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "18351002158");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "18351002158");
		ic.addContextParameter("ddr_city", "14");
		lic.setContextParameter(ic);
		IQueryGrpCustOrderChannelService co = new QueryGrpCustOrderChannelServiceClientImpl();
		QRY010035Result re = co.qryGrpCustOrderChannel("1208022037635931","16");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== 返回结果码 ======" + re.getResultCode());
			System.out.println(" ====== 错误编码 ======" + re.getErrorCode());
			System.out.println(" ====== 错误信息 ======" + re.getErrorMessage());
			System.out.println(" ====== 渠道结果 ======" + re.getChannelinfo());
			
		}
	}
}
