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
import com.xwtech.xwecp.service.logic.pojo.DEL040057Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

public class OperLoversVoiceInvocation extends BaseInvocation implements
		ILogicalService {
	private static final Logger logger = Logger.getLogger(OperLoversVoiceInvocation.class);
	private BossTeletextUtil bossTeletextUtil;
	private IRemote remote;
	private WellFormedDAO wellFormedDAO;
	
	private static final String ATTR ="p100090";
	private static final String EQUALS = "=";
	private String template = "cc_CurrentProRec_65";
	
	
    public  OperLoversVoiceInvocation(){
    	ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
    }
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		DEL040057Result res = new DEL040057Result();
		String oprType = "";
		String append = "";
		
			if (null != params && params.size() > 0) {

				for (RequestParameter p : params) {
					if ("oprType".equals(p.getParameterName())) {
						oprType = String.valueOf(p.getParameterValue());
					}
				}
			}
			//情侣语音包 开通
			if("1" .equals(oprType)){
				for (RequestParameter p : params) {
					if ("mPhoneNum".equals(p.getParameterName())) {
						append = this.parseAppendAttr(String.valueOf(p.getParameterValue()));
					}
				}
				setParameter(params, "append", append);
				getTemplate(oprType);
				operLovesVoice(res,params,accessId);
			}else{
				getTemplate(oprType);
				operLovesVoice(res,params,accessId);
			}
		return res;
	}
	
	private void operLovesVoice(DEL040057Result res,List<RequestParameter> params ,String accessId){
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		try{
			reqXml = this.bossTeletextUtil.mergeTeletext(template, params);
			logger.debug(" ====== 情侣语音套餐 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, template, super.generateCity(params)));
				logger.debug(" ====== 情侣语音套餐 接收报文 ====== \n" + rspXml);
				if (null != rspXml && !"".equals(rspXml)) 
				{
					root = this.getElement(rspXml.getBytes());
					resp_code = root.getChild("response").getChildText("resp_code");
					String resp_desc = root.getChild("response").getChildText("resp_desc");
					String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;

					if (!LOGIC_SUCESS.equals(resultCode)){
						res.setResultCode(resultCode);
						res.setErrorCode(resp_code);
						res.setErrorMessage(resp_desc);
					}
					else{
						res.setResultCode(resultCode);
						res.setErrorCode(resp_code);
						res.setErrorMessage(resp_desc);
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e,e);
		}
	}
	
	
	private  String parseAppendAttr(String sub){
		StringBuffer append = new StringBuffer();
		append.append(ATTR).append(EQUALS).append(sub);
		return append.toString();
	}
	private void getTemplate(String oprType) 
	{
		if(!"1".equals(oprType))
		{
			template = "cc_cupuserincrem_607_boss";
		}
	}

}
