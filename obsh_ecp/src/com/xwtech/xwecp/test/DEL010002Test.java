package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryBillDetailService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryOperDetailService;
import com.xwtech.xwecp.service.logic.client_impl.common.IRegisterMTribeService;
import com.xwtech.xwecp.service.logic.client_impl.common.IScoreExchangeService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryBillDetailServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryOperDetailServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.RegisterMTribeServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ScoreExchangeServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL010002Result;
import com.xwtech.xwecp.service.logic.pojo.DEL020001Result;
import com.xwtech.xwecp.service.logic.pojo.GsmBillDetail;
import com.xwtech.xwecp.service.logic.pojo.OperDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY010001Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040004Result;
import com.xwtech.xwecp.service.logic.pojo.TransferTribe;

public class DEL010002Test {
private static final Logger logger = Logger.getLogger(QRY040004Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/xwecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13665212424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13665212424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13665212424");
		ic.addContextParameter("ddr_city", "20");
		
		ic.addContextParameter("user_id", "2371200004318871");
		
		lic.setContextParameter(ic);
		
		IRegisterMTribeService service = new RegisterMTribeServiceClientImpl();
		List<TransferTribe> tribeList = new ArrayList<TransferTribe>(3);
		
		//TribeType  部落类型编号
		//OperType:1开通部落  2关闭部落
		
		TransferTribe t1 = new TransferTribe();
		t1.setTribeType("1");
		t1.setOperType("2");
		
		TransferTribe t2 = new TransferTribe();
		t2.setTribeType("2");
		t2.setOperType("2");
		
		TransferTribe t3 = new TransferTribe();
		t3.setTribeType("3");
		t3.setOperType("2");
		tribeList.add(t1);
		tribeList.add(t2);
		tribeList.add(t3);
		DEL010002Result res = service.registerMTribe("13485122424", tribeList);
		
		String result = res.getResultCode().equals("0") ? "设置成功" : "设置失败" ;
	}
	
	
	
}
