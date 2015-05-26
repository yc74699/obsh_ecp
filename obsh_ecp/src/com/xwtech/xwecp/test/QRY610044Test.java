package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryTelNumListService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryTelNumListServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY610044Result;
import com.xwtech.xwecp.service.logic.pojo.Usableteldt;

/**
 * 高校迎新
* 通过新生随机码查询可用的在线入网号码
* @author wang.h
*
*/
public class QRY610044Test {
	private static final Logger logger = Logger.getLogger(QRY610044Test.class);
	
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
	
		
		IQueryTelNumListService co = new QueryTelNumListServiceClientImpl();
		QRY610044Result re = co.queryTelnumList("23423432");
		
		if(re !=null){
			for(Usableteldt u : re.getUsableteldtList()){
				System.out.println("======== u.getTelNum ========" + u.getTelNum());
			}
		}
	}
}
