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
import com.xwtech.xwecp.service.logic.pojo.DEL090003Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;



/**
 * boss业务办理
 * @author 汪洪广
 * 
 */
public class ActChkAndDealByImsiInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(ActChkAndDealByImsiInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	public ActChkAndDealByImsiInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId, 
			                ServiceConfig config, List<RequestParameter> params){
		DEL090003Result res = new DEL090003Result();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		String oprType = (String) getParameters(params, "oprType");
		
				
		try{
			//三码合一营销案校验与办理之办理
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			if("1".equals(oprType)){
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_terminalactivitycheck", params);
			logger.debug(" ====== 发送报文 ======\n" + reqXml);
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(reqXml, accessId, "cc_terminalactivitycheck", this.generateCity(params)));
			logger.debug(" ====== 应答报文 ======\n" + rspXml);
			if(null != rspXml && !"".equals(rspXml)){
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL090003", "cc_terminalactivitycheck", errCode);
					if (null != errDt)
					{
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
						res.setErrorCode(errCode);
						res.setErrorMessage(errDesc);
					}
				}
			}
			}else if("2".equals(oprType)){
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_terminalactivityapply", params);
				logger.debug(" ====== 发送报文 ======\n" + reqXml);
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_terminalactivityapply", this.generateCity(params)));
				logger.debug(" ====== 应答报文 ======\n" + rspXml);
				if(null != rspXml && !"".equals(rspXml)){
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("DEL090003", "cc_terminalactivityapply", errCode);
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
		}catch (Exception e){
			e.printStackTrace();
		}
		return res;
	}
	
}
