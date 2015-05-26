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

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.alipay.services.AlipayService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.DEL040099Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;
/**
 * 支付宝充值
 * 
 * @author 杨光
 * 
 */
public class AliWPayInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(AliWPayInvocation.class);
	
  
	//数字签名文件路径
	//private static String signFilePath = ConfigurationRead.getInstance().getValue("signFilePath");
	
	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private Map<String, String> dicAccount = null;
	private Map<String, String> dicPid = null;
	private Map<String, String> dicKey = null;

	public AliWPayInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		//各个地市支付宝账号
		dicAccount = new HashMap<String, String>();
		dicAccount.put("11", "szydcmcc_web@163.com");
		dicAccount.put("12", "hayd_2011@sina.com");
		dicAccount.put("13", "dongcn@sq.js.chinamobile.com");
		dicAccount.put("14", "nj12580lm@nj.js.chinamobile.com");
		dicAccount.put("15", "zhangwzx@lyg.js.chinamobile.com");
		dicAccount.put("16", "jsxzcmcc2011@sina.com");
		dicAccount.put("17", "cmcc_cz2011@sina.com");
		dicAccount.put("18", "1217756907@qq.com");
		dicAccount.put("19", "WX12580LM@wx.js.chinamobile.com");
		dicAccount.put("20", "434494420@139.com");
		dicAccount.put("21", "15850888888@139.com");
		dicAccount.put("22", "yc_scbywzczx@139.com");
		dicAccount.put("23", "yzzfbwt@163.com");
		
		//各个地市pid
		dicPid = new HashMap<String, String>();
		dicPid.put("11", "2088501897511656");
		dicPid.put("12", "2088501893468871");
		//dicPid.put("12", "2088101568345155"); //测试pid
		dicPid.put("13", "2088501873652579");
		dicPid.put("14", "2088501877953920");
		dicPid.put("15", "2088501902925298");
		dicPid.put("16", "2088501893958312");
		dicPid.put("17", "2088501902558588");
		dicPid.put("18", "2088501877931014");
		dicPid.put("19", "2088501890828104");
		dicPid.put("20", "2088501870717967");
		dicPid.put("21", "2088501877932420");
		dicPid.put("22", "2088501881342211");
		dicPid.put("23", "2088501881319729");
		
		
		//各个地市key
		dicKey = new HashMap<String, String>();
		dicKey.put("11", "qnu4g14is8gd3fl5o1rbxdoumgw7zap8");
		dicKey.put("12", "5yyfi2fj1zbr73dziw4bu6bi7j5qwu8p");
		//dicKey.put("12", "xu6xamwvgk5b51ahco9sgpbxy1e49ve9"); //测试key
		dicKey.put("13", "euo93lpt6oudhnby96nrimrvc3ysehbp");
		dicKey.put("14", "ooqw1g6qwwhym7dri9mm5iunrmezshtq");
		dicKey.put("15", "kxf7afltvq164v0smjb6duwx6c7zkujh");
		dicKey.put("16", "6zcalezcipu1hp61nj5x4mhqav7iowyh");
		dicKey.put("17", "y34wb8erds0colgirmhw7gd5fotvfrux");
		dicKey.put("18", "oaunkm0vnxby5cn1crb1g7s049trx12i");
		dicKey.put("19", "o739xdbbxv23pc56sv7situtfvmobdbj");
		dicKey.put("20", "873pygds194rtggxvidp96leavmp68tb");
		dicKey.put("21", "9m2f3ik8f5375adknyizhdxt0u8p3gka");
		dicKey.put("22", "ikekahsvi02t34r57m8qtqvmjrhkzshx");
		dicKey.put("23", "ona4e1xln55a2d6g127mykdjjihsihv8");

	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		DEL040099Result res = new DEL040099Result();
		String userCity = null;
		String userId = null;
		try {
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			userCity = (String)this.getParameters(params, "context_ddr_city");
			userId = (String)this.getParameters(params, "context_user_id");
			String toNumber = (String) getParameters(params, "phoneNum");
			String unionPaySrl = (String) getParameters(params, "checkSrl");
			String bossDate = (String) getParameters(params, "bossDate");
			//充值类型:0-普通充值；1-在线入网充值；2-地市营销充值
			String flag = Integer.toString((Integer)getParameters(params, "payFlag"));
			if (userCity == null || "".equals(userCity)) {
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
			this.setParameter(params, "qryMonth",DateTimeUtil.getTodayChar6());
			this.setParameter(params, "qryMonth", bossDate);
			this.setParameter(params, "checkSrl", unionPaySrl);
			
			String paymethod = (String)getParameters(params, "paymethod");
			String bankCode = (String)getParameters(params, "bankcode"); //直充需要银行编码，账户充值空

			params.add(new RequestParameter("cityId", userCity));
			
			String bossTemplate = "cc_bugetcltsrl_357";
			
				//TODO 假数据
				//String amount = "";
				String amount = (String) getParameters(params, "amount");// 订单金额，单位元,-金额已经经过校验，必须是30、50、100等
				//校验订单接口，
				String checkSrl = unionPaySrl;
				
				// 设置校验参数
				params.add(new RequestParameter("checkSrl", checkSrl));
				params.add(new RequestParameter("bossDate", bossDate));
				//TODO 假金额
				//params.add(new RequestParameter("amount", 10));
				params.add(new RequestParameter("amount", Integer.parseInt(amount) * 100));
				BaseResult checkOrderResult = new BaseResult();
				
				String bossCheckTemplate = "cc_bucheckdeal_359_hw";

				this.setParameter(params, "source", "");
				
				if("0".equals(flag)){ //普通支付宝充值
					bossCheckTemplate = "cc_bucheckdeal_359_hw";
				}else if("1".equals(flag)) {// 在线入网支付宝充值
					bossCheckTemplate = "cc_bucheckdeal_907";
					this.setParameter(params, "payType", "15"); //5：银联 15：支付宝
				}else if("3".equals(flag)){//客户端充值
					bossCheckTemplate = "cc_bucheckdealclient_361";
					this.setParameter(params, "payType", "15"); //5：银联 15：支付宝
				}
				
				//校验订单
				checkOrderResult = this.checkOrder(accessId, config, params, res, bossCheckTemplate);
				if (LOGIC_SUCESS.equals(checkOrderResult.getResultCode())) {
					// 设置tran
					setTranValue(res, userCity, unionPaySrl, amount,paymethod,bankCode,toNumber);
				} else {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(checkOrderResult.getErrorCode());
					res.setErrorMessage(checkOrderResult.getErrorMessage());
				}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

//	private boolean checkHomeMaster(String accessId, ServiceConfig config, List<RequestParameter> params, DEL040029Result res) {
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
//						ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL040029", "cc_cqryhmember_749", errCode);
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
	protected void setTranValue(DEL040099Result res, String userCity, String unionPaySrl, 
			String amount,String parampaymethod,String paramBankCode,String toNumber)
			throws PayException, Exception {
		try {
			
			//请与贵网站订单系统中的唯一订单号匹配
			String out_trade_no = unionPaySrl;
			
			//订单名称，显示在支付宝收银台里的“商品名称”里，显示在支付宝的交易管理的“商品名称”的列表里。
			String subject = "手机话费*商品订单号：" + out_trade_no; //商品名称
			
			//订单描述、订单详细、订单备注，显示在支付宝收银台里的“商品描述”里
			String body = "手机话费"; //商品描述，推荐格式：商品名称（订单编号：订单编号）
			
			//订单总金额，显示在支付宝收银台里的“应付总额”里
			//TODO 假数据
			//String total_fee = "0.01";
			String total_fee = amount+".00"; //订单总价 单位元？（0.01～100000000.00，小数点后 面最多两位 。如20.00，金额已经校验过，

			//默认支付方式，取值见“即时到帐接口”技术文档中的请求参数列表
			String paymethod = parampaymethod;//赋值:bankPay(网银);cartoon(卡通); directPay(余额)
			
			//默认网银代号，代号列表见“即时到帐接口”技术文档“附录”→“银行列表”
			String defaultbank = paramBankCode;

			//防钓鱼时间戳 TODO
			String anti_phishing_key  = "";
			
			//获取客户端的IP地址，建议：编写获取客户端IP地址的程序 TODO
			String exter_invoke_ip= "";
			//注意：
			//1.请慎重选择是否开启防钓鱼功能
			//2.exter_invoke_ip、anti_phishing_key一旦被设置过，那么它们就会成为必填参数
			//3.开启防钓鱼功能后，服务器、本机电脑必须支持远程XML解析，请配置好该环境。
			//4.建议使用POST方式请求数据
			//示例：
			//anti_phishing_key = AlipayService.query_timestamp();	//获取防钓鱼时间戳函数
			//exter_invoke_ip = "202.1.1.1";
			
			//扩展功能参数——其他///
			
			//自定义参数，可存放任何内容（除=、&等特殊字符外），不会显示在页面上
			String extra_common_param = toNumber + "|" + dicPid.get(userCity);
			
			//默认买家支付宝账号
			String buyer_email = "";
			
			//商品展示地址，要用http:// 格式的完整路径，不允许加?id=123这类自定义参数
			String show_url = "";  //根据集成的网站而定 例如：http://wow.alipay.com
			
			//扩展功能参数——分润(若要使用，请按照注释要求的格式赋值)//
			
			//提成类型，该值为固定值：10，不需要修改
			String royalty_type = "";
			//提成信息集
			String royalty_parameters ="";
			//注意：
			//与需要结合商户网站自身情况动态获取每笔交易的各分润收款账号、各分润金额、各分润说明。最多只能设置10条
			//各分润金额的总和须小于等于total_fee
			//提成信息集格式为：收款方Email_1^金额1^备注1|收款方Email_2^金额2^备注2
			//示例：
			//royalty_type = "10"
			//royalty_parameters	= "111@126.com^0.01^分润备注一|222@126.com^0.01^分润备注二"
			
			//////////////////////////////////////////////////////////////////////////////////
			
			String[] arrAlipayAccount = {dicAccount.get(userCity),dicPid.get(userCity),dicKey.get(userCity)};

			//把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
	        sParaTemp.put("payment_type", "1");
	        sParaTemp.put("out_trade_no", out_trade_no);
	        sParaTemp.put("subject", subject);
	        sParaTemp.put("body", body);
	        sParaTemp.put("total_fee", total_fee);
	        sParaTemp.put("show_url", show_url);
	        sParaTemp.put("paymethod", paymethod);
	        sParaTemp.put("defaultbank", defaultbank);
	        sParaTemp.put("anti_phishing_key", anti_phishing_key);
	        sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
	        sParaTemp.put("extra_common_param", extra_common_param);
	        sParaTemp.put("buyer_email", buyer_email);
	        sParaTemp.put("royalty_type", royalty_type);
	        sParaTemp.put("royalty_parameters", royalty_parameters);
	        sParaTemp.put("it_b_pay", "3h");
			//构造函数，生成请求URL
			String sHtmlText = AlipayService.create_direct_pay_by_user(arrAlipayAccount,sParaTemp);		
			
//			System.out.println("sHtmlText=======" + sHtmlText);
			
			if (StringUtils.isNotBlank(sHtmlText)){
				res.setItemUrl(sHtmlText);
				res.setOut_trade_no(out_trade_no);
				res.setExtra_common_param(toNumber); //附加参数，传手机号码供crm
			}else{
				res.setErrorCode("-100003"); // TODO
				res.setErrorMessage("支付宝充值失败");
			}
		} catch (Exception e) {
			res.setErrorCode("-100002"); // TODO
			res.setErrorMessage("支付宝充值失败");
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
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL040099", "cc_cgetusercust_69", errCode);
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
	 * check订单
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult checkOrder(final String accessId, final ServiceConfig config, final List<RequestParameter> params,
			final DEL040099Result result, final String bossTemplate) {
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
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL040099", bossTemplate, errCode);
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