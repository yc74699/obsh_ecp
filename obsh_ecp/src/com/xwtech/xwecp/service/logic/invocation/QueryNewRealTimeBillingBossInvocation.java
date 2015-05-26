package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.FeeDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY010018Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040001Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 根据meta文件设置的参数
 * 解析实时账单查询结果
 * @author 丁亮
 * Apr 19, 2011
 */
public class QueryNewRealTimeBillingBossInvocation extends BaseInvocation implements ILogicalService {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(QueryNewRealTimeBillingInvocation.class);
	private BossTeletextUtil bossTeletextUtil;
	private IRemote remote;
	private WellFormedDAO wellFormedDAO;
	private ParseXmlConfig config;
	
	public QueryNewRealTimeBillingBossInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY010018Result res;
		
		List<QRY010018Result> qry010018List = new ArrayList<QRY010018Result>();
		String phoneNum = (String)getParameters(params,"userMsisdn");
		String[] phoneNums = phoneNum.split(",");
		if(1 == phoneNums.length)
		{
			//设置route_type
			setParameter(params, "context_route_type", "1");
			//查询用户信息报文用到的参数
			setParameter(params, "phoneNum", phoneNums[0]);
			//获取地市
			String city = (String)getParameters(params,"context_route_value")!=null?(String)getParameters(params,"context_route_value"):this.getCity(phoneNums[0], accessId, config, params);
			//设置route_value
			setParameter(params, "context_route_value", city);
			res = querySingleHouseholdbills(params,accessId);
			
			res.setUserMobile(phoneNum);
		}
		else
		{
			int leng = phoneNums.length;
			
			
			for(int i = 0;i < leng;i++)
			{
				//查询用户信息报文用到的参数
				setParameter(params, "phoneNum", phoneNums[i]);
				setParameter(params, "userMsisdn", phoneNums[i]);
				//设置route_type
				setParameter(params, "context_route_type", "1");
				//获取地市
				String city = this.getCity(phoneNums[i], accessId, config, params);
				//设置route_value
				setParameter(params, "context_route_value", city);
				
				QRY010018Result res1 =  querySingleHouseholdbills(params,accessId);
				res1.setUserMobile(phoneNums[i]);
				res1.setIsHouse("0");
				qry010018List.add(res1);
			}
			res = new QRY010018Result();
			res.setQry010018List(qry010018List);
		}
		return res;
	}
	
	/**
	 * 设置结果信息
	 * @param res - 实体类
	 * @param resp_code - 返回代码
	 * @param xmlName - xml报文
	 */
	public void getErrInfo(QRY010018Result res, String resp_code, String xmlName) {
		ErrorMapping errDt = null; // 错误编码解析

		try {
			// 设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code) ? "0" : "1");

			// 失败
			if (!"0000".equals(resp_code)) {
				// 解析错误信息
				errDt = this.wellFormedDAO.transBossErrCode("QRY010018", xmlName, resp_code);
				if (null != errDt) {
					res.setErrorCode(errDt.getLiErrCode()); 	// 设置错误编码
					res.setErrorMessage(errDt.getLiErrMsg()); 	// 设置错误信息
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	public BossTeletextUtil getBossTeletextUtil() {
		return bossTeletextUtil;
	}

	public void setBossTeletextUtil(BossTeletextUtil bossTeletextUtil) {
		this.bossTeletextUtil = bossTeletextUtil;
	}
	
	private QRY010018Result querySingleHouseholdbills(List<RequestParameter> params,String accessId)
	{
		QRY010018Result res = new QRY010018Result();
		res.setResultCode("0"); // 成功
		String reqXml = ""; 	// 发送报文
		String rspXml = ""; 	// 接收报文
		String resp_code = ""; 	// 返回码
		try {
			// 初始化准备
			reqXml = this.bossTeletextUtil.mergeTeletext("ac_acqryrealtimebill_361", params);
			logger.debug(" ====== 查询用户在用/(Boss)账单查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_acqryrealtimebill_361", super.generateCity(params)));
				logger.debug(" ====== 查询用户在用/(Boss)账单查询 接收报文 ====== \n" + rspXml);
			}

			// 解析BOSS报文
			if (null != rspXml && !"".equals(rspXml)) {
				// 解析报文 根节点
				Element root = this.config.getElement(rspXml);
				// 获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				// 设置结果信息
				this.getErrInfo(res, resp_code, "ac_acqryrealtimebill_361");
				
				Element resRoot = this.config.getElement(root, "content");
				// 成功
				if ("0000".equals(resp_code)) {
					// 取第一层节点
					res.setMainprodId(resRoot.getChildText("mainprodid") == null ? null : Long.parseLong(resRoot.getChildText("mainprodid"))); 						// 取产品编号
					res.setMainprodName(resRoot.getChildText("mainprodname"));																						// 取产品名称
					res.setAccTid(resRoot.getChildText("acctid"));																									// 取账号
					res.setSubSid(resRoot.getChildText("subsid"));																									// 取用户号
					res.setCycle(resRoot.getChildText("cycle") == null ? null : Integer.parseInt(resRoot.getChildText("cycle")));									// 取账期
					res.setTotalFee(resRoot.getChildText("totalfee") == null ? null : Long.parseLong(resRoot.getChildText("totalfee")));							// 取总金额
					res.setOtherPay(resRoot.getChildText("otherpay") == null ? null : Long.parseLong(resRoot.getChildText("otherpay")));							// 取本月他人代付
					res.setGroupPay(resRoot.getChildText("grouppay") == null ? null : Long.parseLong(resRoot.getChildText("grouppay")));							// 本月集团代付
					res.setRentFee(resRoot.getChildText("rentfee") == null ? null : Long.parseLong(resRoot.getChildText("rentfee")));								// 取月租费
					
					// 取第二层节点
					List<Element> childElmfeeDetails = this.config.getContentList(root, "feedetail_list");
					List<FeeDetail> feeDetails = new ArrayList<FeeDetail>();
					if (null != childElmfeeDetails && childElmfeeDetails.size() > 0) {
						for (Element elm : childElmfeeDetails) {
							Element e = (Element) elm.getChild("feedetail");
							
							FeeDetail feeDetail = new FeeDetail();
							feeDetail.setLevel(e.getChildText("level") == "" ? null : Integer.parseInt(e.getChildText("level")));				// 帐单项级别
							feeDetail.setFeeTypeId(e.getChildText("feetypeid"));																// 费用代码
							feeDetail.setShowChild(e.getChildText("show_child") == "" ? 1 : Integer.parseInt(e.getChildText("show_child")));	// 下级费用代码是否显示1：表示不显示下一级,0：表示显示下一级
							feeDetail.setPrefeeTypeId(e.getChildText("prefeetypeid"));															// 父费用代码
							if(feeDetail.getFeeTypeId().equals("AB") || feeDetail.getFeeTypeId().equals("AC") || feeDetail.getFeeTypeId().equals("AD"))
							{
								feeDetail.setFeeName("套餐外" + e.getChildText("feename"));// 费用名称
							}
							else
							{
								feeDetail.setFeeName(e.getChildText("feename"));// 费用名称
							}
							feeDetail.setFee(e.getChildText("fee") == "" ? null : Long.parseLong(e.getChildText("fee")));						// 费用金额
							feeDetail.setDisc(e.getChildText("disc") == "" ? null : Long.parseLong(e.getChildText("disc")));					// 优惠金额
							feeDetail.setDspOrder(e.getChildText("dsp_order") == "" ? null : Integer.parseInt(e.getChildText("dsp_order")));	
							feeDetails.add(feeDetail);
						}
						res.setFeeDetailList(feeDetails);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
			res.setResultCode("1");
		}
		return res;
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
	protected String getCity(String phone,String accessId, ServiceConfig config, List<RequestParameter> params) {
		String city = "";
		if(null == XWECPApp.redisCli.get((phone+"_"+"QRY040001").getBytes()))
		{
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
						ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY010018", "cc_cgetusercust_69", errCode);
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
						city = ((Element) xpath.selectSingleNode(root)).getText();
						
					}
				}
			} catch (Exception e) {
				logger.error(e, e);
			}
			
		}
		else
		{
			BaseServiceInvocationResult res = null;
			res = (BaseServiceInvocationResult)XWECPApp.redisCli.get((phone+"_"+"QRY040001").getBytes());
			QRY040001Result ret = (QRY040001Result)res;
			city = ret.getCity();
		}
		return city;
		
	}
	
	
	

	
}
