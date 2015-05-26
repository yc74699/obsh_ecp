package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IMultiNumBlackListDelService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.MultiNumBlackListDelServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.BlackListInfo;
import com.xwtech.xwecp.service.logic.pojo.DEL040062Result;

public class DEL040062Test {

	private static final Logger logger = Logger.getLogger(DEL040062Test.class);
	public static void main(String[] args) throws LIException {
//		初始化ecp客户端片段
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
		lic.setUserCity("12");
		lic.setUserMobile("13912986834");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15861762947");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		
		lic.setContextParameter(ic);
		
		List<BlackListInfo> blackListInfo = new ArrayList<BlackListInfo>();
		
		BlackListInfo bli = new BlackListInfo();
		bli.setListNum("18762544121");
		blackListInfo.add(bli);
		
		BlackListInfo bli1 = new BlackListInfo();
		bli1.setListNum("18762544122");
		blackListInfo.add(bli1);
		
		IMultiNumBlackListDelService is = new MultiNumBlackListDelServiceClientImpl();
		
		DEL040062Result re = is.multiNumBlackListDel("14", "13913825048", "13401979553", "0", blackListInfo);
		if(null != re){
			System.out.println("--getRetMsg--"+re.getRetMsg());
			System.out.println("--getRetCode--------"+re.getRetCode());
		}
	}
}