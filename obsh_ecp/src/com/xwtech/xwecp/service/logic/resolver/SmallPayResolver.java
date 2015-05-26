package com.xwtech.xwecp.service.logic.resolver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.QRY010030Result;
import com.xwtech.xwecp.service.logic.pojo.QryDetail;
import com.xwtech.xwecp.util.StringUtil;

/**
 * 后置请求转换厘为元
 * @author xufan
 * 2014-03-14
 */
public class SmallPayResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(SmallPayResolver.class);
	
	private WellFormedDAO wellFormedDAO;
	
	public SmallPayResolver()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, List<RequestParameter> param) throws Exception
	{
		QRY010030Result res = (QRY010030Result)result;
		List<QryDetail> listQryDetail = res.getQryDetail();  //获取明细
		
		try
		{
			if(listQryDetail.size()==0){
//				res.setLimitfee(StringUtil.mobileFront(res.getLimitfee()));
//				res.setUsefee(StringUtil.mobileFront(res.getUsefee()));
//				res.setPayfee(StringUtil.mobileFront(res.getPayfee()));
//				res.setBalance(StringUtil.mobileFront(res.getBalance()));
			}else{
				for(QryDetail detail : listQryDetail){
					detail.setPayfee(StringUtil.mobileFront(detail.getPayfee()));
				}
				res.setQryDetail(listQryDetail);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		
	}
}
