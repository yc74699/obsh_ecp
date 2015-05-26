package com.xwtech.xwecp.service.logic.invocation;


import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.DEL040050Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 关联号码设置
 * 
 * @author 汪洪广
 * 
 */
public class SetRelationNumInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(SetRelationNumInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;


	
	private ParseXmlConfig config;

	public SetRelationNumInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) 
	{
		DEL040050Result res = new DEL040050Result();
		
		String reqXml = "";
		String rspXml = "";
		String relationSubSid="";
		String relationMsisdn="";
		String relatSetType="";
		StringBuilder relationBuf = new StringBuilder();
	
		
		try{
			BaseResult chkResult =this.getRelationSubsid(accessId, config, params );
			//设置关联号码之前需要获得关联Id
			if (!LOGIC_SUCESS.equals(chkResult.getResultCode())) {
				res.setResultCode(chkResult.getResultCode());
				res.setBossCode(chkResult.getBossCode());
				res.setErrorMessage(chkResult.getErrorMessage());
				return res;
			}
			
			//成功生成subid。则调用变更或者开通接口
			relationSubSid = (String)chkResult.getReObj();
			for (RequestParameter parameter : params) {
				String pName = parameter.getParameterName();
				if (pName.equals("oprType")) {
					if(parameter.getParameterValue().toString().equals("1")){
						parameter.setParameterValue("PCOpRec");
					}else if (parameter.getParameterValue().toString().equals("2")){
						parameter.setParameterValue("PCOpMod");
					}
					relatSetType = parameter.getParameterValue().toString();
				}
				if(pName.equals("relateNum")){
					relationMsisdn=parameter.getParameterValue().toString();
				}
				if(pName.equals("prodAttr")){
					relationBuf.append("RelSubsID=");
					relationBuf.append(relationSubSid);
					relationBuf.append("="+relatSetType+"#RelNumber=");
					relationBuf.append(relationMsisdn);
					relationBuf.append("="+relatSetType);
					parameter.setParameterValue(relationBuf.toString());
				}
			}
			
					reqXml = this.bossTeletextUtil.mergeTeletext("cc_chgrelateservnumber", params);
					logger.info(reqXml);
					rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, 
							"cc_chgrelateservnumber", this.generateCity(params)));
					logger.info(rspXml);
					if (null != rspXml && !"".equals(rspXml)) {
						Element root = this.getElement(rspXml.getBytes());
						String resp_code = root.getChild("response").getChildText(
								"resp_code");
						res.setResultCode(BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS
								: LOGIC_ERROR);
						if(!"0000".equals(resp_code)){
							res.setErrorCode(resp_code);
							res.setErrorMessage(this.config.getChildText(
									this.config.getElement(root, "response"),
									"resp_desc"));
						}	
					}
		}
		catch(Exception e){
			logger.error(e,e);
		}
		return  res;

	}
	protected BaseResult getRelationSubsid(String accessId, ServiceConfig config, List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		String relationSubSid = "";

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_chkcanbemifilinknum", params);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_chkcanbemifilinknum", super.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL040050", "cc_chkcanbemifilinknum", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath.newInstance("/operation_out/response/content/relate_subsid");
					relationSubSid = ((Element) xpath.selectSingleNode(root)).getText();
					res.setReObj(relationSubSid);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

}