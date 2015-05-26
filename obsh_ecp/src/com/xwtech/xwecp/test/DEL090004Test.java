package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import com.xwtech.xwecp.service.logic.pojo.Reward;
import com.xwtech.xwecp.service.logic.pojo.DEL090004Result;
import com.xwtech.xwecp.service.logic.client_impl.common.ITerminalActivityCheckNew;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.TerminalActivityCheckNewClientImpl;
//import org.apache.log4j.Logger;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import java.util.Properties;
/**
 * 三码合一校验
 * @author xwtec
 *
 */
public class DEL090004Test
{
//	private static final Logger logger = Logger.getLogger(DEL090004Test.class);
	public static void main(String[] args) throws Exception
	{

		Properties props = new Properties();
		props.put("client.channel", "wap_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8080/wap_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.166:10004/obsh_ecp/xwecp.do");
		//		props.put("platform.user", "xl");
		//		props.put("platform.password", "xl");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		props.put("platform.user", "xl");
		props.put("platform.password", "xl");
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
//		lic.setUserCity("14");
//		lic.setUserMobile("13813382424");
//		lic.setUserMobile("13813382424");
//		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "13813382424");
//		ic.addContextParameter("route_type", "1");
//		ic.addContextParameter("route_value", "13813382424");
//		ic.addContextParameter("ddr_city", "14");
		
		
		lic.setUserCity("14");
		lic.setUserMobile("13813382424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("oper_id", "");
		ic.addContextParameter("user_id", "1419200008195116");
		
		
		lic.setContextParameter(ic);
		List<Reward> rewardlist = new ArrayList<Reward>();
		
		Reward reward = new Reward();
		reward.setRewardid("88017460390574");// 88009948347048
		reward.setRwdpackid("88017476196602");
//		Reward reward2 = new Reward();
//		reward2.setRewardid("11111");
//		reward2.setRwdpackid("2222");
		rewardlist.add(reward);
//		rewardlist.add(reward2);
		String imeiid = "864518021326430";//手机序列号
		String salenum = "300003128323";//销售用的营销方案编码
		String servnumber = "13813382424";
		
		
//		Reward reward = new Reward();
//		reward.setRewardid("88009948347040");
//		reward.setRwdpackid("88009948347048");
////		Reward reward2 = new Reward();
////		reward2.setRewardid("11111");
////		reward2.setRwdpackid("2222");
//		rewardlist.add(reward);
////		rewardlist.add(reward2);
//		String imeiid = "359750012706512";//手机序列号
//		String salenum = "A";//销售用的营销方案编码
//		String servnumber = "13951590912";
		String restype = "rscIM";//资源类型
		ITerminalActivityCheckNew terminalActivityCheckNew= new TerminalActivityCheckNewClientImpl();
		DEL090004Result re = terminalActivityCheckNew.iTerminalActivityCheckNew(salenum,imeiid,servnumber,restype,rewardlist);
			System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
		}
	}
}