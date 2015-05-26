package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
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
import com.xwtech.xwecp.service.logic.pojo.QRY050037Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 产品互转 预约产品信息查询
 * 
 * @author 吴宗德
 * 
 */
public class SwitchRevocationInfoInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger
			.getLogger(SwitchRevocationInfoInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");

	public SwitchRevocationInfoInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));

	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY050037Result res = new QRY050037Result();
		try {
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");

			BaseResult proList = this.getProList(accessId, config, params);
			if (LOGIC_SUCESS.equals(proList.getResultCode())) {
				List<CUserProductInfodtBean> pList = (List<CUserProductInfodtBean>) proList.getReObj();
				int canSwitch = 1;
				String oldBrandName = "";
				String oldProName = "";
				String newBrandName = "";
				String newProName = "";
				if (pList.size() > 1) {
					String oldProId = pList.get(0).getProduct_id();
					String newProId = pList.get(1).getProduct_id();
					if("99700005".equals(pList.get(0).getChange_operator())
							&& !oldProId.equals(newProId)
							&& !pList.get(0).getProduct_info_id().equals(pList.get(1).getProduct_info_id())
							&&(StringUtils.isBlank(pList.get(0).getEnd_date()) 
									|| StringUtils.isBlank(pList.get(1).getEnd_date()))){
						canSwitch = 0;
						BaseResult proCfgList = this.getProCfgList(accessId, config, params);

						if (LOGIC_SUCESS.equals(proCfgList.getResultCode())) {
							List<CcCGetProByCityBean> pCfgList = (List<CcCGetProByCityBean>) proCfgList.getReObj();
							if (pCfgList != null && pCfgList.size() > 0) {
								//用户品牌字典
								Map<String, String> brandDic = getBrandNameMap();
								
								for (CcCGetProByCityBean obj:pCfgList) {
									if (oldProId != null && oldProId.equals(obj.getProduct_id())) {
										oldBrandName = brandDic.get(obj.getBrand_id());
										oldProName = obj.getProduct_name();
										break;
									}
								}
								for (CcCGetProByCityBean obj:pCfgList) {
									if (newProId != null && newProId.equals(obj.getProduct_id())) {
										newBrandName = brandDic.get(obj.getBrand_id());
										newProName = obj.getProduct_name();
										break;
									}
								}
							}
						} else {
							res.setResultCode(LOGIC_ERROR);
							res.setErrorCode(proCfgList.getErrorCode());
							res.setErrorMessage(proCfgList.getErrorMessage());
						}
					}
				}
				
				res.setCanSwitch(canSwitch);
				res.setOldBrandName(oldBrandName);
				res.setOldProName(oldProName);
				res.setNewBrandName(newBrandName);
				res.setNewProName(newProName);
			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(proList.getErrorCode());
				res.setErrorMessage(proList.getErrorMessage());
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 产品信息查询
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getProList(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		List<CUserProductInfodtBean> proList = null;

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetproinfo_345", params);

			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetproinfo_345", this.generateCity(params)));
			logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY050037", "cc_cgetproinfo_345", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					List userproductinfo_product_info_id = null;
					try {
						userproductinfo_product_info_id = root.getChild("content").getChildren("userproductinfo_product_info_id");
					} catch (Exception e) {
						userproductinfo_product_info_id = null;
					}
					if (null != userproductinfo_product_info_id && userproductinfo_product_info_id.size() > 0) {
						proList = new ArrayList<CUserProductInfodtBean>(userproductinfo_product_info_id.size());
						CUserProductInfodtBean bean = null;
						for (int i = 0; i < userproductinfo_product_info_id.size(); i++) {
							Element cuserproductinfodt = ((Element) userproductinfo_product_info_id.get(i)).getChild("cuserproductinfodt");
							if (null != cuserproductinfodt) {
								bean = new CUserProductInfodtBean();
								
								bean.setChange_date(p.matcher(cuserproductinfodt.getChildText("userproductinfo_change_date")).replaceAll(""));
								bean.setChange_operator(p.matcher(cuserproductinfodt.getChildText("userproductinfo_change_operator")).replaceAll(""));
								bean.setCreate_date(p.matcher(cuserproductinfodt.getChildText("userproductinfo_create_date")).replaceAll(""));
								bean.setCreate_operator(p.matcher(cuserproductinfodt.getChildText("userproductinfo_create_operator")).replaceAll(""));
								bean.setEnd_date(p.matcher(cuserproductinfodt.getChildText("userproductinfo_end_date")).replaceAll(""));
								bean.setStart_date(p.matcher(cuserproductinfodt.getChildText("userproductinfo_start_date")).replaceAll(""));
								bean.setProduct_id(p.matcher(cuserproductinfodt.getChildText("userproductinfo_product_id")).replaceAll(""));
							    bean.setBrand_id(p.matcher(cuserproductinfodt.getChildText("userproductinfo_brand_id")).replaceAll(""));
								bean.setUser_id(p.matcher(cuserproductinfodt.getChildText("userproductinfo_user_id")).replaceAll(""));
								bean.setOperating_srl(p.matcher(cuserproductinfodt.getChildText("userproductinfo_operating_srl")).replaceAll(""));
								bean.setProduct_info_id(p.matcher(cuserproductinfodt.getChildText("userproductinfo_product_info_id")).replaceAll(""));
								
								proList.add(bean);
							}
						}
						res.setReObj(proList);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 网营产品配置信息
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult getProCfgList(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		List<CcCGetProByCityBean> proCfgList = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetprobycity_342_CPHZ", params);

			logger.debug(" ====== 查询产品配置请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetprobycity_342_CPHZ", this.generateCity(params)));
			// logger.debug(" ====== 查询产品配置返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY050037", "cc_cgetprobycity_342_CPHZ", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					List productbasecfg_product_id = null;
					try {
						productbasecfg_product_id = root.getChild("content").getChildren("productbasecfg_product_id");
					} catch (Exception e) {
						productbasecfg_product_id = null;
					}
					if (null != productbasecfg_product_id && productbasecfg_product_id.size() > 0) {
						proCfgList = new ArrayList<CcCGetProByCityBean>(productbasecfg_product_id.size());
						CcCGetProByCityBean bean = null;
						for (int i = 0; i < productbasecfg_product_id.size(); i++) {
							bean = new CcCGetProByCityBean();
							Element cwebproductopencfgdt = ((Element) productbasecfg_product_id.get(i)).getChild("productbasecfgdt");
							if (null != cwebproductopencfgdt) {
								bean.setProduct_id(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_product_id")).replaceAll(""));
								bean.setCity_id(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_city_id")).replaceAll(""));
								bean.setProduct_desc(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_product_desc")).replaceAll(""));
								bean.setPay_mode(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_pay_mode")).replaceAll(""));
								bean.setEnd_date(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_end_date")).replaceAll(""));
								bean.setCreate_date(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_create_date")).replaceAll(""));
								bean.setChange_date(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_change_date")).replaceAll(""));
								bean.setIs_province(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_is_province")).replaceAll(""));
								bean.setChange_operator(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_change_operator")).replaceAll(""));
								bean.setCreate_operator(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_create_operator")).replaceAll(""));
								bean.setBegin_date(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_begin_date")).replaceAll(""));
								bean.setBrand_id(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_brand_id")).replaceAll(""));
								bean.setProduct_name(p.matcher(cwebproductopencfgdt.getChildText("productbasecfg_product_name")).replaceAll(""));

								proCfgList.add(bean);
							}
						}
						res.setReObj(proCfgList);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	public class CUserProductInfodtBean {
		private String change_date;

		private String change_operator;

		private String create_date;

		private String create_operator;

		private String end_date;

		private String start_date;

		private String product_id;

		private String brand_id;

		private String user_id;

		private String operating_srl;

		private String product_info_id;

		public String getBrand_id() {
			return brand_id;
		}

		public void setBrand_id(String brand_id) {
			this.brand_id = brand_id;
		}

		public String getChange_date() {
			return change_date;
		}

		public void setChange_date(String change_date) {
			this.change_date = change_date;
		}

		public String getChange_operator() {
			return change_operator;
		}

		public void setChange_operator(String change_operator) {
			this.change_operator = change_operator;
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

		public String getOperating_srl() {
			return operating_srl;
		}

		public void setOperating_srl(String operating_srl) {
			this.operating_srl = operating_srl;
		}

		public String getProduct_id() {
			return product_id;
		}

		public void setProduct_id(String product_id) {
			this.product_id = product_id;
		}

		public String getProduct_info_id() {
			return product_info_id;
		}

		public void setProduct_info_id(String product_info_id) {
			this.product_info_id = product_info_id;
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

	public class CcCGetProByCityBean {

		private String product_id;

		private String city_id;

		private String product_desc;

		private String pay_mode;

		private String end_date;

		private String create_date;

		private String change_date;

		private String is_province;

		private String change_operator;

		private String create_operator;

		private String begin_date;

		private String brand_id;

		private String product_name;

		public String getBegin_date() {
			return begin_date;
		}

		public void setBegin_date(String begin_date) {
			this.begin_date = begin_date;
		}

		public String getBrand_id() {
			return brand_id;
		}

		public void setBrand_id(String brand_id) {
			this.brand_id = brand_id;
		}

		public String getChange_date() {
			return change_date;
		}

		public void setChange_date(String change_date) {
			this.change_date = change_date;
		}

		public String getChange_operator() {
			return change_operator;
		}

		public void setChange_operator(String change_operator) {
			this.change_operator = change_operator;
		}

		public String getCity_id() {
			return city_id;
		}

		public void setCity_id(String city_id) {
			this.city_id = city_id;
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

		public String getIs_province() {
			return is_province;
		}

		public void setIs_province(String is_province) {
			this.is_province = is_province;
		}

		public String getPay_mode() {
			return pay_mode;
		}

		public void setPay_mode(String pay_mode) {
			this.pay_mode = pay_mode;
		}

		public String getProduct_desc() {
			return product_desc;
		}

		public void setProduct_desc(String product_desc) {
			this.product_desc = product_desc;
		}

		public String getProduct_id() {
			return product_id;
		}

		public void setProduct_id(String product_id) {
			this.product_id = product_id;
		}

		public String getProduct_name() {
			return product_name;
		}

		public void setProduct_name(String product_name) {
			this.product_name = product_name;
		}
	}
}