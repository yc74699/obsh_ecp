package com.xwtech.xwecp.service.logic.resolver;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 用户套餐信息查询 （国内移动数据套餐）
 * @author yuantao
 * 2010-01-12
 */
public class FindPackageTCResolver implements ITeletextResolver
{
	private static final String GPRSDJB = "GPRSDJB";

	private static final String GNYDSJTC = "GNYDSJTC"; // 国内移动数据功能
	
	private static final String GPRS4GTC = "GPRS4G";   //4G上网流量套餐

	private static final String TY_GPRS4G="TY_GPRS4G"; //返回 国内移动数据功能 和  4G上网流量套餐
	
	private static final Logger logger = Logger.getLogger(FindPackageTCResolver.class);
	
	private WellFormedDAO wellFormedDAO;
	
	private Map<String,String> map;
	
	public FindPackageTCResolver()
	{

		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
		if (null == map)
		{
			map = new HashMap<String,String>();
			//国内移动数据套餐
			map.put("1049", "1049");  
			map.put("1046", "1046");
			map.put("1047", "1047");
			map.put("1048", "1048");
			map.put("1039", "1039");
			map.put("1013", "1013");
			//新加入4G流量套餐的type
			map.put("4009", "4009");
			
		}
	}
	
	
	/**
	 * 实现类
	 */
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
			                      List<RequestParameter> param) throws Exception
	{
		GommonBusiness dt = null;
		List<GommonBusiness> list = null;
		List<GommonBusiness> reList = new ArrayList<GommonBusiness>();
		List<BossParmDT> bList = null;
		RequestParameter reqDT = null;
		String bizId = "";
		
		try
		{
			QRY020001Result ret = (QRY020001Result)result; //得到结果集
			list = ret.getGommonBusiness(); 
			if (null != param && param.size() > 0)
			{
				for (RequestParameter p : param)
				{
					if (p.getParameterName().equals("bizId")) 
					{
						//获取业务编码
						bizId = String.valueOf(p.getParameterValue());//
						
						//GPRS4G与国内移动数据套餐 合并
						if ("TY_GPRS4G".equals(bizId)){
							bList = this.wellFormedDAO.getSubBossParmList("GNYDSJTC");
							List<BossParmDT> cList = null;
							cList = this.wellFormedDAO.getSubBossParmList("GPRS4G");
							bList.addAll(cList);
						}
						else if(!"".equals(bizId))
						{
							//根据业务编码获取其子业务信息
							bList = this.wellFormedDAO.getSubBossParmList(bizId); 
						}
					}
				}
			}
			
			//存在BOSS返回信息
			if (null != list && list.size() > 0)
			{
				//从BOSS返回信息中获取 国内移动数据套餐
				for (GommonBusiness gDt : list)
				{
					//对比 package_type    
					if (null != this.map.get(gDt.getReserve2()))   //
					{
						reList.add(gDt);
					}
					
					//9元包本地长市话150分钟  && 18元包本地长市话300分钟  产品大类1022 ||  渠道： 网与短
					if (    "1022".equals(gDt.getReserve2())&& "2000002300".equals(gDt.getReserve1()) ||
							"1022".equals(gDt.getReserve2())&& "2000002301".equals(gDt.getReserve1()) ||
							"1022".equals(gDt.getReserve2())&& "2300".equals(gDt.getReserve1()) ||
							"1022".equals(gDt.getReserve2())&& "2301".equals(gDt.getReserve1()))
					{
						reList.add(gDt);
					}
				}
			}
			
			//存在已开通套餐
			if (reList !=null && reList.size() > 0)
			{
				//存在子业务
				if ( bList!=null  && bList.size() > 0)
				{
					for (BossParmDT bDt : bList)
					{
						boolean flag = true;  //开通标识			
						for (GommonBusiness gBus : reList)
						{
							if (bDt.getParm2().equals(gBus.getReserve1()))
							{
								//判断开通时间是否小于当前时间，如果小于那么是已经开通状态
								Long onTime=Long.valueOf(String.valueOf(DateTimeUtil.getTodayChar14()));
								Long beginDate=Long.valueOf(String.valueOf(gBus.getBeginDate()));
								if(beginDate<onTime){
									gBus.setState(2);
								}
								//设置接口业务编码
								gBus.setId(bDt.getBusiNum());
								flag = false;
							}
						}				
						//未开通
						if (flag)
						{
							dt = new GommonBusiness ();
							dt.setId(bDt.getBusiNum());  //业务编码
							dt.setName(bDt.getStrBz());  //业务名称
							dt.setState(1);              //状态：未开通
							dt.setReserve1("");
							reList.add(dt);
						}
					}
				}
				else  //无子业务
				{
					for (GommonBusiness gBus : reList)
					{
						//设置接口业务编码
						gBus.setId(bizId);
					}
				}
			}
			else  
			{
				//存在子业务
				if (null != bList && bList.size() > 0)
				{
					for (BossParmDT bDt : bList)
					{
						dt = new GommonBusiness();
						dt.setId(bDt.getBusiNum());  //业务编码
						dt.setName(bDt.getStrBz());  //业务名称
						dt.setState(1);              //状态：未开通
						reList.add(dt);
					}
				}
				else   
				{
					dt = new GommonBusiness();
					dt.setId(bizId);  //业务编码
					dt.setName("");   //业务名称
					dt.setState(1);   //状态：未开通
					reList.add(dt);
				}
			}
			
			if (reList != null && reList.size() > 0)
			{
				for (GommonBusiness busi : reList)
				{
					if (null != busi.getBeginDate() && busi.getBeginDate().equals(DateTimeUtil.getFirstdayOfNextMonth()) ||(null != busi.getBeginDate() && busi.getBeginDate().equals(DateTimeUtil.getNextDayOfMonth()))) {
						busi.setState(3);
					}
					if (null != busi.getEndDate() && !"".equals(busi.getEndDate()))
					{
						busi.setState(4);
					}
				}
			}
			
			//国内移动数据返回结果中。过滤掉1039的其他冗余套餐
			if(reList != null  && reList.size() > 0){
				Iterator it = reList.iterator();
				if(GNYDSJTC.equals(bizId)|| bizId.startsWith(GNYDSJTC))
				{
					while (it.hasNext()) 
					{
						GommonBusiness obj = (GommonBusiness) it.next();
						if (!GNYDSJTC.equals(obj.getId())
								&& !obj.getId().startsWith(GNYDSJTC)) {
							it.remove();
						}
					}
				}
				if(GPRSDJB.equals(bizId)|| bizId.startsWith(GPRSDJB))
				{
					while (it.hasNext()) 
					{
						GommonBusiness obj = (GommonBusiness) it.next();
						if (!GPRSDJB.equals(obj.getId())
								&& !obj.getId().startsWith(GPRSDJB)) 
						{
							it.remove();
						}
					}
				}
				
				if(GPRS4GTC.equals(bizId)|| bizId.startsWith(GPRS4GTC))
				{
					while (it.hasNext()) 
					{
						GommonBusiness obj = (GommonBusiness) it.next();
						if (!GPRS4GTC.equals(obj.getId())
								&& !obj.getId().startsWith(GPRS4GTC)) 
						{
							it.remove();
						}
					}
				}	
					
				
				// GPRS4G 与 国内移动数据套餐集合
				if(TY_GPRS4G.equals(bizId)|| bizId.startsWith(TY_GPRS4G))
				{
					while (it.hasNext()) 
					{
						GommonBusiness obj = (GommonBusiness) it.next();
						if (!GPRS4GTC.equals(obj.getId())
								&& !obj.getId().startsWith(GPRS4GTC)&&!GNYDSJTC.equals(obj.getId())&& !obj.getId().startsWith(GNYDSJTC)) 
						{
							it.remove();
						}
					}
					
				}	
			}
			ret.setGommonBusiness(reList);
			
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
}
