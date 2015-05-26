package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.IChangeUserInfoService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ChangeUserInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.CallFeeAccount;
import com.xwtech.xwecp.service.logic.pojo.NewBizAccount;
import com.xwtech.xwecp.service.logic.pojo.DEL030002Result;
import com.xwtech.xwecp.service.logic.pojo.SepcAccount;
import com.xwtech.xwecp.service.logic.pojo.SpecAccountDetail;
import com.xwtech.xwecp.service.logic.pojo.YearlyPayDetail;

public class DEL030002Test
{
	private static final Logger logger = Logger.getLogger(DEL030002Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
			props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8182/obsh_ecp/xwecp.do");
//			props.put("platform.url", "http://10.32.122.166:10004/obsh_ecp/xwecp.do");
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
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "1419200008195116");
		
		lic.setContextParameter(ic);
		String icCard = "1419200008195116";
		IChangeUserInfoService co = new ChangeUserInfoServiceClientImpl();
		DEL030002Result re = co.changeUserInfo("13813382424", "省客服测试号", "鼓楼区定淮门7号", "210000", "13913032424", "kf@163.com",icCard);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			
			/*if (null != re.getCallFeeAccount())
			{
				CallFeeAccount dt = re.getCallFeeAccount();
				logger.info(" ====== CallFeeAccount.getBalance ======" + dt.getBalance());
				logger.info(" ====== CallFeeAccount.getValiDate ======" + dt.getValiDate());
				logger.info(" ====== ============================== ======");
			}
			if (null != re.getNewBizAccount())
			{
				NewBizAccount dt = re.getNewBizAccount();
				logger.info(" ====== NewBizAccount.getBalance ======" + dt.getBalance());
				logger.info(" ====== ============================== ======");
			}
			if (null != re.getSepcAccount())
			{
				SepcAccount dt = re.getSepcAccount();
				logger.info(" ====== SepcAccount.getNowReturnFee ======" + dt.getNowReturnFee());
				logger.info(" ====== SepcAccount.getSpecBalance ======" + dt.getSpecBalance());
				logger.info(" ====== ============================== ======");
			}
			if (null != re.getSpecAccountDetail() && re.getSpecAccountDetail().size() > 0)
			{
				logger.info(" ====== getSpecAccountDetail ======" + re.getSpecAccountDetail().size());
				for (SpecAccountDetail dt : re.getSpecAccountDetail())
				{
					logger.info(" ====== SpecAccountDetail.getAccountName ======" + dt.getAccountName());
					logger.info(" ====== SpecAccountDetail.getCashAccount ======" + dt.getCashAccount());
					logger.info(" ====== SpecAccountDetail.getNewBizAccount ======" + dt.getNewBizAccount());
					logger.info(" ====== SpecAccountDetail.getReturnTime ======" + dt.getReturnTime());
					logger.info(" ====== ============================== ======");
				}
			}
			if (null != re.getYearlyPayAccount() && re.getYearlyPayAccount().size() > 0)
			{
				logger.info(" ====== getYearlyPayAccount ======" + re.getYearlyPayAccount().size());
				for (YearlyPayDetail dt : re.getYearlyPayAccount())
				{
					logger.info(" ====== YearlyPayDetail.getYearlyCardBalance ======" + dt.getYearlyCardBalance());
					logger.info(" ====== YearlyPayDetail.getYearlyCardType ======" + dt.getYearlyCardType());
					logger.info(" ====== ============================== ======");
				}
			}*/
		}
	}
}
