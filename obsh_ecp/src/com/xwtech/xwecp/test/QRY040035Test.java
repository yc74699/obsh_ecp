package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryCustBandInfo;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryCustBandInfoClientImpl;
import com.xwtech.xwecp.service.logic.pojo.CustBandInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY040035Result;

public class QRY040035Test
{
	private static final Logger logger = Logger.getLogger(QRY040035Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13913814424");  
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913814424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
//		ic.addContextParameter("user_id", "");
		
		lic.setContextParameter(ic);
		
		IQueryCustBandInfo co = new QueryCustBandInfoClientImpl();
		QRY040035Result re = co.queryCustBandInfo("13913814424");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== 结果码 ======" + re.getResultCode());
			logger.info(" ====== 可查询个数 ======" + re.getCustBandInfos().size());
			
			if (null != re.getCustBandInfos() && re.getCustBandInfos().size() > 0)
			{
				for (CustBandInfo dt : re.getCustBandInfos())
				{
					logger.info(" ====== getLoginNum ======" + dt.getLoginNum());
					logger.info(" ====== getApplyData ======" + dt.getApplyData());
					logger.info(" ====== getStatus ======" + dt.getStatus());
					logger.info(" ====== getLimitBandWidth ======" + dt.getLimitBandWidth());
					logger.info(" ====== getBandCode ======" + dt.getBandCode());
					logger.info(" ====== getAddress ======" + dt.getAddress());
					logger.info(" ====== getBandSubsid ======" + dt.getBandSubsid());

				}
			}
		}
	}
}
