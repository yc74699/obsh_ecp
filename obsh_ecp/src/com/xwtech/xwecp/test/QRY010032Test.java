package com.xwtech.xwecp.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryEIQryGMarketOrderInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryEIQryGMarketOrderInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.MarketOrderInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY010032Result;
import com.xwtech.xwecp.service.logic.pojo.QryCrset;
/**
 * 集团在线入网订单查询
 * 
 * @author xufan
 * 2014-03-31
 */
public class QRY010032Test
{
	private static final Logger logger = Logger.getLogger(QRY010032Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "wap_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp_test/xwecp.do");
		props.put("platform.user", "xl");
		props.put("platform.password", "xl");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("15261860213");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15261860213");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		lic.setContextParameter(ic);
		Map<String,Object>map=new HashMap<String,Object>();
		map.put("region", "12");//地市编码
		map.put("oper_source","78");//请求来源---网销平台78
		map.put("oper_id","12011001");//操作员
		map.put("page_size","");//分页每页记录数---建议值30   必须小于50（可选）-- [PAGESIZE]的值必须是大于0的数字，可以为空
		map.put("page_index","");//页索引---默认值0（可选）--PAGEIDX的值必须是大于0的数字，可以为空
		/*****************查询参数列表**************
		--入参传下面的值
		<param_id>ORDERID</param_id>
		<param_rule>ALL</param_rule>
		<param_value>212201305239182300</param_value>
		--入参传下面的值
		<param_id>TELNUM</param_id>
		<param_rule>ALL</param_rule>
		<param_value>18762020386</param_value>
		--入参传下面的值
		<param_id>ENDDATE</param_id>
		<param_rule>ALL</param_rule>
		<param_value>20140104120241</param_value>
		--入参传下面的值
		<param_id>STARTDATE</param_id>
		<param_rule>ALL</param_rule>
		<param_value>20120515134913</param_value>
		******************************************/
		QryCrset qryCrset=new QryCrset();
		qryCrset.setParam_id("ENDDATE");
		qryCrset.setParam_rule("ALL");
		qryCrset.setParam_value("20140104120241");
		map.put("qrycrset",qryCrset);
		IQueryEIQryGMarketOrderInfoService co = new QueryEIQryGMarketOrderInfoServiceClientImpl();
		QRY010032Result re = co.queryEIQryGMarketOrderInfo(map);
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== 返回结果码 ======" + re.getResultCode());
			System.out.println(" ====== 数量 ======" + re.getOrderList().size());
			System.out.println(" ====== 错误编码 ======" + re.getErrorCode());
			System.out.println(" ====== 错误信息 ======" + re.getErrorMessage());
			
			if (null != re.getOrderList() && re.getOrderList().size() > 0)
			{
				for (MarketOrderInfo dt : re.getOrderList())
				{
					System.out.println("***************************返回的结果信息*******************************");
					System.out.println(dt.getCERTADDR());
					System.out.println(dt.getCERTID());
					System.out.println(dt.getCUSTNAME());
					System.out.println(dt.getICCID());
					System.out.println(dt.getMAINPRODID());
					System.out.println(dt.getMAINPRODNAME());
					System.out.println(dt.getORDERDATE());
					System.out.println(dt.getOrdered());
					System.out.println(dt.getREGION());
					System.out.println(dt.getSHIPPING_CONTACT());
					System.out.println(dt.getSHIPPING_DETAILADDRESS());
					System.out.println(dt.getTELNUM());
					System.out.println(dt.getTOTALPRICE());
				 }
					
				
			}
		}
	}
}
