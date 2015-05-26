package com.xwtech.xwecp.service.logic.resolver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
 * 查询用户增值业务信息
 * @author wanghongguang
 *
 */
public class GetSmsCallBodyGuardResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(GetSmsCallBodyGuardResolver.class);
	
	private WellFormedDAO wellFormedDAO;
	
	
	public GetSmsCallBodyGuardResolver(){
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		GommonBusiness dt = null;
		List<GommonBusiness> list = null;
		List<GommonBusiness> reList = new ArrayList<GommonBusiness>();
		String bizId = "";
		List<BossParmDT> bList = null;

		try
		{
 			QRY020001Result ret = (QRY020001Result)result;
			list = ret.getGommonBusiness();
			if (null != param && param.size()>0)
			{
				for (RequestParameter p : param)
				{
					if (p.getParameterName().equals("bizId"))
					{
						//获取业务编码
						bizId = String.valueOf(p.getParameterValue());
						bList = this.wellFormedDAO.getSubBossParmList(bizId);
						break;
					}
				}
				
				if("-2727".equals(ret.getBossCode()))
				{
					ret.setResultCode("0");
					ret.setErrorCode("");
					ret.setErrorMessage("");
					if(null != bList && bList.size()>0)
					{
						for(BossParmDT gg : bList)
						{
							dt = new GommonBusiness();
							dt.setId(gg.getBusiNum());
							dt.setState(1);
							dt.setEndDate("");
							dt.setEndDate("");
							dt.setName("");
							reList.add(dt);
							ret.setGommonBusiness(list);
						}
					}
				}
				else
				{
					if(null !=bList && bList.size()>0 && null != list && list.size()>0 )
					{
						for(BossParmDT bp : bList)
						{
							boolean flag = true; 
							for(GommonBusiness gb: list)
							{
								if(gb.getId().equals(bp.getParm1()) || bp.getParm1().endsWith(gb.getId()))
								{
									flag = false;
									dt = new GommonBusiness();
									dt.setId(bp.busiNum);
									dt.setBeginDate(gb.getBeginDate());
									dt.setEndDate(gb.getEndDate());
									dt.setName(gb.getName());
									if(gb.getBeginDate().equals(getFirstdayOfNextMonth())
											|| gb.getBeginDate().equals(getNextDayOfMonth()) )
										dt.setState(3);
									else
										dt.setState(2);
									 if( !gb.getEndDate().equals(""))
										dt.setState(4);
										reList.add(dt);
								}
							}
							if(flag)
							{
								dt= new GommonBusiness();
								dt.setId(bp.busiNum);
								dt.setBeginDate("");
								dt.setEndDate("");
								dt.setState(1);
								reList.add(dt);
							}
						}
					}
				}
					ret.setGommonBusiness(reList);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }

	private String getNextDayOfMonth() {
		String str = "";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		str = sf.format(cal.getTime());
		str += "000000";
		return str;
	}

	private String getFirstdayOfNextMonth() {
		String str = "";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		str = sf.format(cal.getTime());
		str += "000000";
		return str;
	}
}