package com.xwtech.xwecp.service.logic.resolver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;
import com.xwtech.xwecp.service.logic.pojo.PackageBizId;
import com.xwtech.xwecp.service.logic.pojo.PkgDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY050062Result;

public class QryPkgDetailInfoResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(QryPkgDetailInfoResolver.class);
	
	private WellFormedDAO wellFormedDAO;
	
	public QryPkgDetailInfoResolver()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		List<PkgDetail> lists = null;
		try
		{
			QRY050062Result ret = (QRY050062Result)result;
			lists = ret.getPkgDetailList();
			if(null != lists && lists.size() > 0)
			{
				for(PkgDetail pd : lists)
				{
					pd.setPkgId(resetPkgId(pd.getPkgId(),param));
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }

	private String resetPkgId(String pkgId, List<RequestParameter> param) 
	{
		List<PackageBizId> bizIds = (List) getParameters(param, "packageBizIds");
		for (PackageBizId pkgBizId : bizIds) 
		{
			BossParmDT dt = wellFormedDAO.getBossParm(pkgBizId.getBizId());
			if(null != dt )
			{
				if (dt.getParm2().equals(pkgId))
				{
					return pkgBizId.getBizId();
				}	
			}
		}
		return null;
	}

	protected Object getParameters(final List<RequestParameter> params, String parameterName) {
		Object rtnVal = null;
		for (RequestParameter parameter : params) {
			String pName = parameter.getParameterName();
			if (pName.equals(parameterName)) {
				rtnVal = parameter.getParameterValue();
				break;
			}
		}
		return rtnVal;
	}
		
}