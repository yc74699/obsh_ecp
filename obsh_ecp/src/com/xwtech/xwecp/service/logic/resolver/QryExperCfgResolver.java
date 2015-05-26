package com.xwtech.xwecp.service.logic.resolver;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;
import com.xwtech.xwecp.service.logic.pojo.ExperCfg;
import com.xwtech.xwecp.service.logic.pojo.QRY050005Result;

/**
 * 业务优惠使用区 查询体验卡配置
 * @author yuantao
 * 2010-01-28
 */
public class QryExperCfgResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(QryExperCfgResolver.class);
	
	private WellFormedDAO wellFormedDAO;
	
	public QryExperCfgResolver()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
			                      List<RequestParameter> param) throws Exception
	{
		QRY050005Result res = (QRY050005Result)result;
		int qryFlag = 0;  //查询类型
		
		try
		{
			if (null != param && param.size() > 0)
			{
				for (RequestParameter p : param)
				{
					if ("qryFlag".equals(p.getParameterName()))
					{
						qryFlag = Integer.parseInt(String.valueOf(p.getParameterValue()));
					}
				}
			}
			//查询网上营业厅可办理的业务信息
			if (1 == qryFlag)
			{
				this.getExperCfg(result, bossResponse, param, res);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
	}
	
	/**
	 * 查询网上营业厅可办理的业务信息
	 * @param result
	 * @param bossResponse
	 * @param param
	 * @param res
	 */
	public void getExperCfg (BaseServiceInvocationResult result, Object bossResponse, 
                             List<RequestParameter> param, QRY050005Result res)
	{
		ExperCfg dt = null;  //业务信息
		List<BossParmDT> bList = null;
		
		try
		{
			//获取子业务信息  体验业务
			bList = this.wellFormedDAO.getSubBossParmList("TY");
			
			List<ExperCfg> list = res.getExperCfg();
			if (null != list && list.size() > 0)
			{
				for (int i = list.size() - 1; i >= 0; i--)
				{
					dt = (ExperCfg)list.get(i);
					// 配置中没有的不显示 todo:"16"
					if (!ArrayUtils.contains(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
							"10", "11", "12", "13", "14", "15", "125" }, dt.getDictCode())) {
						list.remove(i);
						continue;
					}
					// 去掉5：被叫优惠易查询 9：短信回执 10：闪信
					if (ArrayUtils.contains(new String[] { "5", "9", "10" }, dt.getDictCode())) {
						list.remove(i);
						continue;
					}
					//设置业务编码
					for (BossParmDT parm : bList)
					{
						if (parm.getParm1().equals(dt.getDictCode()))
						{
							dt.setDealCode(parm.getBusiNum());
						}
					}
				}
			}
			res.setExperCfg(list);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
	/**
	 * @desc 判断dealCode是否在List列表中，在：true，不在：false
	 * @return boolean
	 */
	private boolean isInArray(List<ExperCfg> experInfoList, String dealCode) {
		boolean b = false;
		if (dealCode != null && experInfoList != null && experInfoList.size() > 0) {
			int size = experInfoList.size();
			for (int i = 0; i < size; i++) {
				if (dealCode.equals(experInfoList.get(i).getDealCode())) {
					b = true;
					break;
				}
			}
		}

		return b;
	}
}
