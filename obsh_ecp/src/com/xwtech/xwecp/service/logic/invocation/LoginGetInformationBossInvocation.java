package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;

import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.CallFeeAccount;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.FeeDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY010028Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 根据网厅的需求合并QRY010018的账单查询和QRY010003的余额查询
 * 
 * @author 陶刚
 * August 30, 2013
 */
public class LoginGetInformationBossInvocation extends BaseInvocation implements ILogicalService {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(LoginGetInformationInvocation.class);
	private ParseXmlConfig config;
	
	public LoginGetInformationBossInvocation() {
		this.config = new ParseXmlConfig();
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY010028Result res = new QRY010028Result();
		res.setResultCode("0"); // 成功
		String reqXml = ""; 	// 发送报文
		String rspXml = ""; 	// 接收报文
		String resp_code = ""; 	// 返回码
		
		StringBuffer str  = new StringBuffer("");
		res.setCallFeeAccount(this.queryFeeAcc(accessId, config, params, str, res));
		
		try {
			
			// 初始化准备
			reqXml = this.bossTeletextUtil.mergeTeletext("ac_acqryrealtimebill_361", params);
			logger.debug(" ====== 查询用户在用/(新)账单查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_acqryrealtimebill_361", super.generateCity(params)));
				logger.debug(" ====== 查询用户在用/(新)账单查询 接收报文 ====== \n" + rspXml);
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
	 * 设置结果信息
	 * @param res - 实体类
	 * @param resp_code - 返回代码
	 * @param xmlName - xml报文
	 */
	public void getErrInfo(QRY010028Result res, String resp_code, String xmlName) {
		ErrorMapping errDt = null; // 错误编码解析

		try {
			// 设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code) ? "0" : "1");

			// 失败
			if (!"0000".equals(resp_code)) {
				// 解析错误信息
				errDt = this.wellFormedDAO.transBossErrCode("QRY010028", xmlName, resp_code);
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
	
	public CallFeeAccount queryFeeAcc (String accessId, ServiceConfig config, 
			List<RequestParameter> params, StringBuffer str, QRY010028Result res)
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		CallFeeAccount dt = null;
		ErrorMapping errDt = null;
		
		try
		{
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetusercustaccbalance_361", params);
			logger.debug(" ====== 查询话费帐户 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_cgetusercustaccbalance_361", this.generateCity(params)));
				logger.debug(" ====== 查询话费帐户 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010003", "cc_cgetusercustaccbalance_361", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code))
			{
				dt = new CallFeeAccount();
				//余额
				dt.setBalance(String.valueOf(Double.parseDouble(
				   this.config.getChildText(this.config.getElement(root, "content"), "balance"))));
				dt.setValiDate("");
				String strDate = this.config.getChildText(this.config.getElement(root, "content"), "account_expire_date");
				if (null != strDate && !"".equals(strDate))
				{
					dt.setValiDate(strDate.substring(0, 8));
				}
				//新业务账户
				str.append(String.valueOf(Double.parseDouble(
					this.config.getChildText(this.config.getElement(root, "content"), "new_balance"))));
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return dt;
	}
}
