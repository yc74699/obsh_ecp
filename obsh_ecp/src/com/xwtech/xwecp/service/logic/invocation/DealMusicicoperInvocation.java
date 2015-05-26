package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
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
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 
 * 
 * @author 汪洪广
 *
 */
public class DealMusicicoperInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(SuppendResumeInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;


	public DealMusicicoperInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		DEL010001Result res = new DEL010001Result();
	
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		for (RequestParameter parameter : params) {
			String pName = parameter.getParameterName();
			if (pName.equals("oprType")) {
				if(parameter.getParameterValue().toString().equals("1")){
					parameter.setParameterValue("6");
				}else if (parameter.getParameterValue().toString().equals("2")){
					parameter.setParameterValue("7");
				}
			}
		}
		try
		{
			String bossTemplate = "cc_cdealmusicoper";
			reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate, params);
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, bossTemplate, this.generateCity(params)));
			
			if(null !=rspXml && !"".equals(rspXml)){
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL010001", bossTemplate, errCode);
					if (null != errDt)
					{
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
						res.setErrorCode(errCode);
						res.setErrorMessage(errDesc);
					}
				}		
			}
	}
		catch(Exception e){
			logger.error(e,e);
		}
		return  res;
	}
}
