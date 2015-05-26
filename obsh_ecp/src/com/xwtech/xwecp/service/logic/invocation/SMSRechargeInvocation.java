package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;


import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.CommunicateException;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL040049Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 短信充值业务
 * @author 陈小明
 * date 20120924
 */
public class SMSRechargeInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger
			.getLogger(SMSRechargeInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private String BOSSTEMPLATE_CHECK = "cc_chksmsfund";
	
	private String BOSSTEMPLATE_GENFORNUM = "cc_smsfundformnum";
	
	private String BOSSTEMPLATE_APPLY = "cc_recsmsfund";

	public SMSRechargeInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
	}

	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		DEL040049Result res = new DEL040049Result();
		try { 
			// 1.短信充值业务校验
			String oprType = (String) this.getParameters(params, "oprType");
			String attr = (String)this.getParameters(params, "attr");
			if(oprType.equals("1"))
			res = this.checkSMSRecharge(accessId, config,
					params, res, BOSSTEMPLATE_CHECK);
			// 2 生成流水   3 充值
			else if(oprType.equals("2")){
					params.add(new RequestParameter("easyPayType", attr));
					params.add(new RequestParameter("channelid", "4"));
					res = this.generateFornum(accessId, config, params,
							res, BOSSTEMPLATE_GENFORNUM);
			}
			else if(oprType.equals("3")){	
				res = this.checkSMSRecharge(accessId, config,
						params, res, BOSSTEMPLATE_CHECK);
				params.add(new RequestParameter("formNum", attr));
				params.add(new RequestParameter("easyPayType", res.getEasyPayType()));
					res = this.smsRechargeapply(accessId, config, params,
							res, BOSSTEMPLATE_APPLY);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 短信充值业务校验
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected DEL040049Result checkSMSRecharge(final String accessId,
			final ServiceConfig config, final List<RequestParameter> params,
			final DEL040049Result res, final String bossTemplate) {
		String rspXml ;
		try {
			rspXml = callMethod(accessId, params, bossTemplate);
			logger.info(" ====== 创建订单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = getRootEle(rspXml);
				String errCode = getTextByName(root,"resp_code");
				String errDesc = getTextByName(root,"resp_desc"); 
				if(errCode.equals("0000")){
				res.setEasyPayType(root.getChild("response").getChild("content").getChildText("easypay_type").toString());
				res.setCardId(root.getChild("response").getChild("content").getChildText("bank_accid").toString());
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
			}
			else{
				setErrorResult(res);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 短信充值流水生成
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected DEL040049Result generateFornum(final String accessId,
			final ServiceConfig config, final List<RequestParameter> params,
			final DEL040049Result res, final String bossTemplate) {
		String rspXml;
		String outerformnum ="";
		try {
			rspXml = callMethod(accessId, params, bossTemplate);
			logger.info(" ====== 创建订单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = getRootEle(rspXml);
				String resCode = getTextByName(root,"resp_code");
				String errDesc = getTextByName(root,"resp_desc");
				if(resCode.equals(BOSS_SUCCESS))
				{
					 outerformnum = root.getChild("response").getChild("content").getChildText("outerformnum");

				}
				res.setResultCode(BOSS_SUCCESS.equals(resCode) ? LOGIC_SUCESS
						: LOGIC_ERROR);
				res.setErrorCode(resCode);
				res.setFormNum(outerformnum);
				res.setErrorMessage(errDesc);
			}
			else{
				setErrorResult(res);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 短信充值受理
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected DEL040049Result smsRechargeapply(final String accessId,
			final ServiceConfig config, final List<RequestParameter> params,
			final DEL040049Result res, final String bossTemplate) {
		String rspXml;
		try {
			rspXml = callMethod(accessId, params, bossTemplate);
			logger.info(" ====== 创建订单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = getRootEle(rspXml);
				String errCode = root.getChild("response").getChild("content").getChildText("flag");
				String errDesc = root.getChild("response").getChild("content").getChildText("ret_msg");

				res.setResultCode(LOGIC_SUCESS.equals(errCode) ? LOGIC_SUCESS
						: LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
			}
			else{
				setErrorResult(res);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	private String getTextByName(Element root, String textName) {
		String text = root.getChild("response").getChildText(
				textName);
		return text;
	}

	private Element getRootEle(String rspXml) {
		Element root = this.getElement(rspXml.getBytes());
		return root;
	}
	
	private String callMethod(final String accessId,
			final List<RequestParameter> params, final String bossTemplate)
			throws CommunicateException, Exception {
		String reqXml;
		String rspXml;
		reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate, params);
		logger.info(" ====== 创建订单请求报文 ======\n" + reqXml);
		rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml,
				accessId, bossTemplate, super.generateCity(params)));
		return rspXml;
	}
	
}