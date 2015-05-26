package com.xwtech.xwecp.service.logic.resolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;

/**
 * 查询用户附加功能
 * cc_cgetusersvropt _79
 * @author yuantao
 *
 */
public class GetUserSvrOptResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(GetUserSvrOptResolver.class);
	
	private WellFormedDAO wellFormedDAO;
	
	private Map map;
	
	private Map descMap;
	
	public GetUserSvrOptResolver()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		//附加功能字典
		if (null == map)
		{
			map = new HashMap<String, String>();
			map.put("1", "台港澳 国际漫游");
			map.put("2", "省际漫游");
			map.put("3", "省内漫游");
			map.put("4", "短信功能");
			map.put("5", "语音信箱");
			map.put("6", "传真");
			map.put("7", "数据功能");
			map.put("8", "台港澳 国际长权");
			map.put("9", "呼叫转移");
			map.put("10", "呼叫等待");
			map.put("12", "多方通话");
			map.put("13", "来电显示");
			map.put("14", "大众卡本地通");
			map.put("15", "呼叫保持");
			map.put("18", "主叫隐藏");
			map.put("21", "可视电话");
			map.put("2400000280", "4G功能");
			map.put("2100000623", "全网语音信箱业务");
			map.put("2400010059", "国内一卡多号副号");
		}
		
		//附加功能说明
		if (null == descMap)
		{
			descMap = new HashMap<String, String>();
			descMap.put("1", "可在国际及台港澳地区漫游的权限，该功能开通免费");
			descMap.put("2", "可在中国大陆以内其他省市漫游的权限，该功能开通免费");
			descMap.put("3", "可在江苏省内漫游的权限，该功能开通免费");
			descMap.put("4", "发送短消息的功能，该功能开通免费");
			descMap.put("5", "语音信箱业务可用于存储、提取话音留言，该功能开通免费");
			descMap.put("6", "传真");
			descMap.put("7", "是一种通过拨号方式上网的功能，该功能开通免费");
			descMap.put("8", "拨打国际及台港澳地区的长途权限，该功能开通免费");
			descMap.put("9", "将来电转接至另一部电话的功能，该功能开通免费");
			descMap.put("10", "在通话过程中，可以拨打或接听第三方电话，并能在两个通话之间自由切换，该功能开通免费");
			descMap.put("12", "可同时与3-6个（最多6个）客户进行通话的服务，该功能开通免费");
			descMap.put("13", "当国内来话时（台港澳及国际来话除外），使用来电显示业务可以自动显示来话号码，资费：5元/月");
			descMap.put("14", "大众卡本地通");
			descMap.put("15", "与呼叫等待功能同时开通配合使用，使您可以同时在两个通话之间自由切换，该功能开通免费");
			descMap.put("18", "主叫隐藏");
			descMap.put("21", "可采用可视电话呼叫的方式，呼叫对方手机号码，接通后，双方可看到前端的影象和听到对端声音，该功能开通免费。");
			descMap.put("2400000280", "4G功能");
			descMap.put("2100000623", "语音信箱业务是结合智能语音技术，针对被动漏接电话、主动拒接电话这两个客户场景，为您提供留言转接、漏电提醒和非实时信息交互的基础通信业务。资费：3元/月");
			descMap.put("2400010059", "国内一卡多号副号");
		}
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		List<BossParmDT> parList = null;
		List<GommonBusiness> reList = null;
		List<GommonBusiness> list = null;
		GommonBusiness dt = null;
		String bizId = "";
		
		try
		{
			QRY020001Result ret = (QRY020001Result)result;
			list = ret.getGommonBusiness();
			reList = new ArrayList();
			
			for (RequestParameter r : param)
			{
				if ("bizId".equals(r.getParameterName()))
				{
					bizId = String.valueOf(r.getParameterValue());
					//TCJYWCX_FJGN：附加功能 需处理子业务同 附加功能变更
					parList = this.wellFormedDAO.getSubBossParmList(bizId.equals("TCJYWCX_FJGN")?"TCJYWCX_FJGNBG":bizId);
				}
			}
			
			if (null != parList && parList.size() > 0)
			{
				if ("TCJYWCX_FJGN".equals(bizId))  //附加功能
				{
					logger.info("TCJYWCX_FJGN");
					if (null != list && list.size() > 0)
					{
						reList = new ArrayList<GommonBusiness>();
						for (BossParmDT bDT : parList) {
							String busiCode = bDT.getParm1();
							GommonBusiness tBusi = new GommonBusiness();
							tBusi.setId(bDT.getBusiNum());
							tBusi.setReserve1(busiCode);
							tBusi.setName(null == this.map.get(tBusi.getReserve1()) ? "" : String.valueOf(this.map.get(tBusi
									.getReserve1())));
							
							tBusi.setReserve2(null == this.descMap.get(tBusi.getReserve1()) ? "" : String.valueOf(this.descMap.get(tBusi
									.getReserve1())));
							tBusi.setState(4);
							if(!"2400000280".equals(busiCode))
							{//去除4G功能查询行
							reList.add(tBusi);
							}
							
						}
						
						for (GommonBusiness gBusi : list) {
							boolean hasBusi = false;
							for (GommonBusiness tBusi : reList) {
								if (tBusi.getReserve1().equals(gBusi.getReserve1())) {
									hasBusi = true;
									tBusi.setBeginDate(gBusi.getBeginDate());
									tBusi.setEndDate(gBusi.getEndDate());
									tBusi.setState(gBusi.getState());
									break;
								}
							}
							if(!hasBusi){
								gBusi.setName(null == this.map.get(gBusi.getReserve1()) ? "" : String.valueOf(this.map.get(gBusi
										.getReserve1())));
								gBusi.setReserve2(null == this.descMap.get(gBusi.getReserve1()) ? "" : String.valueOf(this.descMap.get(gBusi
										.getReserve1())));
							if(!"2400000280".equals(gBusi.getId()))
							{//去除4G功能查询行
								reList.add(gBusi);
							}
						}
					}
				}
				}
				else
				{
					if (null != list && list.size() > 0)
					{
						reList = new ArrayList<GommonBusiness>();
						for (BossParmDT bDT : parList) {
							String busiCode = bDT.getParm1();
							GommonBusiness tBusi = new GommonBusiness();
							tBusi.setId(bDT.getBusiNum());
							tBusi.setReserve1(busiCode);
							tBusi.setName(null == this.map.get(tBusi.getReserve1()) ? "" : String.valueOf(this.map.get(tBusi
									.getReserve1())));
							tBusi.setReserve2(null == this.descMap.get(tBusi.getReserve1()) ? "" : String.valueOf(this.descMap.get(tBusi
									.getReserve1())));
							tBusi.setState(2);
							reList.add(tBusi);
							
						}
						for (GommonBusiness tBusi : reList) {
							for (GommonBusiness gBusi : list) {
								if (gBusi.getReserve1().equals(
										tBusi.getReserve1())) {
									tBusi.setBeginDate(gBusi.getBeginDate());
									tBusi.setEndDate(gBusi.getEndDate());
									tBusi.setState(gBusi.getState());
//									if(tBusi.getBeginDate().substring(0,8).equals(getNextMonth()))
//									{
//										tBusi.setState(3);
//									}
									tBusi.setReserve2(gBusi.getReserve2());
									break;
								}
							}
						}
					}
					
				}
			}
			else
			{
				reList = list;
			}
			
//			for (GommonBusiness busi : reList)
//			{
//				if (null != busi.getEndDate() && !"".equals(busi.getEndDate()))
//				{
//					busi.setState(4);
//				}
//			}
			
			
			//将crm的state 0：预约1：正常2：暂停3：已退定 转化成ECP的state：0、属于分类，无状态 1、未开通; 2、已开通;3、预约开通  4、预约关闭
			for ( GommonBusiness g: reList){
				int state = g.getState();
				int retState = 0;
				if(0 == state) retState = 3;
				else if (1 == state) retState = 2;
				else if (2 == state) retState = 1;
				else if (3 == state) retState = 4;
				else retState = 0;
				g.setState(retState);
				
			}
			
			
			
			
			
			ret.setGommonBusiness(reList);
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
    }
}
