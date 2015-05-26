package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryOwnMonterBunsiService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryOwnMonterBunsiServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.MonternetBean;
import com.xwtech.xwecp.service.logic.pojo.OwnBunsiBean;
import com.xwtech.xwecp.service.logic.pojo.QRY040055Result;


public class QRY040055Test {	

	private static final Logger logger = Logger.getLogger(QRY040055Test.class);

	public static void main(String[] args) throws LIException {

		// 初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");

		XWECPLIClient client = XWECPLIClient.createInstance(props);
		// 逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13813382424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1419200008195116");
		lic.setContextParameter(ic);

		IQueryOwnMonterBunsiService is = new QueryOwnMonterBunsiServiceClientImpl();
		QRY040055Result re = is.queryOwnMonterBunsi("13813382424");
		List<OwnBunsiBean> ownBunsiList = re.getOwnBunsiList();
		List<MonternetBean> monternetList = re.getMonternetList();
		
		if(null != ownBunsiList && ownBunsiList.size() > 0)
		{
			int size = ownBunsiList.size();
			for(int i = 0; i < size ;i++)
			{
				System.out.println("getDomainCode ================== "+ownBunsiList.get(i).getDomainCode());
				System.out.println("getBizCode ================== "+ownBunsiList.get(i).getBizCode());
				System.out.println("getPrdCode ================== "+ownBunsiList.get(i).getPrdCode());
				System.out.println("getType ================== "+ownBunsiList.get(i).getType());
				System.out.println("getState ================== "+ownBunsiList.get(i).getState());
				System.out.println("getStartTime ================== "+ownBunsiList.get(i).getStartTime());
				System.out.println("getEndTime ================== "+ownBunsiList.get(i).getEndTime());
			}
		}
		if(null != monternetList && monternetList.size() > 0)
		{
			int size = monternetList.size();
			for(int i = 0; i < size ;i++)
			{
				System.out.println("getName ================== "+monternetList.get(i).getName());
				System.out.println("getVal ================== "+monternetList.get(i).getVal());
				System.out.println("getBizType ================== "+monternetList.get(i).getBizType());
				System.out.println("getType ================== "+monternetList.get(i).getType());
				System.out.println("getState ================== "+monternetList.get(i).getState());
				System.out.println("getStartTime ================== "+monternetList.get(i).getStartTime());
				System.out.println("getEndTime ================== "+monternetList.get(i).getEndTime());
			}
		}
	}

}
