package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryBusinessService;
import com.xwtech.xwecp.service.logic.client_impl.common.ITransactBusinessService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryBusinessServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.TransactBusinessServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL010001Result;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;

public class HJZYTestApp {
	
    /**
     * 查询用户设置的呼叫转移
     * @param args
     */
	public static void query()
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1:80/ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13601400067");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13851347524");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13851347524");
		ic.addContextParameter("ddr_city", "20");
		
		ic.addContextParameter("user_id", "2056200011182291");
		
		lic.setContextParameter(ic);
		
		IQueryBusinessService queryBusinessService = new QueryBusinessServiceClientImpl();
		
		try {
			QRY020001Result s = queryBusinessService.queryBusiness("13851347524", 1, "HJZYSZ");
			List<GommonBusiness> gommonBusiness = s.getGommonBusiness();
			
			for(int i = 0 ; i < gommonBusiness.size() ; i ++)
			{
				GommonBusiness g = gommonBusiness.get(i);
				System.out.println(g.getId() + "--" + g.getBeginDate() + "--" + g.getEndDate() + "--被呼叫转移号码:" + g.getReserve1());
			}
			
			System.out.println();
		} catch (LIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
     * 查询用户设置的呼叫转移
     * @param args
     * 	//开通呼叫转移设置
		final String OPT_OPEN = "1";
		//修改呼叫转移设置
		final String OPT_CHANGE = "2";
		//关闭呼叫转移设置
		final String OPT_CLOSE = "3";
     */
	public static void transfer(int type)
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1:80/ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13601400067");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13851347524");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13851347524");
		ic.addContextParameter("ddr_city", "20");
		
		ic.addContextParameter("user_id", "2056200011182291");
		
		lic.setContextParameter(ic);
		
		ITransactBusinessService service = new TransactBusinessServiceClientImpl();
		
		//reserve1  为转移号码     oprType为设置类型 1: 开通  2： 修改  3：关闭  
		String resultInfo = "";
		
		
		try {
			
			if(type == 1)
			{
				DEL010001Result result = service.transactBusiness("", "HJZYSZ", type, 1, "", "", "13851347124", "");
				resultInfo = result.getResultCode().equals("0") ? "呼叫转移开通成功" : "呼叫转移开通失败";
			}
			else if(type == 2)
			{
				DEL010001Result result = service.transactBusiness("", "HJZYSZ", type, 1, "", "", "13813382424", "");
				resultInfo = result.getResultCode().equals("0") ? "呼叫转移修改成功" : "呼叫转移修改失败";
			}
			else if(type == 3)
			{
				DEL010001Result result = service.transactBusiness("", "HJZYSZ", type, 1, "", "", "13851347124", "");
				resultInfo = result.getResultCode().equals("0") ? "呼叫转移关闭成功" : "呼叫转移关闭失败";
			}
			
		} catch (LIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(resultInfo);
		
	}
	
	
	public static void main(String[] args)
	{
		transfer(3);
	}
	
	
	
}
