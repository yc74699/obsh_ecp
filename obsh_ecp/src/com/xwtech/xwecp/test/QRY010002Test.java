package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryAccountFeeInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryAccountFeeInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.AccountFeeInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY010002Result;

public class QRY010002Test {
	private static final Logger logger = Logger.getLogger(QRY010002Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//��ʼ��ecp�ͻ���Ƭ��
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//�߼��ӿڵ���Ƭ��
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("1");
		lic.setUserBrand("1");
		lic.setUserCity("13");
		lic.setUserMobile("13515294455");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13515294455");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "13");
		ic.addContextParameter("ddr_city", "13");
		
		
		lic.setContextParameter(ic);
		
		IQueryAccountFeeInfoService co = new QueryAccountFeeInfoServiceClientImpl();
		QRY010002Result re = co.queryAccountFeeInfo("13515294455", "201307");
		List<AccountFeeInfo> accountFeeInfoList = re.getAccountFeeInfo();
		
		
		AccountFeeInfo accountFeeInfo = null;
		System.out.println("name----fee----code----superCode----leval----displayOrder");
		
		for(int i = 0 ; i < accountFeeInfoList.size() ; i ++)
		{
			accountFeeInfo = accountFeeInfoList.get(i);
			System.out.println(accountFeeInfo.getName() + "----" +
					accountFeeInfo.getFee() + "----" +
					accountFeeInfo.getCode()  + "----" +
					accountFeeInfo.getSuperCode() + "----" +
					accountFeeInfo.getLeval() + "----" +
					accountFeeInfo.getDisplayOrder()
			 );
		}
		//һ��ҵ����Ϣ
		List<AccountFeeInfo> oneLevelList = getLevelFromList(accountFeeInfoList,1,"0");
		
		//��ʾ��Ϣ
		List<AccountFeeInfo> accountListResult = new ArrayList<AccountFeeInfo>(accountFeeInfoList.size());
		
	    Long s1 = System.currentTimeMillis();
		
		if(oneLevelList != null && oneLevelList.size() > 0)
		{
			for(int i = 0 ; i < oneLevelList.size() ; i ++)
			{
				AccountFeeInfo level1accountFeeInfo = oneLevelList.get(i);
				AccountFeeInfo level1accountFeeInfoTemp = new AccountFeeInfo();
				level1accountFeeInfoTemp.setFee(level1accountFeeInfo.getFee());
				level1accountFeeInfoTemp.setName((i + 1) + "." + level1accountFeeInfo.getName());
				accountListResult.add(level1accountFeeInfoTemp);
				
				//����ҵ����Ϣ
				List<AccountFeeInfo> twoLevelList = getLevelFromList(accountFeeInfoList,2,level1accountFeeInfo.getCode());
				
				for(int j = 0 ; j < twoLevelList.size() ; j ++)
				{
					AccountFeeInfo level2accountFeeInfo = twoLevelList.get(j);
					AccountFeeInfo level2accountFeeInfoTemp = new AccountFeeInfo();
					level2accountFeeInfoTemp.setFee(level2accountFeeInfo.getFee());
					level2accountFeeInfoTemp.setName((i + 1) + "." + (j + 1) + "." + level2accountFeeInfo.getName());
					accountListResult.add(level2accountFeeInfoTemp);
					
						//��ҵ����Ϣ
						List<AccountFeeInfo> threeLevelList = getLevelFromList3(accountFeeInfoList,3,level2accountFeeInfo.getCode());
						
						for(int z = 0 ; z < threeLevelList.size() ; z ++)
						{
							AccountFeeInfo level3accountFeeInfo = threeLevelList.get(z);
							AccountFeeInfo level3accountFeeInfoTemp = new AccountFeeInfo();
							level3accountFeeInfoTemp.setFee(level2accountFeeInfo.getFee());
							level3accountFeeInfoTemp.setName((i + 1) + "." + (j + 1) + "." + (z + 1) + "." + level3accountFeeInfo.getName());
							accountListResult.add(level3accountFeeInfoTemp);
						}
				}
			}
		}
		
		Long s2 = System.currentTimeMillis();
		
		System.out.println("Time : " + (s2 - s1));
		
		
		

		
		System.out.println("-------end------");
		System.out.println("-------end1------");
	}
	
	
//	private static Map<String, String> getAccountMapFromList(List<AccountFeeInfo> list){
//		Map<String, String> map = new HashMap<String, String>();
//		
//		if (list != null && list.size() > 0) {
//			int size = list.size();
//			AccountFeeInfo accountFeeInfo = null;
//			for (int i = 0; i < size; i++) {
//				accountFeeInfo = list.get(i);
//				if(accountFeeInfo.getLeval() == 1)
//				{
//					map.put(accountFeeInfo.getCode(), accountFeeInfo.getName() + "--" + accountFeeInfo.getFee());
//				}
//				else
//				{
//					map.put(accountFeeInfo.getSuperCode() + "_" + accountFeeInfo.getCode(), accountFeeInfo.getName() + "--" + accountFeeInfo.getFee());
//				}
//			}
//		}
//		return map;
//	}
	
	
	private static List<AccountFeeInfo> getLevelFromList(List<AccountFeeInfo> list,int level,String superCode)
	{
		
		List<AccountFeeInfo> LevelList = new ArrayList<AccountFeeInfo>(list.size());
		
		if (list != null && list.size() > 0) {
			int size = list.size();
			AccountFeeInfo accountFeeInfo = null;
			for (int i = 0; i < size; i++) {
				accountFeeInfo = list.get(i);
				if(accountFeeInfo.getLeval() == level && accountFeeInfo.getSuperCode().equals(superCode))
				{
					LevelList.add(accountFeeInfo);
				}
			}
			
			Collections.sort(LevelList,new Comparator()
			{
				public int compare(Object o1, Object o2) {
					
					AccountFeeInfo accountFeeInfo1 = (AccountFeeInfo)o1;
					AccountFeeInfo accountFeeInfo2 = (AccountFeeInfo)o2;
					return accountFeeInfo1.getDisplayOrder() - accountFeeInfo2.getDisplayOrder();
				}
				
			});
		}
		return LevelList;
	}
	
	private static List<AccountFeeInfo> getLevelFromList3(List<AccountFeeInfo> list,int level,String superCode)
	{
		
		List<AccountFeeInfo> LevelList = new ArrayList<AccountFeeInfo>(list.size());
		
		if (list != null && list.size() > 0) {
			int size = list.size();
			AccountFeeInfo accountFeeInfo = null;
			for (int i = 0; i < size; i++) {
				accountFeeInfo = list.get(i);
				if(accountFeeInfo.getLeval() == level && accountFeeInfo.getSuperCode().equals(superCode))
				{
					LevelList.add(accountFeeInfo);
				}
			}
			
			Collections.sort(LevelList,new Comparator()
			{
				public int compare(Object o1, Object o2) {
					
					AccountFeeInfo accountFeeInfo1 = (AccountFeeInfo)o1;
					AccountFeeInfo accountFeeInfo2 = (AccountFeeInfo)o2;
					return accountFeeInfo1.getDisplayOrder() - accountFeeInfo2.getDisplayOrder();
				}
				
			});
		}
		return LevelList;
	}
	
	
}
