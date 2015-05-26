package com.xwtech.xwecp.test;

import java.util.Properties;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryFamilyInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryFamilyInfoService;
import com.xwtech.xwecp.service.logic.pojo.QRY200001Result;
import com.xwtech.xwecp.service.logic.pojo.WMemberInfo;
/**
 * 判断是否是国内亲情号码测试类
 * @author yangli
 *
 */
public class QRY200001Test {
private static final Logger logger = Logger.getLogger(QRY200001Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		 lic.setBizCode("biz_code_19234");
		 lic.setOpType("开通/关闭/查询/变更");
		 lic.setUserBrand("动感地带");
		 lic.setUserCity("12");
		 lic.setUserMobile("13585198722");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13585198722");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("request_seq", "0_111");
		ic.addContextParameter("request_time", "20110804022825");
		ic.addContextParameter("ddr_city", "12");
		
		ic.addContextParameter("user_id", "1208200000060545");
		
		lic.setContextParameter(ic);
		WMemberInfo piInfo = new WMemberInfo();
		
		
		List<WMemberInfo> piList = new ArrayList<WMemberInfo>();
		piList.add(piInfo);
		
		IQueryFamilyInfoService co = new QueryFamilyInfoServiceClientImpl();
		QRY200001Result re = co.queryFamilyInfoService("");
		
		if(re !=null){
		
			System.out.println("BOSS编码："+re.getBossCode());
			for(WMemberInfo pi : re.getWMemberInfo()){
				System.out.println("家庭成员开始：" + "---------------------------");
				System.out.println("成员标识：" +pi.getFamilysubsid());
				System.out.println("成员号码：" +pi.getServnumber());//0能办理，其他不能
				System.out.println("成员短号:" +pi.getShortnum());//校验结果说明
				System.out.println("是否主号:" +pi.getIshousehold());//校验结果说明
				System.out.println("所属地市:" +pi.getMemregion());//校验结果说明
				System.out.println("家庭成员结束：" + "---------------------------");
			}
		}
	}
	
}
