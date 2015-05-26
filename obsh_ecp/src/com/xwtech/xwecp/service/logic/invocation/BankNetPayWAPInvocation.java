package com.xwtech.xwecp.service.logic.invocation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.context.ApplicationContext;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.DEL040025Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 建行充值
 * 
 * @author Tkk
 * 
 */
public class BankNetPayWAPInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(BankNetPayInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	/**
	 * Boss接口-获取订单号
	 */
	private static final String BOSS_INTERFACE_GET_ORDER = "cc_bugetcltsrl_357_1";

	/**
	 * BOSS接口-验证业务
	 */
	private static final String BOSS_INTERFACE_VALIDATE_ORDER = "cc_cccbdeal_231_WAP";

	/**
	 * 币种, 只支持人民币, 代码为01
	 */
	private static final String CURCODE = "01";

	/**
	 * 交易码, 目前为MP6000
	 */
	private static final String TXCODE = "MP6000";

	/**
	 * (地市, 地市所对应商户号)
	 */
	private Map<String, String> marchantMap = new HashMap<String, String>();

	/**
	 * (地市, 地市所对应建行行号)
	 */
	private Map<String, String> branchMap = new HashMap<String, String>();

	/**
	 * (地市, 地市所对应柜台号码)
	 */
	private Map<String, String> postMap = new HashMap<String, String>();

	/**
	 * 商户号
	 */
	private String marchant;

	/**
	 * 柜台号码
	 */
	private String post;

	/**
	 * 建行行号
	 */
	private String branch;

	public BankNetPayWAPInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));

		// 1苏州
		marchantMap.put("11", "105320548140054");
		branchMap.put("11", "322000000");
		postMap.put("11", "057156700");

		// 2淮安
		marchantMap.put("12", "105320857340001");
		branchMap.put("12", "320000000");
