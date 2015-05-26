package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ITransResourcePresaleService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.TransResourcePresaleServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL100002Result;
import com.xwtech.xwecp.service.logic.pojo.TerminalOrderArrayBean;

public class DEL100002Test
{
	private static final Logger logger = Logger.getLogger(DEL100002Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13921078544");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15189615211");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		

		lic.setContextParameter(ic);

		ITransResourcePresaleService service = new TransResourcePresaleServiceClientImpl();
		
		/**
父订单号
销售类型 -->BINDSALE 捆绑销售
		    SINGLESALE 单独销售
			ACTSALE 活动销售
			BATCHSALE 批量销售

操作类型 PRESALE预占  RELEASE 释放

电话号码
活动号
档次号
预占数量


订单列表
子订单号
终端品牌
终端型号


		 **/
		
		List<TerminalOrderArrayBean> list = new ArrayList<TerminalOrderArrayBean>();
		TerminalOrderArrayBean bean1 = new TerminalOrderArrayBean();
		bean1.setOrderid("11061000125229");
		bean1.setTerminalBrand("rslm");
		bean1.setTerminalType("rsclM.26.2620031139");
		list.add(bean1);

		DEL100002Result re = service.transResourcePresale("20110923152301", "SINGLESALE", "PRESALE", 
				"15189615211", "300000136002", "300000136001", 1, list);
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" === re.getResultCode() === " + re.getResultCode());
			System.out.println(" === re.getErrorCode() === " + re.getErrorCode());
			System.out.println(" === re.getErrorMessage() === " + re.getErrorMessage());
			
			
		}
	}
}
