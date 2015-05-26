package com.xwtech.xwecp.service.logic.invocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mpi.client.exception.PayException;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import org.springframework.context.ApplicationContext;

import com.hitrust.trustpay.client.MerchantConfig;
import com.hitrust.trustpay.client.TrxException;
import com.hitrust.trustpay.client.b2c.Order;
import com.hitrust.trustpay.client.b2c.PaymentRequest;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.DEL040036Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 农行直充
 * 
 * @author 杨可帆
 * 
 */
public class ABCPayInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger
			.getLogger(ABCPayInvocation.class);

	// 农行回调华为的生产地址
	private static String bgRetUrl = "http://221.178.251.163/jsabcbank/servlet/ReciveAbcServerRtn";
	
	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private Map<String, String> dic = null;
	//地市序号-重要
	private Map<String, Integer> dicNum = null;

	public ABCPayInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		dic = new HashMap<String, String>();
		// 新商户号
		// 11苏州12淮安13宿迁14南京15连云港16徐州17常州18镇江19无锡20南通21泰州22盐城23扬州
		dic.put("11", "232010100336A01");
		dic.put("12", "232010100378A02");
		dic.put("13", "232010100401A02");
		dic.put("14", "232010100295A01");
		dic.put("15", "232010100394A02");
		dic.put("16", "232010100419A02");
		dic.put("17", "232010100310A01");
		dic.put("18", "232010100302A01");
		dic.put("19", "232010100328A01");
		dic.put("20", "232010100360A01");
		dic.put("21", "232010100352A01");
		dic.put("22", "232010100386A02");
		dic.put("23", "232010100344A01");
		
		dicNum = new HashMap<String, Integer>();
		dicNum.put("11", 1);
		dicNum.put("12", 2);
		dicNum.put("13", 3);
		dicNum.put("14", 4);
		dicNum.put("15", 5);
		dicNum.put("16", 6);
		dicNum.put("17", 7);
		dicNum.put("18", 8);
		dicNum.put("19", 9);
		dicNum.put("20", 10);
		dicNum.put("21", 11);
		dicNum.put("22", 12);
		dicNum.put("23", 13);
		
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		DEL040036Result res = new DEL040036Result();
		String userCity = null;
		String userId = null;
		try { 
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			userCity = (String) this.getParameters(params, "context_ddr_city");
			userId = (String) this.getParameters(params, "context_user_id");
			String toNumber = (String) getParameters(params, "phoneNum");
			String abcAmount = (String) getParameters(params,"amount"); //接收页面传值；调用农行单位 元
			if (userCity == null || "".equals(userCity) || userId == null
					|| "".equals(userId)) {
				BaseResult getCityResult = this.getCity(accessId, config,
						params);
				if (LOGIC_SUCESS.equals(getCityResult.getResultCode())) {
					Map map = (Map) getCityResult.getReObj();
					userCity = (String) map.get("city");
					userId = (String) map.get("userId");
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
			// 5银联1建行3招行15支付宝4农行
			this.setParameter(params, "bankType", "4");

			params.add(new RequestParameter("cityId", userCity));

			String bossTemplate = "cc_bugetcltsrl_357";

			// 生成订单
			BaseResult generateResult = this.generateOrder(accessId, config,
					params, res, bossTemplate);
			if (LOGIC_SUCESS.equals(generateResult.getResultCode())) {
				String bossDate = res.getBossDate();
				String unionPaySrl = res.getOrderNo();
			int	abcParamAmount  = Integer.parseInt(abcAmount);
				
			int crmAmount = Integer.parseInt(abcAmount);// 订单金额  。crm单位：分
			crmAmount *= 100;
				//假数据，测试用。务必注掉
				//int amount = 10;
				

				// 设置校验参数
				params.add(new RequestParameter("checkSrl", unionPaySrl));
				params.add(new RequestParameter("bossDate", bossDate));
				params.add(new RequestParameter("amount", crmAmount));
				params.add(new RequestParameter("phoneNum", toNumber));
				params.add(new RequestParameter("ddrCity", userCity));
				params.add(new RequestParameter("source", ""));
				params.add(new RequestParameter("openFlag", ""));
				BaseResult checkOrderResult = new BaseResult();
				
				//农行专用外围调业务检查。
				String bossCheckTemplate = "cc_bucheckdeal_abc_360";

				// 校验订单
				checkOrderResult = this.checkOrder(accessId, config, params,
						res, bossCheckTemplate);
				if (LOGIC_SUCESS.equals(checkOrderResult.getResultCode())) {
					// 设置请求数据
					this.setTranValue(res, userCity, abcParamAmount,toNumber);
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
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 设置返回的Tran值
	 * 
	 * @param res
	 *            返回结果对象
	 * @param userCity
	 *            地市
	 * @param amount
	 *            金额
	 * @throws TrxException
	 *             农行充值返回异常
	 * @throws Exception
	 */
	protected void setTranValue(DEL040036Result res, String userCity, int amount,String phoneNum)
			throws PayException, Exception {

		String merId = dic.get(userCity);
		try {
			// 格式化金额
			// String strAmount = formatUnionPayAmount(amount);
			// 2、生成订单对象
			Order tOrder = new Order();
			tOrder.setOrderNo(res.getOrderNo()); // 设定订单编号 （必要信息）
			tOrder.setOrderDesc("ABC ORDER"); // 设定订单说明
			Map<String, String> map = this.formatBossDate(res.getBossDate());
			// tOrder.setOrderDate("2012/12/20");
			// tOrder.setOrderTime("13:55:30");
			tOrder.setOrderDate(map.get("orderDate")); // 设定订单日期 （必要信息 -
														// YYYY/MM/DD）
			tOrder.setOrderTime(map.get("orderTime")); // 设定订单时间 （必要信息 -
														// HH:MM:SS）
			
			//tOrder.setOrderAmount(0.1); // 设定订单金额 （必要信息） 假数据务必注掉
			tOrder.setOrderAmount(amount); // 设定订单金额 （必要信息）
			tOrder.setOrderURL("http://127.0.0.1/Merchant/MerchantQueryOrder.jsp"); // 设定查询订单网址
			tOrder.setBuyIP("127.0.0.1"); // 设定订单IP
			// 3、生成定单订单对象，并将订单明细加入定单中（可选信息）
			// tOrder.addOrderItem(new OrderItem("IP000001", "中国移动IP卡", 100.00f,
			// 1));
			// tOrder.addOrderItem(new OrderItem("IP000002", "网通IP卡" , 90.00f,
			// 2));

			MerchantConfig tMerchantConfig = MerchantConfig.getUniqueInstance();
			// 网上支付平台交易网址
			String trustPayIETrxURL = tMerchantConfig.getTrustPayIETrxURL();
			// 错误URL地址
			String errorUrl = tMerchantConfig.getMerchantErrorURL();
			// 4、生成支付请求对象
			PaymentRequest tPaymentRequest = new PaymentRequest();
			tPaymentRequest.setOrder(tOrder); // 设定支付请求的订单 （必要信息）
			tPaymentRequest.setProductType("1"); // 设定商品种类 （必要信息）
			// PaymentRequest.PRD_TYPE_ONE：非实体商品，如服务、IP卡、下载MP3、...
			tPaymentRequest.setPaymentType("1"); // 设定支付类型1：农行卡支付 2：国际卡支付
													// 3：农行贷记卡支付 A:支付方式合并
			// PaymentRequest.PAY_TYPE_ABC：农行卡支付
			// PaymentRequest.PAY_TYPE_INT：国际卡支付
			tPaymentRequest.setNotifyType("1"); // 设定商户通知方式
			// 0：URL页面通知
			// 1：服务器通知
			tPaymentRequest.setResultNotifyURL(bgRetUrl); // 设定支付结果回传网址
																// （必要信息）
			tPaymentRequest.setMerchantRemarks(phoneNum+"|"+merId); // 设定商户备注信息【商户号提交过去】
			tPaymentRequest.setPaymentLinkType("1");// 设定支付接入方式1：internet网络接入
													// 2：手机网络接入 3:数字电视网络接入
													// 4:智能客户端

			Integer n = dicNum.get(userCity);
			// 发送支付请求
			String signaTure = tPaymentRequest.genSignature(n);
			res.setResultCode(LOGIC_SUCESS);
			// 保留字段
			res.setPriv1("");
			res.setSignaTure(signaTure);
			res.setErrorUrl(errorUrl);
			res.setTrustPayIETrxURL(trustPayIETrxURL);
		} catch (TrxException e) {
			res.setResultCode(LOGIC_ERROR);
			res.setErrorCode(e.getCode()); // TODO
			res.setErrorMessage(e.getMessage());
		} catch (Exception e) {
			res.setResultCode(LOGIC_ERROR);
			res.setErrorCode("-100002"); // TODO
			res.setErrorMessage("农行直充失败");
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
	protected BaseResult getCity(final String accessId,
			final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetusercust_69",params);

			logger.debug(" ====== 查询用户信息请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId,
							"cc_cgetusercust_69", super.generateCity(params)));
			logger.debug(" ====== 查询用户信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode(
							"DEL040036", "cc_cgetusercust_69", errCode);
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
	protected BaseResult generateOrder(final String accessId,
			final ServiceConfig config, final List<RequestParameter> params,
			final DEL040036Result result, final String bossTemplate) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		String unionPaySrl = "0";
		String bossDate = "";

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate, params);

			logger.debug(" ====== 创建订单请求报文 ======\n" + reqXml);
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml,
					accessId, bossTemplate, super.generateCity(params)));
			logger.debug(" ====== 创建订单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText(
						"resp_code");
				String errDesc = root.getChild("response").getChildText(
						"resp_desc");

				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode(
							"DEL040036", bossTemplate, errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath
							.newInstance("/operation_out/content/buoperatingdetail_clt_sys_date");
					bossDate = ((Element) xpath.selectSingleNode(root)).getText();
					xpath = XPath.newInstance("/operation_out/content/clt_operating_srl");
					unionPaySrl = ((Element) xpath.selectSingleNode(root)).getText();
					result.setBossDate(bossDate);
					result.setOrderNo(unionPaySrl);
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
	protected BaseResult checkOrder(final String accessId,
			final ServiceConfig config, final List<RequestParameter> params,
			final DEL040036Result result, final String bossTemplate) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate, params);

			logger.debug(" ====== 创建订单请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml,
					accessId, bossTemplate, super.generateCity(params)));
			logger.debug(" ====== 创建订单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText(
						"resp_code");
				String errDesc = root.getChild("response").getChildText(
						"resp_desc");

				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode(
							"DEL040036", bossTemplate, errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS
						: LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 把BOSS返回的订单时间转换成农行识别的时间
	 * 
	 * @param bossDate
	 *            订单生成时间
	 * @return
	 */
	private Map<String, String> formatBossDate(String bossDate) {
		Map<String, String> map = new HashMap<String, String>();
		String orderDate = bossDate.substring(0, 4) + "/"
				+ bossDate.substring(4, 6) + "/" + bossDate.substring(6, 8);
		String orderTime = bossDate.substring(8, 10) + ":"
				+ bossDate.substring(10, 12) + ":" + bossDate.substring(12, 14);
		map.put("orderDate", orderDate);
		map.put("orderTime", orderTime);
		return map;
	}


}