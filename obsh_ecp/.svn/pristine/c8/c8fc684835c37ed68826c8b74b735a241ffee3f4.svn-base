package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryAccountStatusAndDetailService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryAccountStatusAndDetailServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.AccountStatus;
import com.xwtech.xwecp.service.logic.pojo.ChildAccountBook;
import com.xwtech.xwecp.service.logic.pojo.MainAccountBook;
import com.xwtech.xwecp.service.logic.pojo.QRY040082Result;
public class QRY040082Test {
	private static final Logger logger = Logger.getLogger(QRY040082Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10006/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/sms_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("8");
		lic.setUserMobile("13851664447");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13851664447");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "2");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1419200000755198");
		
		lic.setContextParameter(ic);
		//
		
		IQueryAccountStatusAndDetailService co = new QueryAccountStatusAndDetailServiceClientImpl();
		//phoneNum:手机号码 oprtype:查询类型  accountOrBookId : 主账本id或账户id 
		//bizNo:业务类型 beginDate:子账本查询开始时间  endDate:子账本查询结束时间
		//查询账户状态
//		QRY040082Result re = co.queryAccountStatusAndDetail("15005156863","1","","","","");
		//查询主账本
//		QRY040082Result re = co.queryAccountStatusAndDetail("15005156863","2","101000","1","","");
		//查询子账本
		QRY040082Result re = co.queryAccountStatusAndDetail("15005156863","3","33","1","201407","201409");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== re.getResultCode()======"+re.getResultCode());
			System.out.println(" ====== re.getErrorCode()======"+re.getErrorCode());
			System.out.println(" ====== re.getErrorMessage()======"+re.getErrorMessage());
			if(null != re.getAccountStatus())
			{	
				AccountStatus accountStatus = re.getAccountStatus();
				System.out.println(" ====== 查询用户账户状态======");
				System.out.println(" ====== 账户ID======"+accountStatus.getAccountId());
				System.out.println(" ====== 账户状态字段======"+accountStatus.getAccountStatus());
			}
			if(null != re.getMainAccountBooks())
			{
				System.out.println(" ====== 查询用户账户主账本======");
				for(MainAccountBook mainAccountBook : re.getMainAccountBooks())
				{
					System.out.println(" ====== 账户ID======"+mainAccountBook.getAccountId());
					System.out.println(" ====== 主账本ID======"+mainAccountBook.getBookId());
					System.out.println(" ====== 主账本状态======"+mainAccountBook.getStatus());
					System.out.println(" ====== 余额======"+mainAccountBook.getBalance());
				}
			}
			if(null != re.getChildAccountBooks())
			{
				System.out.println(" ====== 查询用户账户子账本======");
				for(ChildAccountBook childAccountBook : re.getChildAccountBooks())
				{
					System.out.println(" ====== 主账本ID======"+childAccountBook.getBookId());
					System.out.println(" ====== 子账本ID======"+childAccountBook.getChildBookId());
					System.out.println(" ====== 子账本状态======"+childAccountBook.getStatus());
					System.out.println(" ====== 子账本余额======"+childAccountBook.getBalance());
					System.out.println(" ====== 子账本--======"+childAccountBook.getWithDrawlAmount());
					System.out.println(" ====== 开始时间======"+childAccountBook.getDeadlineBegin());
					System.out.println(" ====== 结束时间======"+childAccountBook.getDeadlineEnd());
				}
			}
		}
	}
}
