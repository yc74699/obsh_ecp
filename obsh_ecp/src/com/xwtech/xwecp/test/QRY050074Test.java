package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.jstl.test.Bean1;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.INetInstallSendOrderService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryBugGetStrService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryCardTypeService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.NetInstallSendOrderServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryBugGetStrServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryCardTypeServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.OrderSendBeen;
import com.xwtech.xwecp.service.logic.pojo.QRY040067Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040068Result;
import com.xwtech.xwecp.service.logic.pojo.QRY050074Result;

public class QRY050074Test
{
	private static final Logger logger = Logger.getLogger(QRY050074Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel"); 
		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
//props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.103:10004/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13813382424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13813382424");
		ic.addContextParameter("ddr_city", "13813382424");
		ic.addContextParameter("user_id", "1419200008195116");  //13813382424-1419200008195116,13913032424-1419200008195160
		lic.setContextParameter(ic);
		INetInstallSendOrderService co = new NetInstallSendOrderServiceClientImpl();
		
		String orderId = "1208012000020726";
		String orderDate = "20140210";
		String custName = "测试";
		String certType = "IdCard";
		String certId = "370211198106134817";
		
		String certAddr = "至少需要十六个字符";
		String telNum = "13813382424";
		String provId = "provid";
		String region = "14";
		String mainProdid = "1000100225";
		
		String mainProdidName = "全球通自选（预付费）";
		String prodTempalteId = null;
		String actId = null;
		String packId = null;
		
		String busidPackId = null;
		String rewardList = null;
		String totalPrice = "200000";
		String privPrice = null;
		String orderSource = "1";
		String notes = "20140210 test1";
		OrderSendBeen been = new OrderSendBeen();
		been.setActId(actId);
		been.setCertType(certType);
		been.setBusidPackId(busidPackId);
		been.setMainProdid(mainProdid);
		been.setCertAddr(certAddr);
		been.setCertId(certId);
		been.setMainProdid(mainProdid);
		been.setOrderDate(orderDate);
		been.setOrderId(orderId);
		been.setMainProdidName(mainProdidName);
		been.setTelNum(telNum);
		been.setProvId(provId);
		been.setRegion(region);
		been.setCustName(custName);
		been.setTotalPrice(totalPrice);
		been.setOrderSource(orderSource);
		/*QRY050074Result re = co.netInstallSendOrder(orderId, orderDate, custName, 
				certType, certId, certAddr, telNum, provId, region, mainProdid, 
				mainProdidName, prodTempalteId, actId, packId, busidPackId, rewardList,
				totalPrice, privPrice, orderSource, notes);*/
		QRY050074Result re2 = co.netInstallSendOrder(been);
		//QRY050074Result re = co.netInstallSendOrder(orderId, orderDate, custName, certType, certId, certAddr, telNum, provId, region, mainProdid, mainProdidName, prodTempalteId, actId, packId, busidPackId, rewardList, totalPrice, privPrice, orderSource, notes);
		logger.info(" ====== 开始返回参数 ======");
		/*if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
		}*/
		
		if (re2 != null)
		{
			logger.info(" === re.getResultCode() === " + re2.getResultCode());
			logger.info(" === re.getErrorCode() === " + re2.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re2.getErrorMessage());
		}
	}
}
