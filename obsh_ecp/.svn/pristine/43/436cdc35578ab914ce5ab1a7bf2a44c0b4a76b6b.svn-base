package com.xwtech.xwecp.service.logic.invocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mpi.client.exception.PayException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import org.springframework.context.ApplicationContext;

import chinapay.PrivateKey;
import chinapay.SecureLink;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.DEL110005Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ConfigurationRead;
import com.xwtech.xwecp.util.DateTimeUtil;
/**
 * 号卡销售-银联卡充值
 * 
 * @author YG
 * 
 */
public class CardUnionPayInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(CardUnionPayInvocation.class);

	//银联后台通知地址
	private static String bgRetUrl =  ConfigurationRead.getInstance().getValue("unionpay_bgRetUrl_18");
	//在线入网用回跳地址
	private static String regonline_merUrl = ConfigurationRead.getInstance().getValue("regonline_pageRetUrl");
	
	//数字签名文件路径
	private static String signFilePath = ConfigurationRead.getInstance().getValue("signFilePath");
	
	private static String extenName= ".key";

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private Map<String, String> dic = null;

	public CardUnionPayInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		dic = new HashMap<String, String>();
		//新商户号
		dic.put("11", "808080123100009");
		dic.put("12", "808080123100007");
		dic.put("13", "808080123100002");
		dic.put("14", "808080123100001");
		dic.put("15", "808080123100010");
		dic.put("16", "808080123100012");
		dic.put("17", "808080123100003");
		dic.put("18", "808080123100011");
		dic.put("19", "808080123100005");
		dic.put("20", "808080123100013");
		dic.put("21", "808080123100006");
		dic.put("22", "808080123100008");
		dic.put("23", "808080123100004");

	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		DEL110005Result res = new DEL110005Result();
		String userCity = null;
		String userId = null;
		try {
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			userCity = (String)this.getParameters(params, "context_ddr_city");
			userId = (String)this.getParameters(params, "context_user_id");
			String toNumber = (String) getParameters(params, "phoneNum");
			if (userCity == null || "".equals(userCity) || userId == null || "".equals(userId)) {
				BaseResult getCityResult = this.getCity(accessId, config, params);
				if (LOGIC_SUCESS.equals(getCityResult.getResultCode())) {
					Map map = (Map)getCityResult.getReObj();
					userCity = (String)map.get("city");
					userId = (String)map.get("userId");
				} else {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(getCityResult.getErrorCode());
					res.setErrorMessage(getCityResult.getErrorMessage());
					return res;
				}
			}
			
			this.setParameter(params, "context_ddr_city", userCity);
			this.setParameter(params, "fixed_ddr_city", userCity);
			this.setParameter(params, "context_user_id", userId);
			this.setParameter(params, "qryMonth", DateTimeUtil.getTodayChar6());
			
	
			params.add(new RequestParameter("cityId", userCity));
			
			String bossTemplate = "cc_bucardgetcltsrl";
	
			
			// 生成订单
			BaseResult generateResult = this.generateOrder(accessId, config, params, res, bossTemplate);
			if (LOGIC_SUCESS.equals(generateResult.getResultCode())) {
				String bossDate = res.getBossDate();
				String unionPaySrl = res.getUnionPaySrl();
				int amount = Integer.parseInt((String) getParameters(params, "amount"));// 订单金额
				amount *= 100;
				String checkSrl = null;
				
				checkSrl = unionPaySrl;
				
				//checkSrl = unionPaySrl;
				
				// 设置校验参数
				params.add(new RequestParameter("checkSrl", checkSrl));
				params.add(new RequestParameter("bossDate", bossDate));
				params.add(new RequestParameter("amount", amount));
				
				BaseResult checkOrderResult = new BaseResult();
				
				String bossCheckTemplate = "cc_bucheckdeal_yl";
				this.setParameter(params, "payType", "5"); //5：银联 15：支付宝
				
				//校验订单
				checkOrderResult = this.checkOrder(accessId, config, params, res, bossCheckTemplate);
				if (LOGIC_SUCESS.equals(checkOrderResult.getResultCode())) {
					// 设置tran
					setTranValue(res, userCity, unionPaySrl, amount, bossDate,toNumber);
				} else {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(checkOrderResult.getErrorCode());
					res.setErrorMessage(checkOrderResult.getErrorMessage());
				}
			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(generateResult.getErrorCode());
				res.setErrorMessage(generateResult.getErrorMessage());
			}
		//	}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

//	private boolean checkHomeMaster(String accessId, ServiceConfig config, List<RequestParameter> params, DEL110005Result res) {
//		boolean rs = false;
//		String reqXml = "";
//		String rspXml = "";
//		try {
//			String toNumber = (String) getParameters(params, "phoneNum");
//			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqryhmember_749", params);
//
//			logger.debug(" ====== 查询用户信息请求报文 ======\n" + reqXml);
//
//			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cqryhmember_749", super.generateCity(params)));
//			logger.debug(" ====== 查询用户信息返回报文 ======\n" + rspXml);
//			if (null != rspXml && !"".equals(rspXml)) {
//				Element root = this.getElement(rspXml.getBytes());
//				String errCode = root.getChild("response").getChildText("resp_code");
//				String errDesc = root.getChild("response").getChildText("resp_desc");
//
//				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
//				if (!BOSS_SUCCESS.equals(errCode)) {
//					if("-13734".equals(errCode)){//#家庭成员不存在
//						res.setResultCode(LOGIC_SUCESS);
//						rs = true;
//					}else{
//						ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL110005", "cc_cqryhmember_749", errCode);
//						if (null != errDt) {
//							errCode = errDt.getLiErrCode();
//							errDesc = errDt.getLiErrMsg();
//						}
//						res.setErrorCode(errCode);
//						res.setErrorMessage(errDesc);
//					}
//				}
//				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
//					XPath xpath = XPath.newInstance("/operation_out/content/homemember_user_id/chomememberdt");
//					List<Element> list = xpath.selectNodes(root);
//					List<CcCQryhMemberBean> listhMember = new ArrayList<CcCQryhMemberBean>();
//					if(list != null){
//						for (Element element : list) {
//							String homemember_remark = element.getChildTextTrim("homemember_householder_flag");
//							String homemember_msisdn = element.getChildTextTrim("homemember_msisdn");
//							//String applyDate = element.getChildTextTrim("homemember_apply_date");
//							
//							
//							if (toNumber.equals(homemember_msisdn)) {
//								if("1".equals(homemember_remark)){
//									CcCQryhMemberBean memberbean = new CcCQryhMemberBean();
//									memberbean.setHomemember_member_name(element.getChildTextTrim("homemember_member_name"));
//									memberbean.setHomemember_msisdn(element.getChildTextTrim("homemember_msisdn"));
//									memberbean.setHomemember_customer_id(element.getChildTextTrim("homemember_home_user_id"));
//									memberbean.setHomemember_member_type("1");
//									listhMember.add(memberbean);
//								}else {
//									CcCQryhMemberBean memberbean = new CcCQryhMemberBean();
//									memberbean.setHomemember_member_name(element.getChildTextTrim("homemember_member_name"));
//									memberbean.setHomemember_msisdn(element.getChildTextTrim("homemember_msisdn"));
//									memberbean.setHomemember_customer_id(element.getChildTextTrim("homemember_home_user_id"));
//									String applyDate = element.getChildTextTrim("homemember_apply_date");
//									String distanceDT = DateTimeUtil.getDistanceDT(applyDate, DateTimeUtil.getTodayChar14(), "s");
//									if (Long.valueOf(distanceDT).intValue() < 0 ) {
//										memberbean.setHomemember_member_type("2");
//									} else {
//										memberbean.setHomemember_member_type("0");
//									}
//									listhMember.add(memberbean);
//								}
//							}
//							
////							if(toNumber.equals(element.getChildText("homemember_msisdn"))){
////								String memberType = element.getChildTextTrim("homemember_member_type");
////								if("1".equals(memberType) || "2".equals(memberType)){//家庭主号
////									rs = true;
////								}else{//家庭附号
////									String applyDate = element.getChildText("homemember_apply_date");
////									String distanceDT = DateTimeUtil.getDistanceDT(applyDate, DateTimeUtil.getTodayChar14(), "s");
////									if (Long.valueOf(distanceDT).intValue() < 0 ) {
////										rs = true;//预约开通
////									} else {
////										rs = false;
////									}
////								}
////								break;
////							}
//						}
//						//判断是否主号
//						if (listhMember != null && listhMember.size() > 0) {
//							CcCQryhMemberBean memberBean = (CcCQryhMemberBean) listhMember.get(0);
//							//家庭户主
//							if ("1".equals(memberBean.getHomemember_member_type()) || "2".equals(memberBean.getHomemember_member_type())) {
//								rs = true;
//							}else{
//								rs = true;
//							}
//						}else{
//							
//							rs = false;
//						}
//						
//						
//					}
//				}
//			}
//		} catch (Exception e) {
//			logger.error(e, e);
//		}
//		return rs;
//	}

	/**
	 * 设置返回的Tran值
	 * 
	 * @param res
	 * @param userCity
	 * @param unionPaySrl
	 * @param amount
	 * @param bossDate
	 * @throws PayException
	 * @throws Exception
	 */
	protected void setTranValue(DEL110005Result res, String userCity, String unionPaySrl, int amount, String bossDate,String toNumber)
			throws PayException, Exception {
	
		String curyId = "156";
		String transType = "0001";
		
		//华为用回调地址 
		String merId = dic.get(userCity);
		
		try {
			PrivateKey key=new PrivateKey();
			boolean flag;
//			flag = key.buildKey(merId,0,"C:/keys/MerPrK_" + merId+ ".key");
			flag = key.buildKey(merId, 0, signFilePath + merId + extenName);
			String strAmount = formatUnionPayAmount(amount);
			String checkValue = "";
			if (flag){
				SecureLink t = new chinapay.SecureLink (key);
				String signMsg = "";
				if(needCutCity(userCity)){
					signMsg = merId +  unionPaySrl + strAmount + curyId + bossDate.substring(0, 8) + transType + bossDate + "|" + toNumber;
				}else{
					signMsg = merId +  unionPaySrl + strAmount + curyId + bossDate.substring(0, 8) + transType + bossDate;
				}
				
				//华为
				//String signMsg = merId +  unionPaySrl + strAmount + curyId + bossDate.substring(0, 8) + transType + bossDate +  "|" + number;
				checkValue = t.Sign(signMsg);
				res.setVersion("20070129");
				res.setChkValue(checkValue);
				res.setBgRetUrl(bgRetUrl);
				res.setPageRetUrl(regonline_merUrl);
				res.setMerId(merId);
				res.setOrdId(unionPaySrl);
				res.setTransAmt(strAmount);
				res.setCuryId(curyId);
				res.setTransDate(bossDate.substring(0, 8));
				res.setTransType(transType);

				res.setPriv1(bossDate + "|" + toNumber);
				res.setUnionPaySrl(unionPaySrl);

				}else{
					res.setErrorCode("-100001"); // TODO
					res.setErrorMessage("生成数字签名失败");
				}
		} catch (Exception e) {
			res.setErrorCode("-100002"); // TODO
			res.setErrorMessage("银联卡充值失败");
		}
	}

	/**
	 * 查询用户信息
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getCity(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetusercust_69", params);

			logger.debug(" ====== 查询用户信息请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetusercust_69", super.generateCity(params)));
			logger.debug(" ====== 查询用户信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL110005", "cc_cgetusercust_69", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					Map retMap = new HashMap();
					
					XPath xpath = XPath.newInstance("/operation_out/content/user_info/user_city");
					String city = ((Element) xpath.selectSingleNode(root)).getText();
					xpath = XPath.newInstance("/operation_out/content/user_info/user_id");
					String userId = ((Element) xpath.selectSingleNode(root)).getText();
					retMap.put("city", city);
					retMap.put("userId", userId);
					//res.setReObj();
					res.setReObj(retMap);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 创建订单
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult generateOrder(final String accessId, final ServiceConfig config, final List<RequestParameter> params,
			final DEL110005Result result, final String bossTemplate) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		String unionPaySrl = "0";
		String bossDate = "";

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate, params);

			logger.debug(" ====== 创建订单请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, bossTemplate, super.generateCity(params)));
			logger.debug(" ====== 创建订单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL110005", bossTemplate, errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath.newInstance("/operation_out/content/buoperatingdetail_clt_sys_date");
					//bossDate = ((Element) xpath.selectSingleNode(root)).getText();
					xpath = XPath.newInstance("/operation_out/content/clt_operating_srl");
					unionPaySrl = ((Element) xpath.selectSingleNode(root)).getText();
					result.setBossDate(bossDate);
					result.setUnionPaySrl(unionPaySrl);

				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * check订单
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult checkOrder(final String accessId, final ServiceConfig config, final List<RequestParameter> params,
			final DEL110005Result result, final String bossTemplate) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate, params);

			logger.debug(" ====== 创建订单请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, bossTemplate, super.generateCity(params)));
			logger.debug(" ====== 创建订单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL110005", bossTemplate, errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	private String formatUnionPayAmount(int amount){
		String strAmount = String.valueOf(amount);
		if(!StringUtils.isBlank(strAmount)){
			while(strAmount.length() < 12){
				strAmount = "0" + strAmount;
			}
			
		}
		return strAmount;
	}

	public class CcCQryhMemberBean {

		/**路由字段*/
		private String ddr_city;
		/**用户归属客户代码*/
		private String homemember_customer_id;
		/**查询的方式*/
		private String virtual_org_qry_type;
		/**成员类型*/
		private String homemember_member_type;
		/**成员名称*/
		private String homemember_member_name;
		/**服务号码*/
		private String homemember_msisdn;
		/**地市*/
		private String homemember_city_id;
		/**成员状态*/
		private String homemember_state;
		/**月份YYYYMM*/
		private String operating_accdeatil_bill_month;

		public String getDdr_city() {
			return ddr_city;
		}

		public void setDdr_city(String ddr_city) {
			this.ddr_city = ddr_city;
		}

		public String getHomemember_city_id() {
			return homemember_city_id;
		}

		public void setHomemember_city_id(String homemember_city_id) {
			this.homemember_city_id = homemember_city_id;
		}

		public String getHomemember_customer_id() {
			return homemember_customer_id;
		}

		public void setHomemember_customer_id(String homemember_customer_id) {
			this.homemember_customer_id = homemember_customer_id;
		}

		public String getHomemember_member_name() {
			return homemember_member_name;
		}

		public void setHomemember_member_name(String homemember_member_name) {
			this.homemember_member_name = homemember_member_name;
		}

		public String getHomemember_member_type() {
			return homemember_member_type;
		}

		public void setHomemember_member_type(String homemember_member_type) {
			this.homemember_member_type = homemember_member_type;
		}

		public String getHomemember_msisdn() {
			return homemember_msisdn;
		}

		public void setHomemember_msisdn(String homemember_msisdn) {
			this.homemember_msisdn = homemember_msisdn;
		}

		public String getHomemember_state() {
			return homemember_state;
		}

		public void setHomemember_state(String homemember_state) {
			this.homemember_state = homemember_state;
		}

		public String getOperating_accdeatil_bill_month() {
			return operating_accdeatil_bill_month;
		}

		public void setOperating_accdeatil_bill_month(String operating_accdeatil_bill_month) {
			this.operating_accdeatil_bill_month = operating_accdeatil_bill_month;
		}

		public String getVirtual_org_qry_type() {
			return virtual_org_qry_type;
		}

		public void setVirtual_org_qry_type(String virtual_org_qry_type) {
			this.virtual_org_qry_type = virtual_org_qry_type;
		}
	}
}