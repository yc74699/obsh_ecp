
package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQryPkgDetailInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QryPkgDetailInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.PackageBizId;
import com.xwtech.xwecp.service.logic.pojo.PkgDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY050062Result;

public class QRY050062Test
{
	private static final Logger logger = Logger.getLogger(QRY050062Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
		props.put("platform.url", "http://127.0.0.1:8080/sms_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.81:10000/sms_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.166:10000/js_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8081/sms_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14"); 
		lic.setUserMobile("13401312424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13401312424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "17");
		ic.addContextParameter("ddr_city", "17");
		
		ic.addContextParameter("user_id", "1738200005062065");  //2056200011182291
		
		lic.setContextParameter(ic);
		IQryPkgDetailInfoService co = new QryPkgDetailInfoServiceClientImpl();
		List<PackageBizId> packageBizIds = new ArrayList<PackageBizId>();
		PackageBizId biz = new PackageBizId();
		biz.setBizId("QQTTC_SW58");
		packageBizIds.add(biz);
		
		biz = new PackageBizId();
		biz.setBizId("QQTTC_SL58");
		packageBizIds.add(biz);

		biz = new PackageBizId();
		biz.setBizId("QQTTC_SW88");
		packageBizIds.add(biz);

		biz = new PackageBizId();
		biz.setBizId("QQTTC_");
		packageBizIds.add(biz);
		
		try{
		//动感地带 13401312424  brand_id：11 city_id：17 user_id：1738200005062065
			QRY050062Result re = co.qryPkgDetailInfo(packageBizIds);
		
		logger.info(" ====== 开始返回参数 ======"+re);
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorCode ======" + re.getErrorCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
			if (null != re.getPkgDetailList() && re.getPkgDetailList().size() > 0)
			{
				logger.info(" ====== getPkgDetailList.size ======" + re.getPkgDetailList().size());
				for (PkgDetail g : re.getPkgDetailList())
				{
					logger.info(" ====== g.getPkgId ======" + g.getPkgId());
					logger.info(" ====== g.getPkgName() ====== " + g.getPkgName());
					logger.info(" ====== g.getFeeDesc() ====== " + g.getFeeDesc());
					logger.info(" =========================================== ");
				}
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
