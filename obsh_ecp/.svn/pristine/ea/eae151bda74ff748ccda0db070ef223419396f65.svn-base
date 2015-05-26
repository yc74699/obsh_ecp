package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL010001Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY020005Result;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class QryExperimentBusInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(QryExperimentBusInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private ParseXmlConfig config;
	
	public QryExperimentBusInvocation ()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY020005Result res = new QRY020005Result ();
		
		try
		{
			
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		return res;
	}
	
	
	
	/**
	 * 设置结果信息
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @param resp_code
	 */
	public void getErrInfo (String accessId, ServiceConfig config, 
                            List<RequestParameter> params, DEL010001Result res, 
                            String resp_code, String resp_desc, String xmlName)
	{
		ErrorMapping errDt = null;  //错误编码解析
		
		try
		{
			//设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code)?"0":"1");
			
			//失败
			if (!"0000".equals(resp_code))
			{
				//解析错误信息
				errDt = this.wellFormedDAO.transBossErrCode("QRY020005", xmlName, resp_code);
				if (null != errDt)
				{
					resp_code = errDt.getLiErrCode();  //设置错误编码
					resp_desc = errDt.getLiErrMsg();  //设置错误信息
				}
				res.setErrorCode(resp_code);  //设置错误编码
				res.setErrorMessage(resp_desc);  //设置错误信息
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
}
