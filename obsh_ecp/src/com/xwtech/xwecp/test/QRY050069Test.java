package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IGetQryaBankSubsInfoBankvtel;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.GetQryaBankSubsInfoBankvtelServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.BankSubsInfo;
import com.xwtech.xwecp.service.logic.pojo.BankVTInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY050069Result;

public class QRY050069Test {
	private static final Logger logger = Logger.getLogger(QRY050067Test.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        //初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
//		props.put("platform.url", "http://10.32.122.166:10004/obsh_ecp/xwecp.do");

		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14"); 
		lic.setUserMobile("13625151208");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13625151208");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		
		ic.addContextParameter("user_id", "1210200007634256");//2056200011182291
		
		lic.setContextParameter(ic);
		
		IGetQryaBankSubsInfoBankvtel igq = new GetQryaBankSubsInfoBankvtelServiceClientImpl();
		QRY050069Result res = new QRY050069Result();
		try {
			res = igq.getQryaBankSubsInfoBankvtel("13675152070", "0");
		} catch (LIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(res);
		List banksubinfoList = res.getBankSubsInfo();
		List bankVTinfoList = res.getBankVTInfo();
		for(int i = 0;i<banksubinfoList.size(); i++)
		{
			BankSubsInfo bsi = (BankSubsInfo)banksubinfoList.get(i);
			System.out.println("getAccttype =================== "+bsi.getAccttype());
			System.out.println("getBalance =================== "+bsi.getBalance());
			System.out.println("getBankacct =================== "+bsi.getBankacct());
			System.out.println("getBankid =================== "+bsi.getBankid());
			System.out.println("getBankname =================== "+bsi.getBankname());
			System.out.println("getContractstatus =================== "+bsi.getContractstatus());
			System.out.println("getCustname =================== "+bsi.getCustname());
			System.out.println("getDrawamt =================== "+bsi.getDrawamt());
			System.out.println("getIsabankauto =================== "+bsi.getIsabankauto());
			System.out.println("getMainprov =================== "+bsi.getMainprov());
			System.out.println("getMaintel =================== "+bsi.getMaintel());
			System.out.println("getPayamount =================== "+bsi.getPayamount());
			System.out.println("getPrepaytype =================== "+bsi.getPrepaytype());
			System.out.println("getProvinceid =================== "+bsi.getProvinceid());
			System.out.println("getTrigamt =================== "+bsi.getTrigamt());
			System.out.println("getUserstatus =================== "+bsi.getUserstatus());
		}
		for(int i = 0;i<bankVTinfoList.size(); i++)
		{
			BankVTInfo bi = (BankVTInfo)banksubinfoList.get(i);
			System.out.println("getCity_id =================== "+bi.getCity_id());
			System.out.println("getContractid =================== "+bi.getContractid());
			System.out.println("getInsertdate =================== "+bi.getInsertdate());
			System.out.println("getProvinceid =================== "+bi.getProvinceid());
			System.out.println("getVtelnum =================== "+bi.getVtelnum());
			System.out.println("getVtype =================== "+bi.getVtype());
		}
	}
}
