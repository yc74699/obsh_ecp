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
import com.xwtech.xwecp.service.logic.pojo.DEL010008Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * boss业务办理
 * @author 吴宗德
 * 
 */
public class CrdScoreZonmInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(CrdScoreZonmInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	public CrdScoreZonmInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId, 
			                ServiceConfig config, List<RequestParameter> params){
		DEL010008Result re = new DEL010008Result();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
				
		try{

			//组装发送报文
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_crdcscorezonm_164", params);
			logger.info(" ====== 发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)){
				//发送并接收报文
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_crdcscorezonm_164", this.generateCity(params)));
				logger.info(" ====== 返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml)){
					Element root = this.getElement(rspXml.getBytes());
					
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode)){
						errDt = this.wellFormedDAO.transBossErrCode("DEL010008", "cc_crdcscorezonm_164", errCode);
						if (null != errDt){
							errCode = errDt.getLiErrCode();
							errDesc = errDt.getLiErrMsg();
						}
					}
					re.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
					re.setErrorCode(errCode);
					re.setErrorMessage(errDesc);
				}
			}				
		}catch (Exception e){
			e.printStackTrace();
		}
		return re;
	}
	
}
