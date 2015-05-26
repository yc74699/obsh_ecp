package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQuerySmallPayQryBalanceService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QuerySmallPayQryBalanceServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY010030Result;
import com.xwtech.xwecp.service.logic.pojo.QryDetail;
/**
 * 小额支付话费测试
 * 
 * @author xufan
 * 2014-03-13
 */
public class QRY010030Test
{
	private static final Logger logger = Logger.getLogger(QRY010030Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
//		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp_test/xwecp.do");
		props.put("platform.url", "http://10.32.65.238:8182/obsh_ecp/xwecp.do");

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
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		lic.setContextParameter(ic);
		IQuerySmallPayQryBalanceService co = new QuerySmallPayQryBalanceServiceClientImpl();
		QRY010030Result re = co.querySmallPayQryBalance("18351002158",0,"","20140201","20140502");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== 返回结果码 ======" + re.getResultCode());
			System.out.println(" ====== 数量 ======" + re.getQryDetail().size());
			System.out.println(" ====== 支付 ======" + re.getMapResult().get("balance"));
			System.out.println(" ====== 错误编码 ======" + re.getErrorCode());
			System.out.println(" ====== 错误信息 ======" + re.getErrorMessage());
			
			if (null != re.getQryDetail() && re.getQryDetail().size() > 0)
			{
				for (QryDetail dt : re.getQryDetail())
				{
					System.out.println("***************************返回的结果信息*******************************");
					System.out.println(" ====== --  ======" + dt.getPayfee());
					System.out.println(" ====== --  ======" + dt.getPantform_date());
					System.out.println(" ====== --  ======" + dt.getStatus_date());
					System.out.println(" ====== --  ======" + dt.getStatus());
				 }
					
				
			}
		}
	}
}
