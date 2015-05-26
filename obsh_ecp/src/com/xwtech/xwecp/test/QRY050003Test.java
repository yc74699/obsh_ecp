package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryGamePointService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryGamePointServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.GamePoint;
import com.xwtech.xwecp.service.logic.pojo.QRY050003Result;

public class QRY050003Test {
private static final Logger logger = Logger.getLogger(QRY040014TestApp.class);
	
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
		lic.setUserCity("用户县市");
		lic.setUserMobile("13646272637");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13813382424");
		ic.addContextParameter("ddr_city", "20");
		
		ic.addContextParameter("user_id", "2050200009867279");
		
		lic.setContextParameter(ic);
		
		IQueryGamePointService service = new QueryGamePointServiceClientImpl();
		QRY050003Result result = service.queryGamePoint("13813382424", "20091101", "20091110");
		
		List<GamePoint> gamePointList = result.getGamePoint();
		GamePoint gamePoint = null;
		
		for(int i = 0 ; i < gamePointList.size() ; i ++)
		{
			gamePoint = gamePointList.get(i);
			System.out.println(gamePoint.getOprType() + "--" +
					           gamePoint.getSpCode() + "--"  +
					           gamePoint.getStartTime() + "--"  +
					           gamePoint.getStopTime() + "--"  +
					           gamePoint.getDataSize() + "--" +
					           gamePoint.getContentCode() + "--" +
					           gamePoint.getPointCost()
					           );
		}
		
		System.out.println("-----end-----------");
	}
	
	
}
