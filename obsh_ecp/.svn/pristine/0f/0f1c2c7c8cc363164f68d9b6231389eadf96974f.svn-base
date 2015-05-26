package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryStudentOrderInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryStudentOrderInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY610043Result;

/**
  * 高校迎新
 * 新增在线入网订单查询接口
 * @author wang.h
 *
 */
public class QRY610043Test {
	private static final Logger logger = Logger.getLogger(QRY610043Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		 lic.setBizCode("biz_code_19234");
		 lic.setOpType("开通/关闭/查询/变更");
		 lic.setUserBrand("动感地带");
		 lic.setUserCity("13");
		 lic.setUserMobile("13585198722");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13585198722");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "13");
		ic.addContextParameter("request_seq", "0_111");
		ic.addContextParameter("request_time", "20110804022825");
		ic.addContextParameter("ddr_city", "13");
		
		ic.addContextParameter("user_id", "1208200000060545");
		
		lic.setContextParameter(ic);
	
		
		IQueryStudentOrderInfoService co = new QueryStudentOrderInfoServiceClientImpl();
		QRY610043Result re = co.queryStudentOrderInfo("1", "1234");
		if(re !=null){
			System.out.println("===== getOrderId =====" + re.getOrderId());
			System.out.println("===== getTelNum =====" + re.getTelNum());
			System.out.println("===== getStudentNo =====" + re.getStudentNo());
			System.out.println("===== getSchoolNo =====" + re.getSchoolNo());
			System.out.println("===== getSchooolName =====" + re.getSchooolName());
			System.out.println("===== getProdInfo =====" + re.getProdInfo());
			System.out.println("===== getLinkName =====" + re.getLinkName());
			System.out.println("===== getLinkAddr =====" + re.getLinkAddr());
			System.out.println("===== getLinkNum =====" + re.getLinkNum());
			System.out.println("===== getDeliveryNo =====" + re.getDeliveryNo());
			System.out.println("===== getDeliverName =====" + re.getDeliverName());
		}
	}
}
