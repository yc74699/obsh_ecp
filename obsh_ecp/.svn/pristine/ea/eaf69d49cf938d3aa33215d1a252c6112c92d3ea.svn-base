package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQryReferralsMarketingService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QryReferralsMarketingServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.CombineBean;
import com.xwtech.xwecp.service.logic.pojo.QRY060003Result;
import com.xwtech.xwecp.service.logic.pojo.ReferralsMarketInfo;;

public class QRY060003Test {
	private static final Logger logger = Logger.getLogger(QRY060003Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.152:10003/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.103:10004/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.67:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.73:10006/openapi_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.185:10000/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.103:10004/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("1");
		lic.setUserCity("14");
		lic.setUserMobile("13951813598");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13951813598");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		lic.setContextParameter(ic);
		
		IQryReferralsMarketingService co = new QryReferralsMarketingServiceClientImpl();
		
		QRY060003Result re = co.qryReferralsMarketing("13951813598", "0", "1", "1","10");
		System.out.println(" ====== 开始返回参数 ======"+re);
		if (re != null)
		{
			System.out.println(re.getResultCode());
			System.out.println(re.getErrorCode());
			System.out.println(re.getErrorMessage());
			System.out.println("ReMarketInfo size: "+re.getReMarketInfo().size());
			for(ReferralsMarketInfo marketinfo : re.getReMarketInfo())
			{
				System.out.println("CombineBean size: "+marketinfo.getComBineLs().size());
				for(CombineBean info : marketinfo.getComBineLs())
				{
					System.out.println("channelsubtype ===================================" + info.getChannelSubType());
					System.out.println("id ===================================" + info.getId());
					System.out.println("channel_id ===================================" + info.getChannel_id());
					System.out.println("date_add ===================================" + info.getDate_add());
					System.out.println("type_id ===================================" + info.getType_id());
					System.out.println("busi_num ===================================" + info.getBusi_num());
					System.out.println("busi_name ===================================" + info.getBusi_name());
					System.out.println("page_name ===================================" + info.getPage_name());
					System.out.println("page_url ===================================" + info.getPage_url());
					System.out.println("page_pic1 ===================================" + info.getPage_pic1());
					System.out.println("page_pic2 ===================================" + info.getPage_pic2());
					System.out.println("page_pic3 ===================================" + info.getPage_pic3());
					System.out.println("page_pic4 ===================================" + info.getPage_pic4());
					System.out.println("field1 ===================================" + info.getField1());
					System.out.println("field2 ===================================" + info.getField2());
					System.out.println("field3 ===================================" + info.getField3());
					System.out.println("remark ===================================" + info.getRemark());
					System.out.println("project_id ===================================" + info.getProject_id());
					System.out.println("status ===================================" + info.getStatus());
					System.out.println("begin_time ===================================" + info.getBegin_time());
					System.out.println("end_time ===================================" + info.getEnd_time());
					System.out.println("mobileList ===================================" + info.getMobileList());
					System.out.println("webTransId  ===================================" + info.getWebTransId ());
					System.out.println("floor ===================================" + info.getFloor());
					System.out.println("scene ===================================" + info.getScene());
					System.out.println("userSeq ===================================" + info.getUserSeq());
					System.out.println("===================================");
					System.out.println("===================================");
				}
			}

		}
	}
}
