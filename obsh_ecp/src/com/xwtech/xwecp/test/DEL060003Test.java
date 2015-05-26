package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IChkJoinDealSuperPackService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ChkJoinDealSuperPackServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL060003Result;
import com.xwtech.xwecp.service.logic.pojo.UserSuperPkg;

public class DEL060003Test
{
	private static final Logger logger = Logger.getLogger(DEL060003Test.class);
	
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
		lic.setUserCity("14");
		lic.setUserMobile("13776632514");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13776632514");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		ic.addContextParameter("user_id", "1419200008195160");
		
		lic.setContextParameter(ic);
		
		IChkJoinDealSuperPackService co = new ChkJoinDealSuperPackServiceClientImpl();
		List<UserSuperPkg> usersuperpkg = new ArrayList<UserSuperPkg>();
		UserSuperPkg uu = new UserSuperPkg();
		uu.setProdid("产品编码");
		uu.setAttr("");
		uu.setEfftype("Type_Immediate立即生效, Type_NextCycle下月生效 EffectType_Time指定时间");
		uu.setStatrdate("");//指定生效时间时必传
		uu.setEnddata("");//指定生效时间时必传
		uu.setIsmodule("默认为0");
		uu.setPkgid("包编码");
		uu.setOld_prodid("只有操作类型为修改时才有效");
		uu.setOprtype("PCOpRec:开通 PCOpMod:修改 PCOpDel:关闭 PCOpPau:暂停 PCOpRes:恢复");
		usersuperpkg.add(uu);
		DEL060003Result re = co.chkjoindealSuperPack("13813382424", usersuperpkg);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			
			
		}
	}
}
