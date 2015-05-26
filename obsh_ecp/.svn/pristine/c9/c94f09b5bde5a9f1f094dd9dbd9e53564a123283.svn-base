package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryMarketplandischkService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryMarketplandischkServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040097Result;

/**
 * 折扣优惠营销方案查询接口
 * @author YangXQ
 * 2015-01-27
 */
public class QRY040097Test
{
	private static final Logger logger = Logger.getLogger(QRY040097Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel"); 
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.230:8182/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("11");
		lic.setUserMobile("");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("user_id", "");	
		lic.setContextParameter(ic);
 
		IQueryMarketplandischkService co = new QueryMarketplandischkServiceClientImpl();
		// 入参 ：  手机号,活动id,档次id
		QRY040097Result re = co.queryMarketplandischk("15805232324","3000866021","300001282032");//   
		System.out.println(" ================================================");
		if (re != null)
		{
			System.out.println(" ====== 档次名称  ======" + re.getPrivname());      //档次名称
			System.out.println(" ====== 档次描述  ======" + re.getDisdiscription());//档次描述
			System.out.println(" ====== 优惠类型  ======" + re.getDiscounttype());  //优惠类型:0：折扣；1：优惠 2：无折扣和优惠
			System.out.println(" ====== 优惠金额  ======" + re.getDiscountvalue()); //优惠金额:优惠类型为0和1，该字段的值为0.1 0.2小数，如果优惠类型为2，该字段的值为空
	        System.out.println(" ====== 返回码       ======" + re.getRet_code());      //返回码 
	        System.out.println(" ====== 返回信息  ======" + re.getRet_msg());       //返回信息 
		}
	}
}
