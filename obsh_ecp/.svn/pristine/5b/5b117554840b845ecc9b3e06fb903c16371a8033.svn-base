package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQryImeiInventoryService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QryImeiInventoryServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY090011Result;
import com.xwtech.xwecp.service.logic.pojo.TerminalInventoryBean;

public class QRY090011Test
{
	private static final Logger logger = Logger.getLogger(QRY090011Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		//props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.122.166:10000/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13770666945");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15189615211");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13913814503");
		ic.addContextParameter("ddr_city", "14");
		
		lic.setContextParameter(ic);
		//
		List<TerminalInventoryBean> list =  new ArrayList<TerminalInventoryBean>();
		TerminalInventoryBean bean1 = new TerminalInventoryBean();
		bean1.setMobilebrand("rslm");
		bean1.setMobiletype("rsclM.5.520037261");
		list.add(bean1);
		
		TerminalInventoryBean bean2 = new TerminalInventoryBean();
		bean2.setMobilebrand("rslm");
		bean2.setMobiletype("rsclM.5.520037261");
		list.add(bean1);
		
//		terminalInventoryBean bean3 = new terminalInventoryBean();
//		bean3.setMobilebrand("NOKIA");
//		bean3.setMobiletype("8210");
//		list.add(bean3);
		
		IQryImeiInventoryService co = new QryImeiInventoryServiceClientImpl();
		QRY090011Result re = co.qryImeiInventory(list);
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== 返回结果码 ======" + re.getResultCode());
			System.out.println(" ====== 库存数量 ======" + re.getTerminalInventoryList().size());
			System.out.println(" ====== 错误编码 ======" + re.getErrorCode());
			System.out.println(" ====== 错误信息 ======" + re.getErrorMessage());
			
			if (null != re.getTerminalInventoryList() && re.getTerminalInventoryList().size() > 0)
			{
				for (TerminalInventoryBean dt : re.getTerminalInventoryList())
				{
					System.out.println(" ====== -- 库存数量 ======" + dt.getCount());
					System.out.println(" ====== -- 终端品牌 ======" + dt.getMobilebrand());
					System.out.println(" ====== -- 终端型号 ======" + dt.getMobiletype());
				 }
					
				
			}
		}
	}
}
