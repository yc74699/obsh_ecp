package com.xwtech.xwecp.test;

import java.util.Properties;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryFamilyProdServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryFamilyProdService;
import com.xwtech.xwecp.service.logic.pojo.QRY200003Result;
import com.xwtech.xwecp.service.logic.pojo.ProductInfo;
/**
 * 判断是否是国内亲情号码测试类
 * @author yangli
 *
 */
public class QRY200003Test {
private static final Logger logger = Logger.getLogger(QRY200003Test.class);
	
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
		 lic.setUserCity("12");
		 lic.setUserMobile("13585198722");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13585198722");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("request_seq", "0_111");
		ic.addContextParameter("request_time", "20110804022825");
		ic.addContextParameter("ddr_city", "12");
		
		ic.addContextParameter("user_id", "1208200000060545");
		
		lic.setContextParameter(ic);
	
		
		
		IQueryFamilyProdService co = new QueryFamilyProdServiceClientImpl();
		QRY200003Result re = co.queryFamilyProdService("");
		
		if(re !=null){
		
			System.out.println("BOSS编码："+re.getBossCode());
			for(ProductInfo pi : re.getProductInfo()){
				System.out.println("家庭成员开始：" + "---------------------------");
				System.out.println("结束时间：" +pi.getEnddate());
				System.out.println("开始时间：" +pi.getStartdate());
				System.out.println("产品编码：" +pi.getIncreprodid());
			}
		}
	}
	
}