//		postMap.put("12", "822470190");
		postMap.put("12", "033518329");

		// 3宿迁
		marchantMap.put("13", "105321357340001");
		branchMap.put("13", "320000000");
		postMap.put("13", "877616242");

		// 4南京
		marchantMap.put("14", "105320157340006");
		branchMap.put("14", "320000000");
		postMap.put("14", "764302467");

		// 连云港
		marchantMap.put("15", "105320757340001");
		branchMap.put("15", "320000000");
		postMap.put("15", "822470190");

		// 5徐州
		marchantMap.put("16", "105320357340004");
		branchMap.put("16", "320000000");
		postMap.put("16", "741027213");

		// 6常州
		marchantMap.put("17", "105320473720003");
		branchMap.put("17", "320000000");
		postMap.put("17", "567486786");

		// 镇江
		marchantMap.put("18", "105321173720002");
		branchMap.put("18", "320000000");
		postMap.put("18", "923171282");

		// 7无锡
		marchantMap.put("19", "105320273720001");
		branchMap.put("19", "320000000");
		postMap.put("19", "783857947");

		// 8南通
		marchantMap.put("20", "105320657340002");
		branchMap.put("20", "320000000");
		postMap.put("20", "865644693");

		// 9泰州
		marchantMap.put("21", "105321257340004");
		branchMap.put("21", "320000000");
		postMap.put("21", "389573865");

		// 10盐城
		marchantMap.put("22", "105320973720001");
		branchMap.put("22", "320000000");
		postMap.put("22", "464583231");

		// 扬州
		marchantMap.put("23", "105321073720002");
		branchMap.put("23", "320000000");
		postMap.put("23", "861690322");
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config,
			List<RequestParameter> params) {
		DEL040025Result res = new DEL040025Result();
		String userCity = null;
		String userId = null;
		try {
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorCode("");
			res.setErrorMessage("");

			// 由于未开放功能,现提供一个给建行测试的链接[js_obsh_service/SIYUServlet?s1=手机号&s2=金额],所以要强制获取地市
			// userCity = (String) this.getParameters(params, "context_ddr_city");

			// 如果是带别人充值, 首先获取该用户的地市和id
			if (userCity == null || "".equals(userCity)) {
				BaseResult getCityResult = this.getCityAndUserId(accessId, config, params);
				Map<String, String> resultMap = (Map<String, String>) getCityResult.getReObj();
				userCity = resultMap.get("city");
				userId = resultMap.get("userId");
				if (userCity == null || userId == null) {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorMessage("获取用户地市或者用户ID失败");
					return res;
				}
			}
			
			//家庭主副号判断
			this.setParameter(params, "fixed_ddr_city", userCity);
			params.add(new RequestParameter("cityId", userCity));
			this.setParameter(params, "context_ddr_city", userCity);
			this.setParameter(params, "context_user_id", userId);
			this.setParameter(params, "qryMonth", DateTimeUtil.getTodayChar6());
			
			//路由信息
			this.setParameter(params, "context_route_type", "1");
			this.setParameter(params, "context_route_value", userCity);
			//boolean isHomeMaster = checkHomeMaster(accessId,config,params,res);
			//if(isHomeMaster){
				// 充值金额(元)
				String amount = (String) getParameters(params, "amount");
				// 测试代码, 如果入口充值参数为1元, 那么转换为0.1元
				if ("0".equals(amount)) {
					for (RequestParameter param : params) {
						if ("amount".equals(param.getParameterName())) {
							param.setParameterValue("0.1");
							amount = "0.1";
							break;
						}
					}
				}

				// 被充值的手机号
				String phoneNum = (String) getParameters(params, "phoneNum");

				// 商户号
				marchant = marchantMap.get(userCity);

				// 柜台号码
				post = postMap.get(userCity);

				// 建行行号
				branch = branchMap.get(userCity);

				if (marchant == null || post == null || branch == null) {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorMessage("所在地市商户信息不存在");
					return res;
				}

				// 1. 生成订单
				this.generateOrder(accessId, config, params, res, BOSS_INTERFACE_GET_ORDER);
				if (LOGIC_SUCESS.equals(res.getResultCode())) {

					// 2. 校验业务与预充值
					boolean validate = validateOrder(res, accessId, res.getOrderId(), amount, params);
					if (validate) {

						// 商户号
						res.setMerchantId(marchant);

						// 商户柜台号
						res.setPosId(post);

						// 建行分行代码
						res.setBranchId(branch);

						// 币种 (01--人民币)
						res.setCurcode(CURCODE);

						// 订单金额
						// 用于测试调为1毛
						if (amount.startsWith("0")) {
							res.setPayMent(amount);
						}
						// 正式用于30元, 50, 100, 200
						else {
							res.setPayMent(amount + ".00");
						}

						// 交易码
						res.setTxCode(TXCODE);

						// 备注1_订单号的前8位
						String orderDate = res.getOrderId().substring(0, 8);
						res.setRemark1(orderDate);

						// 备注2_商户号
						res.setRemark2(marchant);

						// 华为的备注1需要手机号
						//if (XWECPApp.NEED_CUT_CITYS.toString().indexOf(userCity) != -1) {

							String dateStr = DateTimeUtil.getTodayChar8();
							res.setRemark1(dateStr + '|' + phoneNum);
						//}

						// MAC码
						String mac = getMac4Result(res);
						res.setMac(mac);

					} else {
						res.setResultCode(LOGIC_ERROR);
					}

				} else {
					res.setResultCode(LOGIC_ERROR);
				}
			//}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	
	/**
	 * 验证支付业务与预充值
	 * 
	 * @param orderId
	 *            订单号
	 * @param amount
	 *            预充值(转换为分)
	 * @return
	 */
	private boolean validateOrder(DEL040025Result result, String accessId, String orderId, String amountStr,
			List<RequestParameter> params) {

		// 组建模板参数
		// 支付订单号
		this.setParameter(params, "orderId", orderId);

		// 订单金额
		// 用于测试调为1毛
		if (amountStr.startsWith("0")) {
			float amount = Float.parseFloat(amountStr);
			amountStr = (amount * 100) + "";
		}
		// 正式用于30元, 50, 100, 200
		else {
			int amount = Integer.parseInt(amountStr);
			amountStr = (amount * 100) + "";
		}

		this.setParameter(params, "money", amountStr);

		// 商户号
		this.setParameter(params, "merchant", marchant);

		// 分行号码
		this.setParameter(params, "branch", branch);

		// 商户柜台号码
		this.setParameter(params, "post", post);

		// 订单日期(订单的前8位)
		String orderDate = orderId.substring(0, 8);
		this.setParameter(params, "orderDate", orderDate);
		
		
		try {
			String rspXml = (String) this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext(
					BOSS_INTERFACE_VALIDATE_ORDER, params), accessId, BOSS_INTERFACE_VALIDATE_ORDER, super
					.generateCity(params)));

			// 使用Dom4j解析
			Document doc = DocumentHelper.parseText(rspXml);

			// 验证
			boolean validate = setResultCode(result, doc, BOSS_INTERFACE_VALIDATE_ORDER);

			if (validate) {
				String resultStr = doc.selectSingleNode("/operation_out/content/ret_code").getText();
				return "0".equals(resultStr);
			}

		} catch (Exception e) {
			logger.error(e, e);
		}
		return false;
	}

	/**
	 * 获取加密码提交给建行进行验证
	 * 
	 * @param res
	 * @return
	 */
	private String getMac4Result(DEL040025Result res) {
		StringBuffer sbb = new StringBuffer();
		sbb.append("MERCHANTID=").append(res.getMerchantId()).append("&").append("POSID=").append(res.getPosId())
				.append("&").append("BRANCHID=").append(res.getBranchId()).append("&").append("ORDERID=").append(
						res.getOrderId()).append("&").append("PAYMENT=").append(res.getPayMent()).append("&").append(
						"CURCODE=").append(res.getCurcode()).append("&").append("TXCODE=").append(res.getTxCode())
				.append("&").append("REMARK1=").append(res.getRemark1()).append("&").append("REMARK2=").append(
						res.getRemark2());

		String result = "";
		try {

			// 使用最简单的MD5加密
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(sbb.toString().getBytes());

			// 转换小写
			result = byteToAscII(digest.digest());

		} catch (NoSuchAlgorithmException e) {
			logger.error(e, e);
		}
		return result;
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
	private BaseResult getCityAndUserId(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult result = new BaseResult();
		Map<String, String> resultMap = new HashMap<String, String>();
		String rspXml = "";
		try {
			rspXml = (String) this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext("cc_cgetusercust_69", params), accessId, "cc_cgetusercust_69", super.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml)) {
				Document doc = DocumentHelper.parseText(rspXml);
				boolean isSuccess = setResultCode(result, doc, "cc_cgetusercust_69");
				if(isSuccess){
					String city = doc.selectSingleNode("/operation_out/content/user_info/user_city").getText();
					String userId = doc.selectSingleNode("/operation_out/content/user_info/user_id").getText();
					resultMap.put("city", city);
					resultMap.put("userId", userId);
					result.setReObj(resultMap);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return result;
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
	private BaseResult generateOrder(final String accessId, final ServiceConfig config,
			final List<RequestParameter> params, final DEL040025Result result, final String bossTemplate) {
		BaseResult res = new BaseResult();
		String rspXml = "";
		String unionPaySrl = "0";
		try {
			rspXml = (String) this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext(
					bossTemplate, params), accessId, bossTemplate, super.generateCity(params)));

			if (null != rspXml && !"".equals(rspXml)) {
				Document doc = DocumentHelper.parseText(rspXml);

				// 设置错误码
				boolean validate = setResultCode(result, doc, bossTemplate);

				// 流水号(订单号)
				if (validate) {
					unionPaySrl = doc.selectSingleNode("/operation_out/content/clt_operating_srl").getText();
					result.setOrderId(unionPaySrl);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 设置错误码
	 * 
	 * @param result
	 * @param doc
	 * @param bossTemplate
	 */
	private boolean setResultCode(BaseServiceInvocationResult result, Document doc, String bossTemplate) {
		boolean validate = true;

		// 结果码
		String errCode = doc.selectSingleNode("/operation_out/response/resp_code").getText();

		// 返回信息
		String errDesc = doc.selectSingleNode("/operation_out/response/resp_desc").getText();
		result.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);

		// 转换本地化结果信息
		if (!BOSS_SUCCESS.equals(errCode)) {
			ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL040025", bossTemplate, errCode);
			if (null != errDt) {
				errCode = errDt.getLiErrCode();
				errDesc = errDt.getLiErrMsg();
			}
			result.setErrorCode(errCode);
			result.setErrorMessage(errDesc);
			validate = false;
		}
		else
		{
			result.setErrorCode(errCode);
			result.setErrorMessage(errDesc);
		}
		return validate;
	}

	/**
	 * 转换ascii码
	 * 
	 * @param bySourceByte
	 * @return
	 */
	public String byteToAscII(byte[] bySourceByte) {
		int len, i;
		byte tb;
		char high, tmp, low;
		String result = new String();
		len = bySourceByte.length;
		for (i = 0; i < len; i++) {
			tb = bySourceByte[i];

			tmp = (char) ((tb >>> 4) & 0x000f);
			if (tmp >= 10)
				high = (char) ('a' + tmp - 10);
			else
				high = (char) ('0' + tmp);
			result += high;
			tmp = (char) (tb & 0x000f);
			if (tmp >= 10)
				low = (char) ('a' + tmp - 10);
			else
				low = (char) ('0' + tmp);

			result += low;
		}
		return result;
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