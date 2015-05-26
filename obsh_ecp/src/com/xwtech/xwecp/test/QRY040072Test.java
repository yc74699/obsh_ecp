package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQuery4GPkgUsedInfoService; 
import com.xwtech.xwecp.service.logic.client_impl.common.impl.Query4GPkgUsedInfoServiceClientImpl; 
import com.xwtech.xwecp.service.logic.pojo.FluxDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY040072Result;

/**
 *  查询用户4G套餐上月使用情况：上个月开通了多少流量，还剩下多少流量。
 *  YangXQ 
 *  2014-6-13
 */
public class QRY040072Test
{
	private static final Logger logger = Logger.getLogger(QRY040072Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8182/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserMobile("13606132424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13606132424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13606132424");//15895096970（没用）  15005156863、13606132424(有用的)
		lic.setContextParameter(ic);
		
		IQuery4GPkgUsedInfoService co = new Query4GPkgUsedInfoServiceClientImpl();
		QRY040072Result re = co.query4GPkgUsedInfo("13606132424");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
			List<FluxDetail> details = re.getFluxDetailList();
			if(null != details && 0 < details.size())
			{
				for(FluxDetail detail : details)
				{
					System.out.println(detail.show());
				}
			}
		}
		
		
//		long sum=0;
//		for(int i=1;i<=10;i++){	
//		Long startTime = System.currentTimeMillis();// 代码段		
//		Long endTime = System.currentTimeMillis();
//		sum=endTime-startTime+sum;
//		}
//		System.out.println("10次 发送报文 到 返回报文 平均用时 ： "+(sum/10)+ "  milliseconds");

	}
}