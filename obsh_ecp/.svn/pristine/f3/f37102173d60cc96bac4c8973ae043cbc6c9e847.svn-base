package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryGetPayForService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryOrderForNetService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryGetPayForServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryOrderForNetServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.PreConInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY060066Result;
import com.xwtech.xwecp.service.logic.pojo.QRY060067Result;

public class QRY060066Test {
	private static final Logger logger = Logger.getLogger(QRY060066Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
//		props.put("client.channel", "wap_channel");
		props.put("platform.url", "http://10.32.65.71:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8182/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.166:10004/obsh_ecp/xwecp.do");
//		props.put("platform.user", "xl");
//		props.put("platform.password", "xl");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "11");
		ic.addContextParameter("ddr_city", "11");
		ic.addContextParameter("user_id", "1101200021034311");
		lic.setContextParameter(ic);

		IQueryOrderForNetService cof = new QueryOrderForNetServiceClientImpl();
		QRY060067Result ret = cof.queryOrderForNet("15962439682", "", "20140716");
        // 结果集
        Map resultMap = new HashMap();
        // 可充值列表
        List<PreConInfo> canOrder = new ArrayList();
        // 已充值列表
        List<PreConInfo> hadOrder = new ArrayList();
        	List<PreConInfo> ls = ret.getPreContractInfo();
        	for (PreConInfo pre : ls) {
        		if ("5".equals(pre.getStatus())) {
        			canOrder.add(pre);
        		} else if ("6".equals(pre.getStatus())) {
        			hadOrder.add(pre);
        		}
        	}
		//增加新支付方式，支付宝  0 银联  1 支付宝
		IQueryGetPayForService co = new QueryGetPayForServiceClientImpl();
		QRY060066Result re = co.getPayForNum("15962439682","88148434502020","499",0);
		System.out.println(re.getResultCode());
		System.out.println(re.getMerId());
		System.out.println(re);
		System.out.println();
		System.out.println("*************************"+re.getUnionPaySrl());
		System.out.println("**************************"+re.getOrdId());
		System.out.println();
//		buildForm(re);
	}
	
	public static void buildForm(QRY060066Result retOrder)
	{
		String bankType = "4";
		String unionPaySrl = retOrder.getUnionPaySrl();
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> formMap = new HashMap<String, Object>();
		formMap.put("Version", retOrder.getVersion());
		formMap.put("ChkValue", retOrder.getChkValue());
		formMap.put("BgRetUrl", retOrder.getBgRetUrl());
		formMap.put("PageRetUrl", retOrder.getPageRetUrl());
		formMap.put("MerId", retOrder.getMerId());
		formMap.put("OrdId", retOrder.getOrdId());
		formMap.put("TransAmt", retOrder.getTransAmt());
		formMap.put("CuryId", retOrder.getCuryId());
		formMap.put("TransDate", retOrder.getTransDate());
		formMap.put("TransType", retOrder.getTransType());
		formMap.put("Priv1", retOrder.getPriv1());
		if("4".equals(bankType)){
			formMap.put("GateId", "8607");
		}
		
		String formData = buildUnionPayForm(formMap);
//		resultMap.put("formData", formData);
//		resultMap.put("mobile", mobile);
//		resultMap.put("bossDate", retOrder.getBossDate());
//		resultMap.put("unionPaySrl", retOrder.getUnionPaySrl());
//		
//		result.setResultObj(resultMap);
//		result.setResultCode(IResultCode.CDS_HANDLE_SUCCESS);
	}

	private static String buildUnionPayForm(Map<String, Object> map)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<form style='display: none' method='post' target='_blank' name='SendOrderForm' action='https://payment.ChinaPay.com/pay/TransGet'>");
		for(String property : map.keySet()){
			System.out.println("key=="+property+",value=="+map.get(property));
			sb.append(buildFormProperty(map, property));
		}
		sb.append("</form>");
		System.out.println(sb.toString());
		return sb.toString();
		
	}
	
	
	private static String buildFormProperty(Map<String,Object> map,String property){
		StringBuilder sb = new StringBuilder();
		sb.append("<input type='hidden' name='").append(property);
		sb.append("' value='").append(map.get(property)).append("'>");
		return sb.toString();
	}
	
	
	
}
