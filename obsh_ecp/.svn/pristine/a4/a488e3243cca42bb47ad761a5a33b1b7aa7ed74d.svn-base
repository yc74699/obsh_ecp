package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.odc.ODCEncoding;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.OdcDao;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL070001Result;
import com.xwtech.xwecp.service.logic.pojo.UserOdcInfo;

/**
 * 一维码兑换
 * 
 * @author 吴宗德
 * 
 */
public class OdcExchangeInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(OdcExchangeInvocation.class);

	private OdcDao odcDao;

	public OdcExchangeInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.odcDao = (OdcDao) (springCtx.getBean("odcDao"));

	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		DEL070001Result res = new DEL070001Result();
		res.setResultCode(LOGIC_ERROR);
		res.setErrorMessage("");
		try {
			String mobile = (String)this.getParameters(params, "mobile");
			String odcNum = (String)this.getParameters(params, "odcNum");
			String merAwardNum = (String)this.getParameters(params, "merAwardNum");
			String merAccountNum = (String)this.getParameters(params, "merAccountNum");
			String merAccountPwd = (String)this.getParameters(params, "merAccountPwd");
			String fromIP = (String)this.getParameters(params, "fromIP");
			
			if (StringUtils.isBlank(mobile) || StringUtils.isBlank(odcNum) 
					|| StringUtils.isBlank(merAccountNum) || StringUtils.isBlank(merAccountPwd) 
					|| StringUtils.isBlank(fromIP)) {
				res.setErrorCode(LOGIC_PARAM_ERROR);
				res.setErrorMessage("参数错误！");
				return res;
			}
			 
			if (odcDao.checkMerAccount(merAccountNum, merAccountPwd)) {
				
				if (odcDao.queryOdcLockInfo(odcNum) < 1) {
					UserOdcInfo odcInfo = odcDao.queryUserOdcInfoByOdcNum(odcNum);
					if (odcInfo != null) {
						//判断是否已兑换
						if (odcInfo.getIsExchange() != 1) {
							if (odcDao.queryActEndTime(odcInfo.getActNum()) < 1) {
								res.setErrorCode("-60014");
								res.setErrorMessage("已超过兑换截止日期！");
								return res;
							}
							//校验一维码在该商户下是否可兑换
							if (odcDao.checkOdcNum(odcNum, merAccountNum) >= 1) {
								if (odcNum.length() < 14) {
									res.setErrorCode("-60007");
									res.setErrorMessage("加密一维码长度不正确！");
									return res;
								}
								
								String keyArray0 = odcNum.substring(0, 10);
								String seq = odcNum.substring(10, 14);
								
								//活动解密公钥
								String key = odcInfo.getActPublicKey();
								
								ODCEncoding odcEncoding = new ODCEncoding();
								String str = odcEncoding.decrypt(key, keyArray0);
								
								if (str.length() < 20) {
									res.setErrorCode("-60008");
									res.setErrorMessage("解密一维码长度不正确！");
									return res;
								}
								
								String mobileStr = str.substring(0, 11);
								String actStr = str.substring(11, 19);
								String levelStr = str.substring(19, 20);

								String awardNum = odcInfo.getAwardNum();
								int awardLevel = odcDao.getAwardLevel(awardNum);
								String awardLevelStr = String.valueOf(awardLevel);

								if (!mobileStr.equals(odcInfo.getMobile()) || 
										!actStr.equals(odcInfo.getActNum()) || !levelStr.equals(awardLevelStr)) {
									res.setErrorCode("-60009");
									res.setErrorMessage("一维码校验失败！");
									return res;
								}
								
								//查找一维码后四位匹配情况
								long seq10 = odcEncoding.Compress36HexToDHex(seq);
								if (odcDao.queryOdcNum(seq) >= 2 || seq10 > odcDao.getCurrOdcSeq()) {
									res.setErrorCode("-60010");
									res.setErrorMessage("一维码后缀查询异常！");
									return res;
								}
								
								if (StringUtils.isNotBlank(merAwardNum)) {
									//校验商品信息
									if (odcDao.checkMerAwardNum(merAwardNum, merAccountNum) < 1) {
										res.setErrorCode("-60006");
										res.setErrorMessage("查询奖品信息失败！");
										return res;
									}
								}
								merAwardNum = StringUtils.isBlank(merAwardNum) ? "" : merAwardNum;
								
								int confirmSeq = odcDao.addOdcExchangeDetail(mobile, odcInfo.getArea(), 
										odcNum, odcInfo.getActNum(), odcInfo.getAwardNum(), merAccountNum, fromIP, merAwardNum, 0);
								if (confirmSeq > 0) {
									res.setResultCode(LOGIC_SUCESS);
									res.setConfirmSeq(String.valueOf(confirmSeq));
									odcDao.lockOdcNum(odcNum, merAccountNum);
								}
							} else {
								//一维码在该商户下不可兑换
								res.setErrorCode("-60005");
								res.setErrorMessage("查询一维码信息失败！");
							}
						} else {
							res.setErrorCode("-60004");
							res.setErrorMessage("已兑换！");
						}
					} else {
						res.setErrorCode("-60003");
						res.setErrorMessage("一维码不存在！");
					}
				} else {
					res.setErrorCode("-60002");
					res.setErrorMessage("一维码已锁定！");
				}
			} else {
				res.setErrorCode("-60001");
				res.setErrorMessage("账号、密码校验失败！");
			}

		} catch (Exception e) {
			logger.error(e, e);
			res.setResultCode(LOGIC_ERROR);
			res.setErrorCode(LOGIC_EXCEPTION);
		}
		return res;
	}
	
	public static void main (String [] args) throws Exception {
		String odcNum = "G0RWHYFATA004CKKK";
		String keyArray0 = odcNum.substring(0, 10);
		String seq = odcNum.substring(10, 14);
		System.out.println(odcNum);
		System.out.println(keyArray0);
		System.out.println(seq);
		
		ODCEncoding odcEncoding = new ODCEncoding();
		String str = odcEncoding.decrypt("32f52fc2357a0a8f61dfcbb626aa0fe31c0ce74b284fdd39e0668fe5819cb2d4", keyArray0);
		String mobileStr = str.substring(0, 11);
		String actStr = str.substring(11, 19);
		String levelStr = str.substring(19, 20);
		System.out.println(str);
		
		System.out.println(mobileStr + actStr + levelStr);
		
		String [] strArr = odcEncoding.encrypt("21232F297A57A5A743894A0E4A801FC3", "15951824319", "00000010", "2");
		System.out.println(strArr[0]);
		System.out.println(strArr[1]);
	}

}