package com.xwtech.xwecp.service.logic.invocation;


import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.DAOException;
import com.xwtech.xwecp.dao.IPackageChangeDAO;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.CountryNetworkInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY050068Result;
import com.xwtech.xwecp.teletext.BossTeletextUtil;


public class QueryCountryNetworkInfoInvocation extends BaseInvocation implements
		ILogicalService {
	
	private static final Logger logger = Logger.getLogger(QueryCountryNetworkInfoInvocation.class);
		
	private IPackageChangeDAO packageChangeDAO;
	
	public QueryCountryNetworkInfoInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.packageChangeDAO = (IPackageChangeDAO) (springCtx.getBean("packageChangeDAO"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId,ServiceConfig config, List<RequestParameter> params)
	{
		QRY050068Result res = new QRY050068Result();
		String region = (String) getParameters(params,"region");
		String countryId = (String) getParameters(params,"countryId");
		String name = (String) getParameters(params,"name");
		String custId = (String) getParameters(params,"custId");
		
		List<CountryNetworkInfo> couNetInfoList = new ArrayList<CountryNetworkInfo>();
		try {
			if(null != custId && !"".equals(custId))
			{
				couNetInfoList = packageChangeDAO.getCountryInfo(custId);
			}
			else if(null != name && !"".equals(name))
			{
				couNetInfoList = packageChangeDAO.getCountryInfo(region, countryId,name);
			}
			else
			{
				couNetInfoList = packageChangeDAO.getCountryInfo(region, countryId);
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			res.setErrorCode("9999"); 
			res.setErrorMessage("ecp数据库连接异常");
			res.setResultCode("1");
			e.printStackTrace();
			return res;
		}
		res.setCouNetInfoList(couNetInfoList);
		res.setResultCode("0");
		res.setErrorCode("0000");
		if(couNetInfoList.size() == 0)
		{
			res.setErrorMessage("根据提供的地市及区县编码未查询到对应数据");
		}
		return res;
	}	
}