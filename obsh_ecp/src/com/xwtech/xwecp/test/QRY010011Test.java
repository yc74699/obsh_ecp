package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryFamilyBillsService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryFamilyBillsServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.AccountFeeInfo;
import com.xwtech.xwecp.service.logic.pojo.BillPackageInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY010011Result;

public class QRY010011Test {
	private static final Logger logger = Logger.getLogger(QRY010011Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://10.32.229.74:8080/js_ecp/xwecp.do");
		//props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("18252747060");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "18252747060");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "18252747060");
		ic.addContextParameter("ddr_city", "23");
		
		ic.addContextParameter("user_id", "2371300001373195");
		
		lic.setContextParameter(ic);
		
		IQueryFamilyBillsService co = new QueryFamilyBillsServiceClientImpl();
//		QRY010011Result re = co.queryFamilyBills(1, "200912", "1101200019274482");
		QRY010011Result re = co.queryFamilyBills(2, "201109", "2371300001373195");
//		QRY010011Result re = co.queryFamilyBills(3, "200912", "2056200016283080");
//		QRY010002Result re = co.queryAccountFeeInfo("13813382424", "201001");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			
			if (null != re.getAccountFeeInfo())
			{
				List<AccountFeeInfo> list = re.getAccountFeeInfo();
				for (AccountFeeInfo info : list) {
					logger.info(" ====== info.getCode ======" + info.getCode());
					logger.info(" ====== info.getName ======" + info.getName());
					logger.info(" ====== info.getFee ======" + info.getFee());
					logger.info(" ====== ============================== ======");
				}
			}
			if (null != re.getSumFee())
			{
				logger.info(" ====== getSumFee ======" + re.getSumFee());
				logger.info(" ====== ============================== ======");
			}
			if (null != re.getGroup())
			{
				logger.info(" ====== getGroup ======" + re.getGroup());
				logger.info(" ====== ============================== ======");
			}
			if (null != re.getOther())
			{
				logger.info(" ====== getOther ======" + re.getOther());
				logger.info(" ====== ============================== ======");
			}

			if (null != re.getAccountFeeInfo())
			{
				List<BillPackageInfo> list = re.getBillPackageInfo();
				for (BillPackageInfo info : list) {
					logger.info(" ====== info.getName ======" + info.getName());
					logger.info(" ====== info.getDx ======" + info.getDx());
					logger.info(" ====== info.getBdzj ======" + info.getBdzj());
					logger.info(" ====== ============================== ======");
				}
			}
}
	}
}
