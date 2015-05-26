package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IDownPictureService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.DownPictureServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040067Result;
import com.xwtech.xwecp.service.logic.pojo.FileInfo;

public class DEL040067Test {

	private static final Logger logger = Logger.getLogger(DEL040067Test.class);
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
		lic.setUserCity("14");
		lic.setUserMobile("13912986834");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15861762947");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		
		lic.setContextParameter(ic);
		
		IDownPictureService is = new DownPictureServiceClientImpl();
		
		DEL040067Result re = is.downpicture("13913825048", "14130426392204143", "10.32.122.154");
		if(null != re && re.getFileinfo().size()>0){
		for(FileInfo f : re.getFileinfo()){
			System.out.println("--getFile_name--"+f.getFile_name());
			System.out.println("--getFile_type--"+f.getFile_type());
		
		}
		}}}

