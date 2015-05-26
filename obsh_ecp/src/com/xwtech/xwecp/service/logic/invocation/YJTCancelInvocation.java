package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
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
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 宜居通退订
 * 2011-12-19
 * @author chenxiaoming_1037
 * 
 */
public class YJTCancelInvocation extends BaseInvocation implements ILogicalService   
{
	
	private static final Logger logger = Logger.getLogger(YJTCancelInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;
	
	private static final String BOSS_INTERFACE_GETSMSCALL = "cc_cgetsmscall_602";
	
	private static final String BOSS_INTERFACE_CANCELINCRM = "cc_cupuserincrem_607";
	
	public YJTCancelInvocation() 
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config,
			List<RequestParameter> params)
	{
		logger.info("del010001executeService");
		DEL010001Result result = new DEL010001Result();
		result.setResultCode(LOGIC_SUCESS);
		String optType = getParameters(params,"oprType").toString();
		if("2".equals(optType))
		{
			try {
				//查询用户否订购增值业务宜居通
				String id = getParameters(params,"id").toString();
				params.add(new RequestParameter(("bizId"),id));
				String rspXml = (String)this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext(BOSS_INTERFACE_GETSMSCALL, params), accessId, BOSS_INTERFACE_GETSMSCALL, this.generateCity(params)));
				if(parseXml(rspXml, result))
				{
					//进行退订
					rspXml = (String)this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext(BOSS_INTERFACE_CANCELINCRM, params), accessId, BOSS_INTERFACE_CANCELINCRM, this.generateCity(params)));
					parseXml(rspXml, result);
				}
			} catch (Exception e)
			{
				logger.error(e, e);
			}
		}
		
		return result;
	}
	
	/**
	 * 解析Xml
	 */
	private boolean parseXml(String rspXml, DEL010001Result result) throws DocumentException 
	{
		Element root = this.getElement(rspXml.getBytes());
		String resp_code = root.getChild("response").getChildText("resp_code");
		String resp_desc = root.getChild("response").getChildText("resp_desc");
		String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
		result.setResultCode(resultCode);
		result.setErrorCode(resp_code);
		result.setErrorMessage(resp_desc);
		if (BOSS_SUCCESS.equals(resp_code))
		{
			return true;
		}
		return false;
	}
}
