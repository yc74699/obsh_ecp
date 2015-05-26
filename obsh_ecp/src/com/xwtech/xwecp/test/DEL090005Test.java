package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import com.xwtech.xwecp.service.logic.pojo.Reward;
import com.xwtech.xwecp.service.logic.pojo.DEL090005Result;
import com.xwtech.xwecp.service.logic.client_impl.common.ITerminalActivityApplyNew;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.TerminalActivityApplyNewClientImpl;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import java.util.Properties;
/**
 * 三码合一营销案办理（新）
 * @author xwtec
 *
 */
public class DEL090005Test
{
//	private static final Logger logger = Logger.getLogger(DEL090005Test.class);
	public static void main(String[] args) throws Exception
	{
		Properties props = new Properties();
		props.put("client.channel", "wap_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
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
		
		
		lic.setUserCity("12");
		lic.setUserMobile("13951590912");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13651554218");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("oper_id", "");
		
		
		lic.setContextParameter(ic);
		List<Reward> rewardlist = new ArrayList<Reward>();
		
		Reward reward = new Reward();
		reward.setRewardid("88009948347048");// 88009948347048
		reward.setRwdpackid("88009948347044");
		Reward reward2 = new Reward();
		reward2.setRewardid("11111");
		reward2.setRwdpackid("2222");
		rewardlist.add(reward);
		rewardlist.add(reward2);
		String imeiid = "359750013126678";//手机序列号
		String salenum = "A";//销售用的营销方案编码
		String servnumber = "13651554218";
		
		
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
		ITerminalActivityApplyNew terminalActivityApplyNew= new TerminalActivityApplyNewClientImpl();
		DEL090005Result re = terminalActivityApplyNew.iTerminalActivityApplyNew(salenum,imeiid,servnumber,restype,rewardlist);
			System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
		}
	}
}