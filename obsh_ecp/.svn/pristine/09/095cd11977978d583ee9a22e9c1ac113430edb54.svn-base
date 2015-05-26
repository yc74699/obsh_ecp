package com.xwtech.xwecp.service.logic.resolver;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.invocation.BaseInvocation;
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;

/**
 * 通用业务查询QRY020001 
 * @author yuantao
 * 2009
 */
public class QrySpuserRegResolver extends BaseInvocation implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(QrySpuserRegResolver.class);
	
	private WellFormedDAO wellFormedDAO;
	
	public QrySpuserRegResolver()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		GommonBusiness dt = null;
		List<GommonBusiness> list = null;
		List<GommonBusiness> reList = new ArrayList();
		List<BossParmDT> bList = null;
		BossParmDT bossDT = null;
		String bizId = "";
		
		String channelName=(String)getParameters(param,"channel");
		try
		{
			QRY020001Result ret = (QRY020001Result)result;
			list = ret.getGommonBusiness();
			
			if (null != param && param.size() > 0)
			{
				for (RequestParameter p : param)
				{
					if (p.getParameterName().equals("bizId"))
					{
						bizId = String.valueOf(p.getParameterValue());
					}
				}
			}
			bList = (List)this.wellFormedDAO.getSubBossParmList(bizId);
			if (null != list && list.size() > 0)
			{
				if (null != bList && bList.size() > 0)
				{   
					boolean YX139 = bizId.length() >= 5 && "139YX".equals(bizId.subSequence(0, 5));//139邮箱
					boolean SMSYX139 = "SMS_139YX5Y".equals(bizId) ||"SMS_139YX20Y".equals(bizId) ||"SMS_139YXMF".equals(bizId) || "SMS_139YX".equals(bizId);
					//特殊处理进行过滤
					if (YX139)  //139邮箱
					{						
						for (BossParmDT bDt : bList)
						{
							boolean flag = true;
							if (null != bDt.getParm3() && !"".equals(bDt.getParm3()))
							{
								for (GommonBusiness gDt : list)
								{
									if (bDt.getParm3().equals(gDt.getReserve2().trim()) && "1".equals(gDt.getReserve1().trim()))
									{
										flag = false;
										gDt.setId(bDt.getBusiNum());
										gDt.setState(2);
										reList.add(gDt);
									}
								}
							}
							if (flag)
							{
								dt = setDefaultValue(bDt);
								reList.add(dt);
							}
						}
					}
					else if (SMSYX139)  //139邮箱
					{						
						for (BossParmDT bDt : bList)
						{
							boolean flag = true;
							if (null != bDt.getParm3() && !"".equals(bDt.getParm3()))
							{
								for (GommonBusiness gDt : list)
								{
									if (bDt.getParm3().equals(gDt.getReserve2().trim()))
									{
										flag = false;
										gDt.setId(bDt.getBusiNum());
										gDt.setState(2);
										reList.add(gDt);
									}
								}
							}
							if (flag)
							{
								dt = setDefaultValue(bDt);
								reList.add(dt);
							}
						}
					}
					else if ("TQP".equalsIgnoreCase(bizId)&&"jsmcc_channel".equals(channelName))  //TQP天气
					{		
						for (BossParmDT bDt : bList)
						{
							boolean flag = true;
								for (GommonBusiness gDt : list)
								{
									
									if (bDt.getParm2().trim().equals(gDt.getReserve1().trim()))
									{
										flag = false;
										gDt.setId(bDt.getBusiNum());
										gDt.setState(2);
										reList.add(gDt);
									}
								}
							
							if (flag)
							{
								dt = setDefaultValue(bDt);
								reList.add(dt);
							}
						}
						
					}
					//号簿管家——网厅
					else if ("HBGJ".equals(bizId))  
					{
						bossDT = (BossParmDT)bList.get(0);
						if (null != bossDT.getParm1() && !"".equals(bossDT.getParm1()))
						{
							for (GommonBusiness g : list)
							{
								if (bossDT.getParm1().equals(g.getReserve2().trim()))
								{
									g.setId(bossDT.getBusiNum());
									g.setState(2);
									reList.add(g);
								}
							}
							
							if (reList.size() == 0)
							{
								dt = setDefaultValue(bossDT);
								reList.add(dt);
							}
						}
						else
						{
							dt = setDefaultValue(bossDT);
							reList.add(dt);
						}
					}
					//短厅号簿管家特殊处理
					else if ("HBGJ_SMS".equals(bizId))  //号簿管家
					{
						bossDT = (BossParmDT)bList.get(0);
						if (null != bossDT.getParm1() && !"".equals(bossDT.getParm1()))
						{
							for (GommonBusiness g : list)
							{
								if (bossDT.getParm1().equals(g.getId().trim()))
								{
									g.setId(bossDT.getBusiNum());
									g.setState(2);
									reList.add(g);
								}
							}
							
							if (reList.size() == 0)
							{
								dt = setDefaultValue(bossDT);
								reList.add(dt);
							}
						}
						else
						{
							dt = setDefaultValue(bossDT);
							reList.add(dt);
						}
					}
					else if (bizId.contains("YDCX"))  //移动彩讯
					{
						for (BossParmDT bDt : bList)
						{
							boolean close = true;
							for (GommonBusiness gDt : list)
							{
								//过滤政务通业务
								if (bDt.getParm1().equals(gDt.getReserve2().trim()) 
										&& bDt.getParm2().equals(gDt.getReserve1()))
								{
									close = false;
									gDt.setId(bDt.getBusiNum());  //设置业务编码
									gDt.setState(2);  //已开通
									reList.add(gDt);
								}
							}
							if (close)
							{
								dt = setDefaultValue(bDt);
								reList.add(dt);
							}
						}
					}
					else if ("SBH".equalsIgnoreCase(bizId))  //世博汇
					{
						for (BossParmDT bDt : bList)
						{
							boolean close = true;
							for (GommonBusiness gDt : list)
							{
								//过滤政务通业务
								if ("-UMGSBBK".equals(gDt.getReserve1().trim()) 
										&& "901808".equals(gDt.getReserve2().trim()))
								{
									close = false;
									gDt.setId(bDt.getBusiNum());  //设置业务编码
									gDt.setState(2);  //已开通
									reList.add(gDt);
								}
							}
							if (close)
							{
								dt = setDefaultValue(bDt);
								reList.add(dt);
							}
						}
					} else if("MYB".equalsIgnoreCase(bizId)) //母婴宝
					{
						for (BossParmDT bDt : bList)
						{
							boolean close = true;
							for (GommonBusiness gDt : list)
							{
								if ("-FYBDG1".equals(gDt.getReserve1().trim()) 
										&& "910193".equals(gDt.getReserve2().trim()))
								{
									close = false;
									gDt.setId(bDt.getBusiNum());  //设置业务编码
									gDt.setState(2);  //已开通
									reList.add(gDt);
								}
							}
							if (close)
							{
								dt = setDefaultValue(bDt);
								reList.add(dt);
							}
						}
					}
					else if("SMS_JKZY".equalsIgnoreCase(bizId)) //健康之友
					{
						for (BossParmDT bDt : bList)
						{
							boolean close = true;
							for (GommonBusiness gDt : list)
							{
								if ("-UMGJKZY".equals(gDt.getReserve1().trim()) 
										&& "901808".equals(gDt.getReserve2().trim()))
								{
									close = false;
									gDt.setId(bDt.getBusiNum());  //设置业务编码
									gDt.setState(2);  //已开通
									reList.add(gDt);
								}
							}
							if (close)
							{
								dt = setDefaultValue(bDt);
								reList.add(dt);
							}
						}
					}
					else if("YDWB".equalsIgnoreCase(bizId)) //移动微博
					{
						for (BossParmDT bDt : bList)
						{
							boolean close = true;
							for (GommonBusiness gDt : list)
							{
								//过滤
								if (bDt.getParm4().equals(gDt.getReserve2().trim()) 
										&& bDt.getParm3().equals(gDt.getReserve1()))
								{
									close = false;
									gDt.setId(bDt.getBusiNum());  //设置业务编码
									gDt.setState(2);  //已开通
									reList.add(gDt);
								}
							}
							if (close)
							{
								dt = new GommonBusiness();
								dt.setId(bDt.getBusiNum());  //设置业务编码
								dt.setState(1);  //未开通
								reList.add(dt);
							}
						}
					}
					//其他所有，通用处理
					else
					{
						for (BossParmDT bDt : bList)
						{
							boolean close = true;
							for (GommonBusiness gDt : list)
							{
								if (bDt.getParm1().equals(gDt.getReserve2().trim()) && bDt.getParm2().equals(gDt.getReserve1().trim()))
								{
									close = false;
									gDt.setId(bDt.getBusiNum());
									reList.add(gDt);
								}
							}
							if (close)
							{
								dt = setDefaultValue(bDt);
								reList.add(dt);
							}
						}
					
					}
				}
			}
			else
			{
				if (bList != null && bList.size() > 0)
				{
					for (BossParmDT bp : bList)
					{
						dt = setDefaultValue(bp);
						reList.add(dt);
					}
				}
				else
				{
					dt = new GommonBusiness();
					dt.setId(bizId);
					dt.setState(1);
					reList.add(dt);
				}
			}
			
			for (GommonBusiness busi : reList)   
			{
				//存在结束时间
				if (null != busi.getEndDate() && !"".equals(busi.getEndDate()))
				{
					busi.setState(4);  //设置状态为：预约关闭
				}
				if("139YX_MFSJYX".equals(busi.getId()) || "139YX_5Y".equals(busi.getId()) ||"139YX_20Y".equals(busi.getId())){
					if (null != busi.getEndDate() && !"".equals(busi.getEndDate())){
						busi.setEndDate("");
						busi.setState(2);
					}
				}
				//短厅139邮箱。开通是以前的boss编码 短厅服务不支持，开通用套餐业务受理接口,
				/**
				 * 	2400000001	139邮箱免费版
					2400000002	139邮箱标准版
					2400000003	139邮箱VIP版
				 */
				if("SMS_139YXMF".equals(busi.getId()) || "SMS_139YX5Y".equals(busi.getId()) ||"SMS_139YX20Y".equals(busi.getId())){
					if (null != busi.getBeginDate() && !"".equals(busi.getBeginDate())){
						if (null != busi.getEndDate() && !"".equals(busi.getEndDate())){
							busi.setEndDate("");
							busi.setState(2);
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

	private GommonBusiness setDefaultValue(BossParmDT bDt) 
	{
		GommonBusiness dt = new GommonBusiness();
		dt.setId(bDt.getBusiNum());
		dt.setState(1);
		return dt;
	}
}
