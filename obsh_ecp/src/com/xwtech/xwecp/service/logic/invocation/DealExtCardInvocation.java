package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.DEL040020Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 业务优惠使用区 优惠业务免费体验、中止
 * @author 吴宗德
 * 
 */
public class DealExtCardInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(DealExtCardInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public DealExtCardInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId, 
			                ServiceConfig config, List<RequestParameter> params)
	{
		DEL040020Result res = new DEL040020Result();
		String cardId = "";
		int oprType = 0;
		try
		{
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			if (params != null && params.size() > 0) {
				for (RequestParameter param:params) {
					if ("cardId".equals(param.getParameterName())) {
						cardId = (String)param.getParameterValue();
					}
					//1：体验申请 2：体验中止
					if ("oprType".equals(param.getParameterName())) {
						oprType = Integer.parseInt(param.getParameterValue().toString());
					}
				}
			}
			//体验申请
			if (oprType == 1) {
				BaseResult exeCardDealRet = this.extCardDeal(accessId, config, params);
				if (!LOGIC_SUCESS.equals(exeCardDealRet.getResultCode())) {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(exeCardDealRet.getErrorCode());
					res.setErrorMessage(exeCardDealRet.getErrorMessage());
				}
			} else {
				if ("100107".equals(cardId)) {
					List<RequestParameter> copyParam = this.copyParam(params);
					copyParam.add(new RequestParameter("id", "LLF_CXLLFKTTY"));
					copyParam.add(new RequestParameter("oprType", "2"));
					copyParam.add(new RequestParameter("chooseFlag", "1"));
					
					BaseResult dealSpChgRet = this.dealSpChg(accessId, config, copyParam);
					if (LOGIC_SUCESS.equals(dealSpChgRet.getResultCode())) {
						BaseResult excardBreakRet = this.excardBreak(accessId, config, params);
						if (!LOGIC_SUCESS.equals(excardBreakRet.getResultCode())) {
							res.setResultCode(LOGIC_ERROR);
							res.setErrorCode(excardBreakRet.getErrorCode());
							res.setErrorMessage(excardBreakRet.getErrorMessage());
						}
					} else {
						res.setResultCode(LOGIC_ERROR);
						res.setErrorCode(dealSpChgRet.getErrorCode());
						res.setErrorMessage(dealSpChgRet.getErrorMessage());
					}
				} else {
					BaseResult excardBreakRet = this.excardBreak(accessId, config, params);
					if (!LOGIC_SUCESS.equals(excardBreakRet.getResultCode())) {
						res.setResultCode(LOGIC_ERROR);
						res.setErrorCode(excardBreakRet.getErrorCode());
						res.setErrorMessage(excardBreakRet.getErrorMessage());
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 体验申请
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult extCardDeal(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_excarddeal_315", params);

			logger.debug(" ====== 业务优惠使用区 体验申请 请求报文 ======\n" + reqXml);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_excarddeal_315", this.generateCity(params)));
			logger.debug(" ====== 业务优惠使用区 体验申请 返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
//				String errCode = root.getChild("content").getChildText("ret_code");
//				String errDesc = root.getChild("content").getChildText("ret_msg");
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL040020", "cc_excarddeal_315", errCode);
					if (null != errDt)
					{
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 体验中止
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult excardBreak(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String cardId = "";
		ExperienceInfoBean itemBean = null;
		try {
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			for (RequestParameter param:params) {
				if ("cardId".equals(param.getParameterName())) {
					cardId = (String)param.getParameterValue();
				}
			}
			
			BaseResult userXpListRet = this.queryUserXp(accessId, config, params);
			if (LOGIC_SUCESS.equals(userXpListRet.getResultCode())) {
				List<ExperienceInfoBean> userXpList = (List<ExperienceInfoBean>)userXpListRet.getReObj();
				if (userXpList != null && userXpList.size() > 0) {
					for (ExperienceInfoBean expInfo:userXpList) {
						if (cardId.equals(expInfo.getCardId())) {
							itemBean = expInfo;
							break;
						}
					}
					
					if (itemBean != null) {
						List<RequestParameter> copyParam = this.copyParam(params);
						
						copyParam.add(new RequestParameter("user_id", itemBean.getUserId()));
						copyParam.add(new RequestParameter("deal_code", itemBean.getDealCode()));
						copyParam.add(new RequestParameter("deal_type", itemBean.getDealType()));
						copyParam.add(new RequestParameter("use_date", itemBean.getUseDate()));
						copyParam.add(new RequestParameter("end_date", itemBean.getEndDate()));
						copyParam.add(new RequestParameter("is_remind", itemBean.getIsRemind()));
						copyParam.add(new RequestParameter("remark", itemBean.getRemark()));
						copyParam.add(new RequestParameter("sms_flag", itemBean.getSmsFlag()));
						copyParam.add(new RequestParameter("card_id", itemBean.getCardId()));
						copyParam.add(new RequestParameter("open_flag", itemBean.getOpenFlag()));
						copyParam.add(new RequestParameter("operating_srl", itemBean.getOperatingSrl()));
						
						BaseResult tranExcardBreakRet = this.tranExcardBreak(accessId, config, copyParam);
						if (!LOGIC_SUCESS.equals(tranExcardBreakRet.getResultCode())) {
							res.setResultCode(LOGIC_ERROR);
							res.setErrorCode(tranExcardBreakRet.getErrorCode());
							res.setErrorMessage(tranExcardBreakRet.getErrorMessage());
						}
					} else {
						logger.debug("============================未查询到要中断的体验业务!!!!");
					}
				} else {
					logger.debug("================================未查询到体验任何业务!!!!");
				}
			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(userXpListRet.getErrorCode());
				res.setErrorMessage(userXpListRet.getErrorMessage());
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 体验中止-boss
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult tranExcardBreak(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_excardbreak_817", params);

			logger.debug(" ====== 业务优惠使用区 体验中止 请求报文 ======\n" + reqXml);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_excardbreak_817", this.generateCity(params)));
			logger.debug(" ====== 业务优惠使用区 体验中止 返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL040020", "cc_excardbreak_817", errCode);
					if (null != errDt)
					{
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 自有业务受理-关闭彩信连连发
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult dealSpChg(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cdealspchg_71", params);

			logger.debug(" ====== 业务优惠使用区 关闭彩信连连发 请求报文 ======\n" + reqXml);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cdealspchg_71", this.generateCity(params)));
			logger.debug(" ====== 业务优惠使用区 关闭彩信连连发 返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL040020", "cc_cdealspchg_71", errCode);
					if (null != errDt)
					{
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 查询用户正在体验的新业务
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult queryUserXp(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
//		String key = "PRO_CFG_LIST_";
		List<ExperienceInfoBean> userXpList = null;
		try {
//			for (RequestParameter param:params) {
//				if ("city".equals(param.getParameterName())) {
//					key += param.getParameterValue();
//				}
//			}
//			
//			Object obj = this.wellFormedDAO.getCache().get(key);
//			if(obj != null && obj instanceof List)
//			{
//				proCfgList = (List<CcCGetProByCityBean>)obj;
//			} else {
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqueryuserxp_314", params);
				
				logger.debug(" ====== 查询用户正在体验的新业务请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cqueryuserxp_314", this.generateCity(params)));
				logger.debug(" ====== 查询用户正在体验的新业务返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("DEL040020", "cc_cqueryuserxp_314", errCode);
						if (null != errDt)
						{
							errCode = errDt.getLiErrCode();
							errDesc = errDt.getLiErrMsg();
						}
					}
					res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					
					if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
						List userexperienceinfo_user_id = null;
						try
						{
							userexperienceinfo_user_id = root.getChild("content").getChildren("userexperienceinfo_user_id");
						}
						catch (Exception e)
						{
							userexperienceinfo_user_id = null;
						}
						if (null != userexperienceinfo_user_id && userexperienceinfo_user_id.size() > 0) {
							userXpList = new ArrayList<ExperienceInfoBean>(userexperienceinfo_user_id.size());
							ExperienceInfoBean bean = null;
							for (int i = 0; i < userexperienceinfo_user_id.size(); i++)
							{
								bean = new ExperienceInfoBean();
								Element cuserexperienceinfodt = ((Element)userexperienceinfo_user_id.get(i)).getChild("cuserexperienceinfodt");
								if (null != cuserexperienceinfodt)
								{
				                	bean.setUserId(p.matcher(cuserexperienceinfodt.getChildText("userexperienceinfo_user_id")).replaceAll(""));
									bean.setDealCode(p.matcher(cuserexperienceinfodt.getChildText("userexperienceinfo_deal_code")).replaceAll(""));
									bean.setDealType(p.matcher(cuserexperienceinfodt.getChildText("userexperienceinfo_deal_type")).replaceAll(""));
									bean.setUseDate(p.matcher(cuserexperienceinfodt.getChildText("userexperienceinfo_use_date")).replaceAll(""));
									bean.setEndDate(p.matcher(cuserexperienceinfodt.getChildText("userexperienceinfo_end_date")).replaceAll(""));
									bean.setIsRemind(p.matcher(cuserexperienceinfodt.getChildText("userexperienceinfo_is_remind")).replaceAll(""));
									bean.setRemark(p.matcher(cuserexperienceinfodt.getChildText("userexperienceinfo_remark")).replaceAll(""));
									bean.setSmsFlag(p.matcher(cuserexperienceinfodt.getChildText("userexperienceinfo_sms_flag")).replaceAll(""));
									bean.setCardId(p.matcher(cuserexperienceinfodt.getChildText("userexperienceinfo_card_id")).replaceAll(""));
									bean.setOpenFlag(p.matcher(cuserexperienceinfodt.getChildText("userexperienceinfo_open_flag")).replaceAll(""));
									bean.setOperatingSrl(p.matcher(cuserexperienceinfodt.getChildText("userexperienceinfo_operating_srl")).replaceAll(""));
									bean.setDictCode(p.matcher(cuserexperienceinfodt.getChildText("userexperienceinfo_dict_code")).replaceAll(""));
				                	
				                	userXpList.add(bean);
								}
							}
//							this.wellFormedDAO.getCache().add(key, proCfgList);
							res.setReObj(userXpList);
						}
					}
				}
//			}
//			res.setReObj(proCfgList);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	public class ExperienceInfoBean {
		private String userId;
		private String dealCode;
		private String dealType;
		private String useDate;
		private String endDate;
		private String isRemind;
		private String remark;
		private String smsFlag;
		private String cardId;
		private String openFlag;
		private String operatingSrl;
		private String dictCode;
		public String getCardId() {
			return cardId;
		}
		public void setCardId(String cardId) {
			this.cardId = cardId;
		}
		public String getDealCode() {
			return dealCode;
		}
		public void setDealCode(String dealCode) {
			this.dealCode = dealCode;
		}
		public String getDealType() {
			return dealType;
		}
		public void setDealType(String dealType) {
			this.dealType = dealType;
		}
		public String getDictCode() {
			return dictCode;
		}
		public void setDictCode(String dictCode) {
			this.dictCode = dictCode;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public String getIsRemind() {
			return isRemind;
		}
		public void setIsRemind(String isRemind) {
			this.isRemind = isRemind;
		}
		public String getOpenFlag() {
			return openFlag;
		}
		public void setOpenFlag(String openFlag) {
			this.openFlag = openFlag;
		}
		public String getOperatingSrl() {
			return operatingSrl;
		}
		public void setOperatingSrl(String operatingSrl) {
			this.operatingSrl = operatingSrl;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getSmsFlag() {
			return smsFlag;
		}
		public void setSmsFlag(String smsFlag) {
			this.smsFlag = smsFlag;
		}
		public String getUseDate() {
			return useDate;
		}
		public void setUseDate(String useDate) {
			this.useDate = useDate;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
	}
}
