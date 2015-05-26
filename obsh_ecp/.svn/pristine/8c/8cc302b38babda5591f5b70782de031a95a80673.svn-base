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
import com.xwtech.xwecp.util.DateTimeUtil;

public class GetShadowMemberResolver implements ITeletextResolver{
	
    private static final Logger logger = Logger.getLogger(GetShadowMemberResolver.class);
	private WellFormedDAO wellFormedDAO;
	
	public GetShadowMemberResolver()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, List<RequestParameter> param) throws Exception {
		List<GommonBusiness> list = null;
		List<GommonBusiness> listNew = new ArrayList<GommonBusiness>();
		List<BossParmDT> bList = null;
		QRY020001Result ret = (QRY020001Result)result;
		String bizId = "";
		try
		  {
		   if (null != param && param.size() > 0)
		   {
			for (RequestParameter p : param)
			{
				if (p.getParameterName().equals("bizId"))
				{
					bizId = String.valueOf(p.getParameterValue());
					if (!"".equals(bizId))
					{
						bList = this.wellFormedDAO.getSubBossParmList(bizId);
						break;
					}
				}
			}
		}
		list = ret.getGommonBusiness();
	
		for(BossParmDT bt :bList)
		{
			getCode(list,bt.getBusiNum(),listNew);
		}
		
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		ret.setGommonBusiness(listNew);
	}
	//过滤返回报文中与文影俱乐部无关的业务，相同产品取最
	public void getCode(List<GommonBusiness> list,String bizId,List<GommonBusiness> listNew){
		GommonBusiness gb = new GommonBusiness();
		Map <String,String> map = new HashMap<String,String>();
		List<GommonBusiness> lis = new ArrayList<GommonBusiness>();
		map.put("WYJLBTYHY", "2380000191");   map.put("WYJLBCJHY", "2380000192");    map.put("WYJLBZJHY", "2380000193");  map.put("WYJLBGJHY", "2380000170");
		String code = map.get(bizId);
		
		for( int i = 0 ; i < list.size() ; i ++){
			String packCode = list.get(i).getId();
			if(code.equals(packCode))
			{
			  int state =getState(list.get(i).getBeginDate(),list.get(i).getEndDate());
			  gb.setState(state);
			  gb.setId(bizId);
			  if( 1 != state)
			  {
				  gb.setBeginDate(list.get(i).getBeginDate());
				  gb.setEndDate(list.get(i).getEndDate());
			  }
		      lis.add(gb);
			} 
		}
		if(lis.size() > 1)
		{
			listNew.add(lis.get(lis.size()-1));	
		}
		else if(lis.size() == 0)
		{
			gb.setId(bizId);
			gb.setState(1);
			listNew.add(gb);
		}
		else
		{
			listNew.add(lis.get(0));
		}
	}
	//判断无影俱乐部会员开通状态
	public int getState(String beginDate,String endDate){
		DateTimeUtil da = new DateTimeUtil();
		int state = 1;
		Long nowDate =  da.formatDate(da.getTodayChar14());
		Long beginDateL = da.formatDate(beginDate);	
		Long endDateL = da.formatDate(endDate);
		
		if(nowDate > endDateL && 0 != endDateL )
		{
			state = 1;
		}
		else if(nowDate >= beginDateL && endDateL == 0)
		{
			state = 2;
		}
		else if(nowDate < beginDateL)
		{
			state = 3;
		}
		else if(nowDate < endDateL && nowDate >= beginDateL)
		{
			state = 4;
		}
		
		return state;
	}
	
	

}
