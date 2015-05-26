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
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.MobileGameInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY020010Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 手机游戏订购关系查询
 * 
 * @author 吴宗德
 * 
 */
public class QueryMobileGameInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryMobileGameInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");

	public QueryMobileGameInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));

	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY020010Result res = new QRY020010Result();
		List<MobileGameInfo> mobileGameInfos = new ArrayList<MobileGameInfo>();
		MobileGameInfo bean = null;
		try {
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			// 查询手机游戏订购关系
			BaseResult mobileOrderInfoListRet = this.getMobileOrderInfoList(accessId, config, params);
			if (LOGIC_SUCESS.equals(mobileOrderInfoListRet.getResultCode())) {
				List<MobileGameOrderInfoBean> mobileOrderInfoList = (List<MobileGameOrderInfoBean>) mobileOrderInfoListRet.getReObj();

				if (mobileOrderInfoList != null && mobileOrderInfoList.size() > 0) {

					for (MobileGameOrderInfoBean obj : mobileOrderInfoList) {
						bean = new MobileGameInfo();
						bean.setStartDate(obj.getCreate_date());
						bean.setEndDate(obj.getEnd_date());
						bean.setSpId(obj.getSpid());
						bean.setBizCode(obj.getBizcode());
						bean.setIsOpen(1);
						mobileGameInfos.add(bean);
					}

				}
				// 查询可开通业务列表
				BaseResult mobileCfgListRet = this.getMobileCfgList(accessId, config, params);
				
				// 查询SP企业名称、SP业务名称
				BaseResult spBizListRet = this.getSpBizName(accessId, config, params);
				
				//已开通的游戏数
				int isOpenGameCount = mobileGameInfos.size();
				
				if (LOGIC_SUCESS.equals(mobileOrderInfoListRet.getResultCode())) {
					List<CcCGetMobileCfgBean> mobileCfgList = (List<CcCGetMobileCfgBean>) mobileCfgListRet.getReObj();
					//List<CcCGetMobileCfgBean> sjyxCfgList = new ArrayList();
					//游戏玩家业务配置接口查询不出来，cc_cgerspbizinfo接口也查询不到，目前只能写死，日后需改造
					//==================================================================================
					CcCGetMobileCfgBean bean2 = new CcCGetMobileCfgBean();
					bean2.setBusi_code("500230544000");
					bean2.setBusi_desc("包月游戏套餐");
					bean2.setPrice("5000");
					bean2.setBusi_name("游戏玩家");
					bean2.setSp_id("701001");
					bean2.setBilling_type("3");
					if(mobileCfgList != null){
						mobileCfgList.add(bean2);
					}
					//==================================================================================
					//新增业务配置接口查询不出来，cc_cgerspbizinfo接口也查询不到，目前只能写死，日后需改造
					//==================================================================================
					CcCGetMobileCfgBean bean3 = new CcCGetMobileCfgBean();
					bean3.setBusi_code("500231891000");
					bean3.setBusi_desc("包月游戏套餐");
					bean3.setPrice("0");
					bean3.setBusi_name("手机游戏—GAMEZONE");
					bean3.setSp_id("701001");
					bean3.setBilling_type("3");
					if(mobileCfgList != null){
						mobileCfgList.add(bean3);
					}
					//==================================================================================

					if (mobileCfgList != null && mobileCfgList.size() > 0) {
						gg: for (CcCGetMobileCfgBean cfg : mobileCfgList) {
								
								if(isOpenGameCount != 0){
									for (MobileGameInfo gameInfo : mobileGameInfos) {
										if (gameInfo.getSpId().equals(cfg.getSp_id().trim()) && gameInfo.getBizCode().equals(cfg.getBusi_code())) {
											gameInfo.setBizType(Integer.parseInt(cfg.getBilling_type().trim()));
											gameInfo.setSpName(cfg.getBusi_name());
											gameInfo.setPrice(Integer.parseInt(cfg.getPrice()));
											isOpenGameCount--;
											continue gg;
										}
									}
								}
								
								bean = new MobileGameInfo();
								bean.setSpId(cfg.getSp_id().trim());
								bean.setBizCode(cfg.getBusi_code());
								bean.setBizType(Integer.parseInt(cfg.getBilling_type().trim()));
								bean.setIsOpen(0);
								bean.setSpName(cfg.getBusi_name());
								String price = cfg.getPrice();
								bean.setPrice("".equals(price) ? 0 : Integer.parseInt(price));
								mobileGameInfos.add(bean);
							}
					}
					
					//需要删除的迭代号集合
					StringBuffer iterator =new StringBuffer();
					
					for (MobileGameInfo gameInfo : mobileGameInfos) {
						
						if (LOGIC_SUCESS.equals(spBizListRet.getResultCode())) {
							List<SpBusinessInfoBean> spBizList = (List<SpBusinessInfoBean>) spBizListRet.getReObj();

							if (spBizList != null && spBizList.size() > 0) {
								for (SpBusinessInfoBean obj : spBizList) {
									String bizCode = gameInfo.getBizCode();
									
									/*
									 * 500231886000 爱的游戏盒3 500231888000 爱的游戏盒5 500231887000 爱的游戏盒4 500231885000
									 * 爱的游戏盒2 500231884000 爱的游戏盒1 500231883000 江苏动感套餐 屏蔽以上套餐
									 */
									if("500231886000".equals(bizCode) || "500231884000".equals(bizCode)|| "500231885000".equals(bizCode)|| "500231886000".equals(bizCode)|| "500231887000".equals(bizCode)|| "500231888000".equals(bizCode)){
										int index = mobileGameInfos.indexOf(gameInfo);
										iterator.append(index).append(',');
										break;
									}
									if(obj.getBusi_code().equals(bizCode)){
										gameInfo.setBusiName(obj.getBusi_type().trim());
										String price = obj.getPrice().trim();
										gameInfo.setPrice("".equals(price) ? 0 : Integer.parseInt(price));
										break;
									}
								}
							}
						} else {
							res.setResultCode(LOGIC_ERROR);
							res.setErrorCode(spBizListRet.getErrorCode());
							res.setErrorMessage(spBizListRet.getErrorMessage());
						}
					}
					
					//删除不显示的套餐
					String [] iterators = iterator.toString().split(",");
					
					//偏移量
					int off = 0;
					for(String item : iterators){
						if(!"".equals(item)){
							int index = Integer.parseInt(item) - off;
							mobileGameInfos.remove(index);
							off++;
						}
					}
					
					res.setMobileGameInfos(mobileGameInfos);
				} else {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(mobileCfgListRet.getErrorCode());
					res.setErrorMessage(mobileCfgListRet.getErrorMessage());
				}
			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(mobileOrderInfoListRet.getErrorCode());
				res.setErrorMessage(mobileOrderInfoListRet.getErrorMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 查询手机游戏订购关系
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getMobileOrderInfoList(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		List<MobileGameOrderInfoBean> mobileOrderInfoList = null;

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetusermobile_825", params);
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetusermobile_825", super.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY020010", "cc_cgetusermobile_825", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);

				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					List mobilegameorderinfo_user_id = null;
					try {
						mobilegameorderinfo_user_id = root.getChild("content").getChildren("mobilegameorderinfo_user_id");
					} catch (Exception e) {
						mobilegameorderinfo_user_id = null;
					}
					if (null != mobilegameorderinfo_user_id && mobilegameorderinfo_user_id.size() > 0) {
						mobileOrderInfoList = new ArrayList<MobileGameOrderInfoBean>(mobilegameorderinfo_user_id.size());
						MobileGameOrderInfoBean bean = null;
						for (int i = 0; i < mobilegameorderinfo_user_id.size(); i++) {
							Element cmobilegameorderinfodt = ((Element) mobilegameorderinfo_user_id.get(i)).getChild("cmobilegameorderinfodt");
							if (null != cmobilegameorderinfodt) {
								bean = new MobileGameOrderInfoBean();

								bean.setUser_id(p.matcher(cmobilegameorderinfodt.getChildText("mobilegameorderinfo_user_id")).replaceAll(""));
								bean.setStart_date(p.matcher(cmobilegameorderinfodt.getChildText("mobilegameorderinfo_start_date")).replaceAll(""));
								bean.setEnd_date(p.matcher(cmobilegameorderinfodt.getChildText("mobilegameorderinfo_end_date")).replaceAll(""));
								bean.setSpid(p.matcher(cmobilegameorderinfodt.getChildText("mobilegameorderinfo_spid")).replaceAll(""));
								bean.setBizcode(p.matcher(cmobilegameorderinfodt.getChildText("mobilegameorderinfo_bizcode")).replaceAll(""));
								bean.setCreate_date(p.matcher(cmobilegameorderinfodt.getChildText("mobilegameorderinfo_create_date")).replaceAll(""));
								bean.setCreate_operator(p.matcher(cmobilegameorderinfodt.getChildText("mobilegameorderinfo_create_operator")).replaceAll(""));
								bean.setOper_source(p.matcher(cmobilegameorderinfodt.getChildText("mobilegameorderinfo_oper_source")).replaceAll(""));
								bean.setOperating_srl(p.matcher(cmobilegameorderinfodt.getChildText("mobilegameorderinfo_operating_srl")).replaceAll(""));
								bean.setOperation_srl(p.matcher(cmobilegameorderinfodt.getChildText("mobilegameorderinfo_operation_srl")).replaceAll(""));

								mobileOrderInfoList.add(bean);
							}
						}
						res.setReObj(mobileOrderInfoList);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 查询可开通业务列表
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult getMobileCfgList(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		List<CcCGetMobileCfgBean> mobileCfgList = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetmobilecfg_831", params);

			logger.debug(" ====== 手机游戏订购关系查询 查询可开通业务列表请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetmobilecfg_831", super.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY020010", "cc_cgetmobilecfg_831", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);

				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					List spbusinessinfo_sp_id = null;
					try {
						spbusinessinfo_sp_id = root.getChild("content").getChildren("spbusinessinfo_sp_id");
					} catch (Exception e) {
						spbusinessinfo_sp_id = null;
					}
					if (null != spbusinessinfo_sp_id && spbusinessinfo_sp_id.size() > 0) {
						mobileCfgList = new ArrayList<CcCGetMobileCfgBean>(spbusinessinfo_sp_id.size());
						CcCGetMobileCfgBean bean = null;
						for (int i = 0; i < spbusinessinfo_sp_id.size(); i++) {
							Element cspbusiinfodt = ((Element) spbusinessinfo_sp_id.get(i)).getChild("cspbusiinfodt");
							if (null != cspbusiinfodt) {
								bean = new CcCGetMobileCfgBean();

								bean.setSp_id(p.matcher(cspbusiinfodt.getChildText("spbusinessinfo_sp_id")).replaceAll(""));
								bean.setBusi_code(p.matcher(cspbusiinfodt.getChildText("spbusinessinfo_busi_code")).replaceAll(""));
								bean.setBusi_name(p.matcher(cspbusiinfodt.getChildText("spbusinessinfo_busi_name")).replaceAll(""));
								bean.setBusi_desc(p.matcher(cspbusiinfodt.getChildText("spbusinessinfo_busi_desc")).replaceAll(""));
								bean.setAccess_model(p.matcher(cspbusiinfodt.getChildText("spbusinessinfo_access_model")).replaceAll(""));
								bean.setPrice(p.matcher(cspbusiinfodt.getChildText("spbusinessinfo_price")).replaceAll(""));
								bean.setBilling_type(p.matcher(cspbusiinfodt.getChildText("spbusinessinfo_billing_type")).replaceAll(""));
								bean.setBusi_state(p.matcher(cspbusiinfodt.getChildText("spbusinessinfo_busi_state")).replaceAll(""));
								bean.setProv_addr(p.matcher(cspbusiinfodt.getChildText("spbusinessinfo_prov_addr")).replaceAll(""));
								bean.setProv_port(p.matcher(cspbusiinfodt.getChildText("spbusinessinfo_prov_port")).replaceAll(""));
								bean.setUsage_desc(p.matcher(cspbusiinfodt.getChildText("spbusinessinfo_usage_desc")).replaceAll(""));
								bean.setIntro_url(p.matcher(cspbusiinfodt.getChildText("spbusinessinfo_intro_url")).replaceAll(""));
								bean.setReserved(p.matcher(cspbusiinfodt.getChildText("spbusinessinfo_reserved")).replaceAll(""));

								mobileCfgList.add(bean);
							}
						}
						// this.wellFormedDAO.getCache().add(key, proCfgList);
						res.setReObj(mobileCfgList);
					}
				}
			}
			// }
			// res.setReObj(proCfgList);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 查询SP业务名称
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult getSpBizName(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String rspXml = "";
		ErrorMapping errDt = null;
		List<SpBusinessInfoBean> spBizList = null;
		try {
			rspXml = (String) this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext("cc_cgerspbizinfo_826", params), accessId, "cc_cgerspbizinfo_826", super.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY020010", "cc_cgerspbizinfo_826", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);

				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					List ib_spb_sp_id = null;
					try {
						ib_spb_sp_id = root.getChild("content").getChildren("ib_spb_sp_id");
					} catch (Exception e) {
						ib_spb_sp_id = null;
					}
					if (null != ib_spb_sp_id && ib_spb_sp_id.size() > 0) {
						spBizList = new ArrayList<SpBusinessInfoBean>(ib_spb_sp_id.size());
						SpBusinessInfoBean bean = null;
						for (int i = 0; i < ib_spb_sp_id.size(); i++) {
							Element cspbusinessinfodt = ((Element) ib_spb_sp_id.get(i)).getChild("cspbusinessinfodt");
							if (null != cspbusinessinfodt) {
								bean = new SpBusinessInfoBean();

								bean.setSp_id(p.matcher(cspbusinessinfodt.getChildText("ib_spb_sp_id")).replaceAll(""));
								bean.setBusi_code(p.matcher(cspbusinessinfodt.getChildText("ib_spb_busi_code")).replaceAll(""));
								bean.setBusi_type(p.matcher(cspbusinessinfodt.getChildText("ib_spb_busi_type")).replaceAll(""));
								bean.setBusi_desc(p.matcher(cspbusinessinfodt.getChildText("ib_spb_busi_desc")).replaceAll(""));
								bean.setAccess_model(p.matcher(cspbusinessinfodt.getChildText("ib_spb_access_model")).replaceAll(""));
								bean.setPrice(p.matcher(cspbusinessinfodt.getChildText("ib_spb_price")).replaceAll(""));
								bean.setBilling_type(p.matcher(cspbusinessinfodt.getChildText("ib_spb_billing_type")).replaceAll(""));
								bean.setBusi_state(p.matcher(cspbusinessinfodt.getChildText("ib_spb_busi_state")).replaceAll(""));
								bean.setProv_addr(p.matcher(cspbusinessinfodt.getChildText("ib_spb_prov_addr")).replaceAll(""));
								bean.setProv_port(p.matcher(cspbusinessinfodt.getChildText("ib_spb_prov_port")).replaceAll(""));
								bean.setUsage_desc(p.matcher(cspbusinessinfodt.getChildText("ib_spb_usage_desc")).replaceAll(""));
								bean.setIntro_url(p.matcher(cspbusinessinfodt.getChildText("ib_spb_intro_url")).replaceAll(""));
								bean.setReserved(p.matcher(cspbusinessinfodt.getChildText("ib_spb_reserved")).replaceAll(""));
								spBizList.add(bean);
							}
						}
						res.setReObj(spBizList);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 业务boss实现类
	 * 
	 * @author 吴宗德
	 * 
	 */
	public class BusinessBoss {
		private String parm1;

		public BusinessBoss(String parm1) {
			super();
			this.parm1 = parm1;
		}

		public String getParm1() {
			return parm1;
		}

		public void setParm1(String parm1) {
			this.parm1 = parm1;
		}
	}

	public class SpBusinessInfoBean {

		private String sp_id;

		private String busi_code;

		private String busi_type;

		private String busi_desc;

		private String access_model;

		private String price;

		private String billing_type;

		private String busi_state;

		private String prov_addr;

		private String prov_port;

		private String usage_desc;

		private String intro_url;

		private String reserved;

		public String getAccess_model() {
			return access_model;
		}

		public void setAccess_model(String access_model) {
			this.access_model = access_model;
		}

		public String getBilling_type() {
			return billing_type;
		}

		public void setBilling_type(String billing_type) {
			this.billing_type = billing_type;
		}

		public String getBusi_code() {
			return busi_code;
		}

		public void setBusi_code(String busi_code) {
			this.busi_code = busi_code;
		}

		public String getBusi_desc() {
			return busi_desc;
		}

		public void setBusi_desc(String busi_desc) {
			this.busi_desc = busi_desc;
		}

		public String getBusi_state() {
			return busi_state;
		}

		public void setBusi_state(String busi_state) {
			this.busi_state = busi_state;
		}

		public String getBusi_type() {
			return busi_type;
		}

		public void setBusi_type(String busi_type) {
			this.busi_type = busi_type;
		}

		public String getIntro_url() {
			return intro_url;
		}

		public void setIntro_url(String intro_url) {
			this.intro_url = intro_url;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getProv_addr() {
			return prov_addr;
		}

		public void setProv_addr(String prov_addr) {
			this.prov_addr = prov_addr;
		}

		public String getProv_port() {
			return prov_port;
		}

		public void setProv_port(String prov_port) {
			this.prov_port = prov_port;
		}

		public String getReserved() {
			return reserved;
		}

		public void setReserved(String reserved) {
			this.reserved = reserved;
		}

		public String getSp_id() {
			return sp_id;
		}

		public void setSp_id(String sp_id) {
			this.sp_id = sp_id;
		}

		public String getUsage_desc() {
			return usage_desc;
		}

		public void setUsage_desc(String usage_desc) {
			this.usage_desc = usage_desc;
		}
	}

	class CcCGetMobileCfgBean {

		private String sp_id;

		private String busi_code;

		private String busi_name;

		private String busi_desc;

		private String access_model;

		private String price;

		private String billing_type;

		private String busi_state;

		private String prov_addr;

		private String prov_port;

		private String usage_desc;

		private String intro_url;

		private String reserved;

		public String getAccess_model() {
			return access_model;
		}

		public void setAccess_model(String access_model) {
			this.access_model = access_model;
		}

		public String getBilling_type() {
			return billing_type;
		}

		public void setBilling_type(String billing_type) {
			this.billing_type = billing_type;
		}

		public String getBusi_code() {
			return busi_code;
		}

		public void setBusi_code(String busi_code) {
			this.busi_code = busi_code;
		}

		public String getBusi_desc() {
			return busi_desc;
		}

		public void setBusi_desc(String busi_desc) {
			this.busi_desc = busi_desc;
		}

		public String getBusi_name() {
			return busi_name;
		}

		public void setBusi_name(String busi_name) {
			this.busi_name = busi_name;
		}

		public String getBusi_state() {
			return busi_state;
		}

		public void setBusi_state(String busi_state) {
			this.busi_state = busi_state;
		}

		public String getIntro_url() {
			return intro_url;
		}

		public void setIntro_url(String intro_url) {
			this.intro_url = intro_url;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getProv_addr() {
			return prov_addr;
		}

		public void setProv_addr(String prov_addr) {
			this.prov_addr = prov_addr;
		}

		public String getProv_port() {
			return prov_port;
		}

		public void setProv_port(String prov_port) {
			this.prov_port = prov_port;
		}

		public String getReserved() {
			return reserved;
		}

		public void setReserved(String reserved) {
			this.reserved = reserved;
		}

		public String getSp_id() {
			return sp_id;
		}

		public void setSp_id(String sp_id) {
			this.sp_id = sp_id;
		}

		public String getUsage_desc() {
			return usage_desc;
		}

		public void setUsage_desc(String usage_desc) {
			this.usage_desc = usage_desc;
		}
	}

	class MobileGameOrderInfoBean {
		private String user_id;

		private String start_date;

		private String end_date;

		private String spid;

		private String bizcode;

		private String create_date;

		private String create_operator;

		private String oper_source;

		private String operating_srl;

		private String operation_srl;

		public String getBizcode() {
			return bizcode;
		}

		public void setBizcode(String bizcode) {
			this.bizcode = bizcode;
		}

		public String getCreate_date() {
			return create_date;
		}

		public void setCreate_date(String create_date) {
			this.create_date = create_date;
		}

		public String getCreate_operator() {
			return create_operator;
		}

		public void setCreate_operator(String create_operator) {
			this.create_operator = create_operator;
		}

		public String getEnd_date() {
			return end_date;
		}

		public void setEnd_date(String end_date) {
			this.end_date = end_date;
		}

		public String getOper_source() {
			return oper_source;
		}

		public void setOper_source(String oper_source) {
			this.oper_source = oper_source;
		}

		public String getOperating_srl() {
			return operating_srl;
		}

		public void setOperating_srl(String operating_srl) {
			this.operating_srl = operating_srl;
		}

		public String getOperation_srl() {
			return operation_srl;
		}

		public void setOperation_srl(String operation_srl) {
			this.operation_srl = operation_srl;
		}

		public String getSpid() {
			return spid;
		}

		public void setSpid(String spid) {
			this.spid = spid;
		}

		public String getStart_date() {
			return start_date;
		}

		public void setStart_date(String start_date) {
			this.start_date = start_date;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
	}

}