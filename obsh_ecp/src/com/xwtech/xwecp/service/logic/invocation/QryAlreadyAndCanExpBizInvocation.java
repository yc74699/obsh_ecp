package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
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
import com.xwtech.xwecp.service.logic.pojo.AlreadyExperienceBiz;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.CanExperienceBiz;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY050031Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 业务优惠使用区 查询已体验的业务及可体验的业务
 * 
 * @author 吴宗德
 *
 */
public class QryAlreadyAndCanExpBizInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(QryAlreadyAndCanExpBizInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public QryAlreadyAndCanExpBizInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY050031Result res = new QRY050031Result();
		List<AlreadyExperienceBiz> allBizs = new ArrayList<AlreadyExperienceBiz>();
		List<CanExperienceBiz> canBizs = new ArrayList<CanExperienceBiz>();
		
		try
		{
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			//查询体验卡配置信息
			BaseResult experCfgListRet = this.getExperCfg(accessId, config, params);
			if (LOGIC_SUCESS.equals(experCfgListRet.getResultCode())) {
				List<CcCGetExperCfgBean> experCfgList = (List<CcCGetExperCfgBean>)experCfgListRet.getReObj();
				
				//查询用户正在体验的新业务
				BaseResult userXpListRet = this.queryUserXp(accessId, config, params);
				if (LOGIC_SUCESS.equals(experCfgListRet.getResultCode())) {
					List<ExperienceInfoBean> userXpList = (List<ExperienceInfoBean>)userXpListRet.getReObj();
					
					if (experCfgList != null && experCfgList.size() > 0) {
						CcCGetExperCfgBean ccCGetExperCfgBean = null;
						Map<Integer, String[]> bizInfoMap = getExperienceBizInfo();

						String[] info = null;
						int size = experCfgList.size();
						boolean hasSecretary = false;

						for (int i = size - 1; i >= 0; i--) {
							ccCGetExperCfgBean = experCfgList.get(i);

							// 配置中没有的不显示 todo:"16"
							if (!ArrayUtils.contains(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
									"10", "11", "12", "13", "14", "15", "125", "110", "114", "117", "120", "61", "60"}, ccCGetExperCfgBean.getDictCode())) {
								experCfgList.remove(i);
								continue;
							}

							// 不展现experiencecfg_deal_type为3，和与用户地市Id不同的并且不为-1和-99的业务
							if ("3".equals(ccCGetExperCfgBean.getEalType()) || isInArray2(userXpList, ccCGetExperCfgBean.getDealCode(),ccCGetExperCfgBean.getPassword())
									//屏蔽139邮箱
									|| "100014".equals(ccCGetExperCfgBean.getPassword())) {
								experCfgList.remove(i);
								// 判断是否开通了秘书服务，如果体验了秘书服务业务中的一个,把另一个相关业务过滤掉不展示
								if (!hasSecretary && isInArray(userXpList, ccCGetExperCfgBean.getDealCode())
										&& ("15".equals(ccCGetExperCfgBean.getDictCode()) || "7".equals(ccCGetExperCfgBean.getDictCode()))) {
									hasSecretary = true;
								}

							} else {
								info = bizInfoMap.get(new Integer(ccCGetExperCfgBean.getDictCode()));
								if (info != null && info.length == 4) {
									CanExperienceBiz canBean = new CanExperienceBiz();
									
									canBean.setBizId(ccCGetExperCfgBean.getPassword());
									canBean.setBizName(info[0]);
									canBean.setBizIcon(info[1]);
									canBean.setBizURL(info[2]);
									canBean.setBizDesc(info[3]);
									
									canBizs.add(canBean);
								}
							}
						}

						// 如果体验了秘书服务业务中的一个,把另一个相关业务过滤掉不展示
						if (hasSecretary && experCfgList != null && experCfgList.size() > 0) {
							for (int i = 0; i < experCfgList.size(); i++) {
								if (hasSecretary
										&& ("15".equals(experCfgList.get(i).getDictCode()) || "7".equals(experCfgList.get(i).getDictCode()))) {
									// 如果已经开通了秘书助理或者秘书管家,将另一个秘书服务的套餐去掉不展现
									experCfgList.remove(i);
									break;
								}
							}
						}

						// 用户已经体验的新业务列表
						if (userXpList != null && userXpList.size() > 0) {
							for (ExperienceInfoBean bean:userXpList) {
								AlreadyExperienceBiz allBiz = new AlreadyExperienceBiz();
								
								allBiz.setBizId(bean.getCardId());
								allBiz.setBizName(bean.getRemark());
								allBiz.setStartDate(bean.getUseDate());
								allBiz.setEndDate(bean.getEndDate());
								
								allBizs.add(allBiz);
							}
						}
						//已体验的业务
						res.setAlreadyExperienceBizs(allBizs);
						//可以体验的业务
						res.setCanExperienceBizs(canBizs);
					}
				} else {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(userXpListRet.getErrorCode());
					res.setErrorMessage(userXpListRet.getErrorMessage());
				}
			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(experCfgListRet.getErrorCode());
				res.setErrorMessage(experCfgListRet.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 体验卡配置信息
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult getExperCfg(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
//		String key = "PRO_CFG_LIST_";
		List<CcCGetExperCfgBean> experCfgList = null;
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
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetexpercfg_313", params);
				
				logger.debug(" ====== 查询体验卡配置信息请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetexpercfg_313", this.generateCity(params)));
//				logger.debug(" ====== 查询体验卡配置信息返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY050031", "cc_cgetexpercfg_313", errCode);
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
						List experiencecfg_deal_type = null;
						try
						{
							experiencecfg_deal_type = root.getChild("content").getChildren("experiencecfg_deal_type");
						}
						catch (Exception e)
						{
							experiencecfg_deal_type = null;
						}
						if (null != experiencecfg_deal_type && experiencecfg_deal_type.size() > 0) {
							experCfgList = new ArrayList<CcCGetExperCfgBean>(experiencecfg_deal_type.size());
							CcCGetExperCfgBean bean = null;
							for (int i = 0; i < experiencecfg_deal_type.size(); i++)
							{
								bean = new CcCGetExperCfgBean();
								Element cexperiencecfgdt = ((Element)experiencecfg_deal_type.get(i)).getChild("cexperiencecfgdt");
								if (null != cexperiencecfgdt)
								{
				                	bean.setDealCode(p.matcher(cexperiencecfgdt.getChildText("experiencecfg_deal_code")).replaceAll(""));
				                	bean.setPackageCode(p.matcher(cexperiencecfgdt.getChildText("experiencecfg_package_code")).replaceAll(""));
				                	bean.setPackageType(p.matcher(cexperiencecfgdt.getChildText("experiencecfg_package_code")).replaceAll(""));
				                	bean.setContinuePackage(p.matcher(cexperiencecfgdt.getChildText("experiencecfg_continue_package")).replaceAll(""));
				                	bean.setBeginDate(p.matcher(cexperiencecfgdt.getChildText("experiencecfg_begin_date")).replaceAll(""));
				                	bean.setEndDate(p.matcher(cexperiencecfgdt.getChildText("experiencecfg_end_date")).replaceAll(""));
				                	bean.setTimes(p.matcher(cexperiencecfgdt.getChildText("experiencecfg_times")).replaceAll(""));
				                	bean.setExperienceMonth(p.matcher(cexperiencecfgdt.getChildText("experiencecfg_experience_month")).replaceAll(""));
				                	bean.setShowFlag(p.matcher(cexperiencecfgdt.getChildText("experiencecfg_show_flag")).replaceAll(""));
				                	bean.setSpBizCode(p.matcher(cexperiencecfgdt.getChildText("experiencecfg_sp_biz_code")).replaceAll(""));
				                	bean.setCityId(p.matcher(cexperiencecfgdt.getChildText("experiencecfg_city_id")).replaceAll(""));
				                	bean.setSpComCode(p.matcher(cexperiencecfgdt.getChildText("experiencecfg_sp_com_code")).replaceAll(""));
				                	bean.setDictCode(p.matcher(cexperiencecfgdt.getChildText("experiencecfg_dict_code")).replaceAll(""));
				                	bean.setContinueSpBizCode(p.matcher(cexperiencecfgdt.getChildText("experiencecfg_continue_sp_biz_code")).replaceAll(""));
				                	bean.setExperienceId(p.matcher(cexperiencecfgdt.getChildText("experiencecfg_experience_id")).replaceAll(""));
				                	bean.setPassword(p.matcher(cexperiencecfgdt.getChildText("experiencecfg_password")).replaceAll(""));
				                	bean.setEalType(p.matcher(cexperiencecfgdt.getChildText("experiencecfg_deal_type")).replaceAll(""));
				                	
				                	experCfgList.add(bean);
								}
							}
//							this.wellFormedDAO.getCache().add(key, proCfgList);
							res.setReObj(experCfgList);
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
						errDt = this.wellFormedDAO.transBossErrCode("QRY050031", "cc_cqueryuserxp_314", errCode);
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
	
	/**
	 * @desc 判断dealCode是否在List列表中，在：true，不在：false
	 * @return boolean
	 */
	private boolean isInArray(List<ExperienceInfoBean> experInfoList, String dealCode) {
		boolean b = false;
		if (dealCode != null && experInfoList != null && experInfoList.size() > 0) {
			int size = experInfoList.size();
			for (int i = 0; i < size; i++) {
				if (dealCode.equals(experInfoList.get(i).getDealCode())) {
					b = true;
					break;
				}
			}
		}

		return b;
	}
	
	/**
	 * @desc 判断dealCode是否在List列表中，在：true，不在：false
	 * @return boolean
	 */
	private boolean isInArray2(List<ExperienceInfoBean> experInfoList, String dealCode,String cardId) {
		boolean b = false;
		if (dealCode != null && experInfoList != null && experInfoList.size() > 0) {
			int size = experInfoList.size();
			for (int i = 0; i < size; i++) {
				if (dealCode.equals(experInfoList.get(i).getDealCode()) && cardId.equals(experInfoList.get(i).getCardId())) {
					b = true;
					break;
				}
			}
		}

		return b;
	}
	
	/**
	 * @desc 可体验的业务相关信息，新大陆提供的
	 * @param request
	 * @param response
	 * @return
	 */
	private static Map<Integer, String[]> getExperienceBizInfo() {
		// TODO 体验业务配置区
		Map<Integer, String[]> experienceMap = new HashMap<Integer, String[]>();
		experienceMap.put(new Integer(0), new String[] { "彩铃", "trial6.jpg",
				"http://www.js.10086.cn/10086/help/handbook/xxyw/2008/xxyw38768.html",
				"被叫客户自行设定个性化回铃音，在其做被叫时，为主叫客户播放其设定的一段音乐或录音，来代替普通回铃音的业务。" });
		
		experienceMap.put(new Integer(1), new String[] { "理财助手（话费易）", "trial1.jpg",
				"http://www.js.10086.cn/10086/help/handbook/xxyw/2008/xxyw38779.html",
				"在您开通理财助手（话费易）后，系统自动分析您上月的通信消费情况，以短信方式每月向您发送一次资费理财建议，每周发送二次您的消费情况信息。" });
		        
		experienceMap.put(new Integer(2), new String[] { "亲情易查询", "trial4.jpg",
				"http://www.js.10086.cn/10086/help/handbook/xxyw/2008/xxyw38761.html",
				"畅谈无间客户无需主动查询即可每周两次获取本机当月亲情号码之间剩余优惠通话分钟数的提醒短信，直到优惠分钟额度使用完后。" });
		
		experienceMap.put(new Integer(3), new String[] { "动感易查询", "trial2.jpg",
				"http://www.js.10086.cn/10086/help/handbook/xxyw/2008/xxyw276.html",
				"无需主动查询即可每周两次获取本机动感地带套餐使用情况的提醒信息（含必选套餐与可选套餐，暂不提供可选套餐中赠送的GPRS流量使用情况），套餐额度使用完后将不再发送提醒短信。" });
		        
		experienceMap.put(new Integer(4), new String[] { "套餐易查询", "trial3.jpg",
				"http://www.js.10086.cn/10086/help/handbook/xxyw/2008/xxyw38761.html",
				"无需主动查询即可每周两次获取本机各类套餐使用情况的提醒信息（具体提供提醒的套餐种类由分公司决定，原则上仅提供全球通、放心打套餐的信息)，套餐额度使用完后将不再发送提醒短信。" });
		
		//experienceMap.put(new Integer(5), new String[]{"被叫优惠易查询", "trial5.jpg",
		// 		"http://www.js.10086.cn/10086/help/handbook/xxyw/2008/xxyw276.html",
		//		"用户无需主动查询即可每周两次获取本机当月分区计费套餐免费被叫分钟数使用情况的提醒信息。客户享受完套餐优惠后将不再发送提醒短信。"});
				 
		experienceMap.put(new Integer(6), new String[] { "短信呼", "trial7.gif",
				"http://www.js.10086.cn/10086/help/handbook/yyyw/2009/yyyw38758.html",
				"在关机或手机掉网(地下室,电梯里)时，来电转入语音应答系统记录来电号码，当客户开机或手机恢复正常时，来电信息会以短信方式发到客户手机上。" });
		
		experienceMap.put(new Integer(7), new String[] { "秘书管家", "trial11.jpg",
				"http://www.js.10086.cn/10086/help/handbook/xxyw/2008/xxyw285.html",
				"开通秘书管家后，可以享受代发短信(每次来电5条)、经典短信、来电代接、机主留言、日程提醒、叫醒服务、个性化首问语、电话本管家、号码预选、天气预报（赠送）等秘书服务、服务热线12580。" });
				 
		experienceMap.put(new Integer(8), new String[] { "5元彩信套餐（包25条彩信）", "trial9.jpg",
				"http://www.js.10086.cn/10086/help/handbook/xxyw/2008/xxyw38762.html",
				"仅包含网内点对点彩信（含通过“手机桌面助理”发给网内客户的彩信）。客户与各类应用服务之间（如SP服务等）产生的彩信以及向异网客户发送的彩信均不计入套餐。" });
		
		//experienceMap.put(new Integer(9), new String[]{"短信回执", "trial8.jpg",
		// 		"http://www.js.10086.cn/10086/help/handbook/xxyw/2008/xxyw290.html",
		//		"发送点对点短信后，可接收到对方已成功接收到短信的确认信息。若被叫客户一直未能成功接收到此短信,则主叫客户将不能接收到确认信息。"});

		//experienceMap.put(new Integer(10), new String[]{"闪信", "trialsx.jpg",
		// 		"http://www.js.10086.cn/10086/help/handbook/xxyw/2008/xxyw287.html",
		//		"发送给对方的信息可直接显示在对方手机屏幕上，不需按“查看”键即可阅读。闪信阅读后不自动保存。若先后收到几条闪信，最后一条闪信会自动覆盖掉前条。"});
		
		experienceMap.put(new Integer(11), new String[] { "气象站", "trial19.jpg",
				"http://www.js.10086.cn/10086/help/handbook/mwyw/2008/mwyw1010.html",
				"移动气象站业务采用统一的服务代码0121，提供全省各市县的天气预报、生活气象指数、海洋天气等信息的点播、定制。" });
		
		experienceMap.put(new Integer(12), new String[] { "139邮箱", "trial12.jpg",
				"http://www.js.10086.cn/10086/help/handbook/hlwtx/2008/hlwtx2737.html",
				"“139邮箱”业务是中国移动通信集团江苏有限公司为移动客户提供的免费手机邮箱业务，客户可通过WEB、WAP方式访问和使用邮箱； “139邮箱”业务除了提供普通的收发邮件功能外，还具备：网络硬盘、短信提醒、短信珍藏、自写短信、自写彩信、网络日历、话单定制等增值功能。" });
		        
		experienceMap.put(new Integer(13), new String[] { "无线音乐俱乐部高级会员", "trial13.jpg",
				"http://www.js.10086.cn/10086/help/handbook/xxyw/2008/xxyw38767.html",
				"无限音乐俱乐部面向客户提供音乐下载、音乐共享、音乐交流、明星演唱会等服务，满足客户在音乐上的核心需求。" });
		
		experienceMap.put(new Integer(14), new String[] { "新闻早晚报", "trial14.jpg",
				"http://www.js.10086.cn/10086/help/handbook/xxyw/2008/xxyw300.html",
				"《新闻早晚报》以综合新闻资讯为主，包括国际国内体育娱乐各类新闻以及笑话生活天气等实用信息，每天早晚给您发送2条彩信：“早报”以精选新闻为主，“晚报”加强娱乐信息及实用资讯。" });
		
		experienceMap.put(new Integer(15), new String[] { "秘书助理", "trial10.jpg",
				"http://www.js.10086.cn/10086/help/handbook/xxyw/2008/xxyw285.html",
				"开通秘书助理后，您可以享受代发短信(每次来电3条)、经典短信、来电代接、机主留言、日程提醒、叫醒服务、个性化首问语、电话本管家等秘书服务，服务热线12580。" });
		
		//experienceMap.put(new Integer(16), new String[] { "来电提醒", "trial18.jpg",
		//		"http://www.js.10086.cn/10086/help/handbook/yyyw/2009/yyyw26809.html",
		// 		"“来电提醒”，当你关机或不在服务区时，您所有的来电均自动记录。当您开机时，来电信息会以短信形式发送到您的手机上。" });
		
		//体坛快报业务优惠体验
		experienceMap.put(new Integer(125), new String[] { "体坛快报", "trial20.jpg",
				"http://www.js.10086.cn/10086/help/handbook/xxyw/2009/xxyw42581.html",
				"《体育通-体坛快报》是一份专为体育竞赛和运动休闲爱好者度身打造的手机报，由江苏省体育局、中国移动江苏公司和南京日报报业集团联合主办的体育专业手机报，是省内唯一一家体讯手机报。" });
		
		//移动彩讯--时尚彩讯
		experienceMap.put(new Integer(110), new String[] { "移动彩讯-时尚彩讯", "trial110.jpg",
				"http://www.js.10086.cn/10086/help/handbook/xxyw/2010/xxyw43360.html",
				"为时尚人群和百姓消费群体提供服饰、家居、名品等时尚生活服务类信息，包括服饰潮流、鞋包配饰、时尚搭配等内容。" });
		
		//移动彩讯--影视速递
		experienceMap.put(new Integer(114), new String[] { "移动彩讯-影视速递", "trial114.jpg",
				"http://www.js.10086.cn/10086/help/handbook/xxyw/2010/xxyw43360.html",
				"涉及经典电影介绍、新片上映预告、热门电视剧介绍和经典电视剧回顾等内容，把众多电影资讯浓缩到手机中。" });
		
		//移动彩讯--气象彩讯
		experienceMap.put(new Integer(117), new String[] { "移动彩讯-气象彩讯", "trial117.jpg",
				"http://www.js.10086.cn/10086/help/handbook/xxyw/2010/xxyw43360.html",
				"向用户提供最新的天气预报、气象要闻、天文及地理杂谈、旅游气象等系列知识。" });
		
		//移动彩讯--流行小说
		experienceMap.put(new Integer(120), new String[] { "移动彩讯-流行小说", "trial120.jpg",
				"http://www.js.10086.cn/10086/help/handbook/xxyw/2010/xxyw43360.html",
				"面向小说爱好者，提供网上最热的畅销小说排行情况及最热门的小说连载，涉及奇幻武侠、男女情感、历史典故等众多内容。" });
		
		//12580联盟会员3元
		experienceMap.put(new Integer(60), new String[] { "12580联盟会员3元", "trial60.jpg",
				"http://www.js.10086.cn/10086/help/handbook/xxyw/2010/xxyw43248.html",
				"折扣实惠不用卡，畅享优惠有专家！只要你成为12580联盟会员，即可获取餐饮、娱乐、文化等多行业“优选+优惠”的消费资讯和及时折扣。联盟会员更可以通过互联网、手机网站、彩信、12580语音等多种途径获取到最新联盟商户信息。 " });
		
		//12580联盟会员5元
		experienceMap.put(new Integer(61), new String[] { "12580联盟会员5元", "trial61.jpg",
				"http://www.js.10086.cn/10086/help/handbook/xxyw/2010/xxyw43248.html",
				"折扣实惠不用卡，畅享优惠有专家！只要你成为12580联盟会员，即可获取餐饮、娱乐、文化等多行业“优选+优惠”的消费资讯和及时折扣。联盟会员更可以通过互联网、手机网站、彩信、12580语音等多种途径获取到最新联盟商户信息。 " });

		return experienceMap;
	}
	
	public class CcCGetExperCfgBean {

		//体验类型	1－语音业务，2－增值业务，3－MISC业务，4－套餐  5- 秘书服务，6-非MISC业务
		private String ealType;
		//业务代码
		private String dealCode;
		//免费套餐代码
		private String packageCode;
		//免费套餐大类
		private String packageType;
		//预约的收费套餐
		private String continuePackage;
		//开始时间
		private String beginDate;
		//结束时间
		private String endDate;
		//体验次数
		private String times;
		//体验月份
		private String experienceMonth;
		//界面展现	格式为 1,2,3
		private String showFlag;
		//Sp业务代码
		private String spBizCode;
		//地市
		private String cityId;
		//Sp企业代码
		private String spComCode;
		private String experienceId;
		private String dictCode;
		private String continueSpBizCode;
		private String password;
		
		//业务名称
		private String bizName;
		//业务描述
		private String desc;
		//业务介绍链接地址
		private String introduceUrl;
		//图片地址
		private String imgUrl;
		public String getBeginDate() {
			return beginDate;
		}
		public void setBeginDate(String beginDate) {
			this.beginDate = beginDate;
		}
		public String getBizName() {
			return bizName;
		}
		public void setBizName(String bizName) {
			this.bizName = bizName;
		}
		public String getCityId() {
			return cityId;
		}
		public void setCityId(String cityId) {
			this.cityId = cityId;
		}
		public String getContinuePackage() {
			return continuePackage;
		}
		public void setContinuePackage(String continuePackage) {
			this.continuePackage = continuePackage;
		}
		public String getContinueSpBizCode() {
			return continueSpBizCode;
		}
		public void setContinueSpBizCode(String continueSpBizCode) {
			this.continueSpBizCode = continueSpBizCode;
		}
		public String getDealCode() {
			return dealCode;
		}
		public void setDealCode(String dealCode) {
			this.dealCode = dealCode;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		public String getDictCode() {
			return dictCode;
		}
		public void setDictCode(String dictCode) {
			this.dictCode = dictCode;
		}
		public String getEalType() {
			return ealType;
		}
		public void setEalType(String ealType) {
			this.ealType = ealType;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public String getExperienceId() {
			return experienceId;
		}
		public void setExperienceId(String experienceId) {
			this.experienceId = experienceId;
		}
		public String getExperienceMonth() {
			return experienceMonth;
		}
		public void setExperienceMonth(String experienceMonth) {
			this.experienceMonth = experienceMonth;
		}
		public String getImgUrl() {
			return imgUrl;
		}
		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}
		public String getIntroduceUrl() {
			return introduceUrl;
		}
		public void setIntroduceUrl(String introduceUrl) {
			this.introduceUrl = introduceUrl;
		}
		public String getPackageCode() {
			return packageCode;
		}
		public void setPackageCode(String packageCode) {
			this.packageCode = packageCode;
		}
		public String getPackageType() {
			return packageType;
		}
		public void setPackageType(String packageType) {
			this.packageType = packageType;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getShowFlag() {
			return showFlag;
		}
		public void setShowFlag(String showFlag) {
			this.showFlag = showFlag;
		}
		public String getSpBizCode() {
			return spBizCode;
		}
		public void setSpBizCode(String spBizCode) {
			this.spBizCode = spBizCode;
		}
		public String getSpComCode() {
			return spComCode;
		}
		public void setSpComCode(String spComCode) {
			this.spComCode = spComCode;
		}
		public String getTimes() {
			return times;
		}
		public void setTimes(String times) {
			this.times = times;
		}
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