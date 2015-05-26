package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.odc.ODCEncoding;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.OdcDao;
import com.xwtech.xwecp.dao.SmsDao;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL070003Result;

/**
 * 一维码生成
 * 
 * @author 吴宗德
 * 
 */
public class OdcBuildInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(OdcBuildInvocation.class);

	private OdcDao odcDao;
	
	private SmsDao smsDao;

	public OdcBuildInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.odcDao = (OdcDao) (springCtx.getBean("odcDao"));
		this.smsDao = (SmsDao)(springCtx.getBean("smsDao"));

	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		DEL070003Result res = new DEL070003Result();
		res.setResultCode(LOGIC_ERROR);
		res.setErrorMessage("");
		try {
			String mobile = (String)this.getParameters(params, "mobile");
			String areaNum = (String)this.getParameters(params, "areaNum");
			String actNum = (String)this.getParameters(params, "actNum");
			String awardNum = (String)this.getParameters(params, "awardNum");
			String mrcNum = (String)this.getParameters(params, "mrcNum");
			
			if (StringUtils.isBlank(mobile) || StringUtils.isBlank(areaNum) 
					|| StringUtils.isBlank(actNum) || StringUtils.isBlank(awardNum)
					|| StringUtils.isBlank(mrcNum)) {
				res.setErrorCode(LOGIC_PARAM_ERROR);
				res.setErrorMessage("参数错误！");
				return res;
			}
			
			//校验活动试用地区信息
			int sum = odcDao.checkOdcArea(actNum, areaNum);
			if (sum < 1) {
				res.setErrorCode("-600015");
				res.setErrorMessage("活动试用地区不匹配！");
				return res;
			}
			
			//校验活动、奖项、商户帐号信息配置
			Map map = odcDao.checkOdcInfo(actNum, awardNum, mrcNum);
			
			if (map == null) {
				res.setErrorCode("-600016");
				res.setErrorMessage("活动、奖项、商户信息不匹配！");
				return res;
			}
			
			String actName = (String)map.get("F_ACT_NAME");
			 
			//判断一维码是否已经分配
//			sum = odcDao.queryOdcAwardCodeDisp(mobile, actNum, awardNum);
//			
//			if (sum < 1) {
				
				//更新奖项档案表
				int num = odcDao.updateOdcAwardDa(actNum, awardNum);
				
				if (num < 1) {
					res.setErrorCode("-600013");
					res.setErrorMessage("该奖项已没有可分配的名额！");
					return res;
				}
				
				//1、获取活动加密私钥
				String actPrivateKey = odcDao.getActPrivateKey(actNum);
				int awardLevel = odcDao.getAwardLevel(awardNum);
				long seq = odcDao.getNextOdcSeq();
				
				ODCEncoding odcEncoding = new ODCEncoding();
				String[] keyArray = odcEncoding.encrypt(actPrivateKey, mobile, actNum, String.valueOf(awardLevel));
				
				String odcNum = keyArray[0] + org4Seq(odcEncoding.CompressNumberTo36(seq));
				//解密公钥
				String encodeKey = keyArray[1];
				//添加一维码奖品分配信息
				num = odcDao.addOdcAwardCodeDisp(mobile, areaNum, actNum, awardNum, odcNum, encodeKey);
				
				if (num >= 1) {
					//添加用户一维码可兑换商户列表信息
					num = odcDao.addOdcCodeMerList(odcNum, mrcNum);
					//给用户下发一维码
					num = smsDao.sendSms(mobile, buildOdcNumMsg(odcNum, actName), "10086", "FREE");
				}
				
				res.setResultCode(LOGIC_SUCESS);
//			} else {
//				res.setErrorCode("-600012");
//				res.setErrorMessage("一维码已分配！");
//			}

		} catch (Exception e) {
			logger.error(e, e);
			res.setResultCode(LOGIC_ERROR);
			res.setErrorCode(LOGIC_EXCEPTION);
		}
		return res;
	}
	
	private static String org4Seq(String seq) {
		if (StringUtils.isNotBlank(seq)) {
			if (seq.length() > 4) {
				return seq.substring(seq.length() - 4, seq.length());
			}
			while (seq.length() < 4) {
				seq = "0" + seq;
			}
			return seq;
		}
		return null;
	}
	
	private String buildOdcNumMsg(String odcNum, String actName) {
		String str = "尊敬的江苏移动客户:您好！您参加的【"+actName+"】的一维码兑奖凭证为：" +
		odcNum + "，请妥善保管，不要告诉他人！";
		return str;
	}
	
}