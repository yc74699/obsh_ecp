package com.xwtech.xwecp.service.logic.resolver;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;
import com.xwtech.xwecp.service.logic.pojo.QRY050026Result;
import com.xwtech.xwecp.service.logic.pojo.SelfDonate;

/**
 * 用户赠送产品信息查询 QRY050026
 * @author yuantao
 * 2010-01-26
 */
public class QrySelfDonateGiftResolver implements ITeletextResolver
{
	private WellFormedDAO wellFormedDAO;
	
	private static final Logger logger = Logger.getLogger(QrySelfDonateGiftResolver.class);
	
	public QrySelfDonateGiftResolver(){
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, List<RequestParameter> param) throws Exception
	{
		String id = "";  //业务编码
		List<SelfDonate> list = null;  //返回列表
		List<BossParmDT> bList = null;
		
		try
		{
			QRY050026Result res = (QRY050026Result)result;
			list = res.getSelfDonate();
			//获取参数 业务编码
			if (null != param && param.size() > 0)
			{
				for (RequestParameter p : param)
				{
					if ("id".equals(p.getParameterName()))
					{
						id = String.valueOf(p.getParameterValue());
					}
				}
			}
			
			if (null != list && list.size() > 0)
			{
				if (null != id && !"".equals(id))
				{
					bList = (List)this.wellFormedDAO.getSubBossParmList(id);
				}
				
				if (null != bList && bList.size() > 0)
				{
					for (BossParmDT bDt : bList)
					{
						Iterator it = list.iterator();
						SelfDonate sd = null;
						while (it.hasNext()) {
							sd = (SelfDonate)it.next();
							
							if (sd.getPrdCode().equals(bDt.getParm3())) {
								sd.setPrdCode(bDt.getBusiNum());
							} else {
								it.remove();
							}
						}
					}
					res.setSelfDonate(list);
				}
			}
			//体坛风云赠送
//			if ("TTFY_TTFYZS".equals(id))
//			{
//				reList = new ArrayList<SelfDonate>();
//				for (SelfDonate dt : res.getSelfDonate())
//				{
//					if ("930120".equals(dt.getPrdCode()))
//					{
//						dt.setPrdCode("TTFY_TTFYZS");  //设置业务编码
//						reList.add(dt);
//					}
//				}
//				res.setSelfDonate(reList);
//			}
			
			// 生活小贴士
//			String tipsCode = "SHXTS";
//			if (id.indexOf(tipsCode) > -1) {
//				reList = new ArrayList<SelfDonate>();
//				
//				List<BossParmDT> parList = (List<BossParmDT>) wellFormedDAO.getSubBossParmList(tipsCode);
//				if (parList != null && parList.size() > 0) {
//					for (BossParmDT par : parList) {
//						String pinyin = par.getBusiNum();
//						String bossImp = par.getParm1();
//						String number = par.getParm3();
//						for (SelfDonate dt : res.getSelfDonate()) {
//							String prdCode = dt.getPrdCode();
//							if (prdCode.equals(number)) {
//								dt.setPinyin(pinyin);
//								dt.setBossImp(bossImp);
//								dt.setPrdCode(number);
//								reList.add(dt);
//							}
//						}
//					}
//				}
//				
//				res.setSelfDonate(reList);
//			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
}
