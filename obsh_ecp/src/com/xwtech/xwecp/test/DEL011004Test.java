 package com.xwtech.xwecp.test;     

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.ITransSubmit;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.TransSubmitClientImpl;
import com.xwtech.xwecp.service.logic.pojo.Cusertransnoticedt;
import com.xwtech.xwecp.service.logic.pojo.Cusertransspdt;
import com.xwtech.xwecp.service.logic.pojo.Cusrtemplprodt;
import com.xwtech.xwecp.service.logic.pojo.Cusrtransproddt;
import com.xwtech.xwecp.service.logic.pojo.DEL011004Result;

/**   
 * @author 工号：743 
 * @version 1.0
 * @CreateDate  Jun 27, 2011 2:28:12 PM
 */
public class DEL011004Test {

	private static final Logger logger = Logger.getLogger(DEL011004Test.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13851347524");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13851347524");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		
		lic.setContextParameter(ic);
		
		ITransSubmit co = new TransSubmitClientImpl();
		//模板内选择的产品列表
		List<Cusrtemplprodt> cusrtemplprodtList =  new ArrayList<Cusrtemplprodt>();
		Cusrtemplprodt cusrtemplprodt = new Cusrtemplprodt();
		cusrtemplprodt.setTemplateProId("2000001146");
		cusrtemplprodt.setTemplateProInfoId("2010121018");
		cusrtemplprodt.setTemplateProName("");
		cusrtemplprodt.setTemplateProAttr("");
		
		Cusrtemplprodt cusrtemplprodt2 = new Cusrtemplprodt();
		cusrtemplprodt2.setTemplateProId("2000001591");
		cusrtemplprodt2.setTemplateProInfoId("2010121013");
		cusrtemplprodt2.setTemplateProName("");
		cusrtemplprodt2.setTemplateProAttr("");

		cusrtemplprodtList.add(cusrtemplprodt);
		cusrtemplprodtList.add(cusrtemplprodt2);
		//转移的增值产品列表
		List<Cusrtransproddt> cusrtransproddtList =  new ArrayList<Cusrtransproddt>();
		Cusrtransproddt cusrtransproddt = new Cusrtransproddt();
		cusrtransproddt.setZzProInfoName("");
		cusrtransproddt.setZzProPkgId("");
		cusrtransproddt.setZzProId("");
		cusrtransproddt.setZzProInfoId("");
		cusrtransproddt.setZzProAttr("");
		cusrtransproddtList.add(cusrtransproddt);
		//转移的SP列表
		List<Cusertransspdt> cusertransspdtList =  new ArrayList<Cusertransspdt>();
		Cusertransspdt cusertransspdt = new Cusertransspdt();
		cusertransspdt.setSpId("");
		cusertransspdtList.add(cusertransspdt);
		//转移的营销案列表
		List<Cusertransnoticedt> cusertransnoticedtList =  new ArrayList<Cusertransnoticedt>();
		Cusertransnoticedt cusertransnoticedt = new Cusertransnoticedt();
		cusertransnoticedt.setPlanInfoID("");
		cusertransnoticedtList.add(cusertransnoticedt);
		DEL011004Result re = co.transSubmit(
				"13951591857", 
				"", 
				"13", 
				"12", 
				"1000100070", 
				"88010472932582", 
				"891121", 
				cusrtemplprodtList, 
				cusrtransproddtList, 
				cusertransspdtList, 
				cusertransnoticedtList);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getBossCode());
		}
	}

}
  