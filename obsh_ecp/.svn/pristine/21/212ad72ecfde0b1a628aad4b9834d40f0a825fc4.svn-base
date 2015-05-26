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
import com.xwtech.xwecp.service.logic.pojo.DEL040116Result;
import com.xwtech.xwecp.service.logic.pojo.DEL040116Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;
/**
 * 现金购买流量
 * @author YangXQ
 * 2015-05-07
 */
public class CashpaymentflowInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(CashpaymentflowInvocation.class);
	
  
	//数字签名文件路径
	//private static String signFilePath = ConfigurationRead.getInstance().getValue("signFilePath");
	
	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private Map<String, String> dicAccount = null;
	private Map<String, String> dicPid = null;
	private Map<String, String> dicKey = null;

	public CashpaymentflowInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		
        Map<String,Map<String,String>> aliPayRechargeInfo = wellFormedDAO.getAliPayRechargeInfo();
		
		if(null != aliPayRechargeInfo)
		{
			dicAccount = aliPayRechargeInfo.get("dicAccount");  //各地市支付宝账号
//			dicPid = aliPayRechargeInfo.get("dicPid");          //各地市支付宝PID
//			dicKey = aliPayRechargeInfo.get("dicKey");          //各地市支付宝KEY
		}
		
//		//各个地市支付宝账号
//		dicAccount = new HashMap<String, String>();
//		dicAccount.put("11", "cmccjssz@163.com");
//		dicAccount.put("12", "hayd_2011@sina.com");
//		dicAccount.put("13", "dongcn@sq.js.chinamobile.com");
//		dicAccount.put("14", "nj12580lm@nj.js.chinamobile.com");
//		dicAccount.put("15", "zhangwzx@lyg.js.chinamobile.com");
//		dicAccount.put("16", "jsxzcmcc2011@sina.com");
//		dicAccount.put("17", "cmcc_cz2011@sina.com");
//		dicAccount.put("18", "1217756907@qq.com");
//		dicAccount.put("19", "WX12580LM@wx.js.chinamobile.com");
//		dicAccount.put("20", "434494420@139.com");
//		dicAccount.put("21", "tzydcmcc@yahoo.cn");
//		dicAccount.put("22", "yc_scbywzczx@139.com");
//		dicAccount.put("23", "one_barcode@139.com");
//		
		//各个地市pid
		dicPid = new HashMap<String, String>();
		dicPid.put("11", "2088801471029460");
		dicPid.put("12", "2088801471029460");
		dicPid.put("13", "2088801471029460");
		dicPid.put("14", "2088801471029460");
		dicPid.put("15", "2088801471029460");
		dicPid.put("16", "2088801471029460");
		dicPid.put("17", "2088801471029460");
		dicPid.put("18", "2088801471029460");
		dicPid.put("19", "2088801471029460");
		dicPid.put("20", "2088801471029460");
		dicPid.put("21", "2088801471029460");
		dicPid.put("22", "2088801471029460");
		dicPid.put("23", "2088801471029460");
		
		
		//各个地市key
		dicKey = new HashMap<String, String>();
		dicKey.put("11", "fspffx31wp4nshw96ris9cwh9sauydqd");
		dicKey.put("12", "fspffx31wp4nshw96ris9cwh9sauydqd");
		dicKey.put("13", "fspffx31wp4nshw96ris9cwh9sauydqd");
		dicKey.put("14", "fspffx31wp4nshw96ris9cwh9sauydqd");
		dicKey.put("15", "fspffx31wp4nshw96ris9cwh9sauydqd");
		dicKey.put("16", "fspffx31wp4nshw96ris9cwh9sauydqd");
		dicKey.put("17", "fspffx31wp4nshw96ris9cwh9sauydqd");
		dicKey.put("18", "fspffx31wp4nshw96ris9cwh9sauydqd");
		dicKey.put("19", "fspffx31wp4nshw96ris9cwh9sauydqd");
		dicKey.put("20", "fspffx31wp4nshw96ris9cwh9sauydqd");
		dicKey.put("21", "fspffx31wp4nshw96ris9cwh9sauydqd");
		dicKey.put("22", "fspffx31wp4nshw96ris9cwh9sauydqd");
		dicKey.put("23", "fspffx31wp4nshw96ris9cwh9sauydqd");

	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		DEL040116Result res = new DEL040116Result();
		String userCity = null;
		String userId = null;

		try {
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			userCity = (String)this.getParameters(params, "context_ddr_city");
			userId = (String)this.getParameters(params, "context_user_id");
			String toNumber = (String) getParameters(params, "phoneNum");
			//充值类型:0 – 现金 1 –余额 2 – 赠送
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
			this.setParameter(params, "qryMonth", DateTimeUtil.getTodayChar6());
			
			String paymethod = (String)getParameters(params, "paymethod");
			String bankCode = (String)getParameters(params, "bankcode"); //直充需要银行编码，账户充值空

			/**入参*/
			params.add(new RequestParameter("ddr_city", userCity));
			params.add(new RequestParameter("servnumber", toNumber));//号码
			params.add(new RequestParameter("paytype", flag));//支付方式 : 0 – 现金 1 –余额 2 – 赠送
			params.add(new RequestParameter("payfee", (String)this.getParameters(params, "amount")));//金额
			
			/**现金购买流量接口*/
			String bossTemplate = "cc_cashpaymentflow_363";
		/** String bossTemplate = "cc_bugetcltsrl_357"; 获取新客户端流水号*/
			
			// 生成订单
			BaseResult generateResult = this.generateOrder(accessId, config, params, res, bossTemplate);
			if (LOGIC_SUCESS.equals(generateResult.getResultCode())) {
//				String bossDate = res.getBossDate();
				
				/** Taskoid 充值订单号*/
				String unionPaySrl = res.getTaskoid();
			 /**String unionPaySrl = res.getOut_trade_no(); //Out_trade_no商户网站订单*/
				
				//TODO 假数据
				//String amount = "";
				String amount = (String) getParameters(params, "amount");// 订单金额，单位元,-金额已经经过校验，必须是30、50、100等
				//校验订单接口，
				String checkSrl = unionPaySrl; 
				
				// 设置校验参数
				params.add(new RequestParameter("checkSrl", checkSrl));
//				params.add(new RequestParameter("bossDate", bossDate));
				//TODO 假金额
				//params.add(new RequestParameter("amount", 10));
				params.add(new RequestParameter("amount", Integer.parseInt(amount) * 100));
//				BaseResult checkOrderResult = new BaseResult();
				

				this.setParameter(params, "source", "");

				setTranValue(res, userCity, unionPaySrl, amount,paymethod,bankCode,toNumber);

			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(generateResult.getErrorCode());
				res.setErrorMessage(generateResult.getErrorMessage());
			}
//			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}


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
	protected void setTranValue(DEL040116Result res, String userCity, String unionPaySrl, 
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

			res.setKey(arrAlipayAccount[2]);          //key
			res.setPartner(arrAlipayAccount[1]);      //pid
			res.setSeller_email(arrAlipayAccount[0]); //账号
			
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
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL040116", "cc_cgetusercust_69", errCode);
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
	 * 创建支付宝订单
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult generateOrder(final String accessId, final ServiceConfig config, final List<RequestParameter> params,
			final DEL040116Result result, final String bossTemplate) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		String unionPaySrl = "0";
//		String bossDate = "";

		try {
			//bank_type 传15表示支付宝，其他充值暂时不变
			params.add(new RequestParameter("bankType", "15"));
			reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate, params);

			logger.debug(" ====== 创建订单请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, bossTemplate, super.generateCity(params)));
			logger.debug(" ====== 创建订单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL040116", bossTemplate, errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
//					XPath xpath = XPath.newInstance("/operation_out/content/buoperatingdetail_clt_sys_date");
//					bossDate = ((Element) xpath.selectSingleNode(root)).getText();
					XPath xpath = XPath.newInstance("/operation_out/content/taskoid");
					unionPaySrl = ((Element) xpath.selectSingleNode(root)).getText();
//					result.setBossDate(bossDate);
//					result.setOut_trade_no(unionPaySrl);
					result.setTaskoid(unionPaySrl);

				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
}