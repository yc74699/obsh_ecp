package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Element;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.DEL100005Result;
import com.xwtech.xwecp.service.logic.pojo.DEL100008Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY040001Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.util.StringUtil;
import com.xwtech.xwecp.util.XMLUtil;

/**
 * 宽带营销案办理-承诺使用1年宽带赠送100元话费-镇江宽带用户
 * @author xmchen
 * 
 */
public class MarketPlanBroadBandTransInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(MarketPlanBroadBandTransInvocation.class);
	
	public MarketPlanBroadBandTransInvocation()
	{
	}
	
	public BaseServiceInvocationResult executeService(String accessId, 
			                ServiceConfig config, List<RequestParameter> params)
	{
		DEL100008Result res = new DEL100008Result();
		setCommonResult(res,LOGIC_ERROR,"接口异常，办理失败");
		try
		{
			String phoneNum = (String)getParameters(params, "servnumber"); //functionType=2时传入子订单编号  functionType=1时传空
			String privid = (String)getParameters(params, "privid"); //functionType=2时传入子订单编号  functionType=1时传空
			//传入参数校验 --非空
			if(StringUtils.isBlank(phoneNum) || StringUtils.isBlank(privid)){
				setCommonResult(res,LOGIC_PARAM_ERROR,"接口参数错误不能为空");
				return res;
			}
			params.add(new RequestParameter("phoneNum",phoneNum));
//			//校验号码: 非镇江用户，不给与办理
//			if(!checkUserInfo(accessId, config, params))
//			{
//				setCommonResult(res,LOGIC_ERROR,"校验号码失败: 非镇江用户，不给与办理");
//				return res;
//			}
			// 办理营销案 承诺使用1年宽带赠送100元话费	
			doMarketPlanApply(accessId, config, params, res,"cc_marketplanapplynet_1008");
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 地市号码校验
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected boolean checkUserInfo(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		try {
			QRY040001Result qryRes= (QRY040001Result) getInvocation("QRY040001", params, accessId);
			if(null != qryRes && qryRes.getCity().equals("18"))
			{
				return true;
			}
			
		} catch (Exception e) {
			logger.error(e, e);
			return false;
		}
		return false;
	}
	
	/**
	 * 营销方案办理
	 * @param accessId
	 * @param config
	 * @param params
	 * @param string 
	 * @param res2 
	 * @return
	 */
	protected void doMarketPlanApply(final String accessId, final ServiceConfig config, final List<RequestParameter> params, DEL100008Result res, String bossTemplate) 
	{
		String reqXml = "";
		String rspXml = "";
		try {
			reqXml = mergeReqXML2Boss(params,bossTemplate);
			if (!StringUtil.isNull(reqXml))
			{
				//发送请求报文
				rspXml = sendReqXML2BOSS(accessId, params, reqXml,bossTemplate);
				if (!StringUtil.isNull(rspXml))
				{
					String resp_code = XMLUtil.getChildText(rspXml,"response","resp_code");
					String resp_desc = XMLUtil.getChildText(rspXml,"response","resp_desc");
					String recoid = XMLUtil.getChildText(rspXml,"content","recoid");
					res.setRecoid(recoid);
					setCommonResult(res, resp_code, resp_desc);
				}
				else
				{
					setCommonResult(res, "1", "响应为空");
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
			setCommonResult(res, "1", "接口异常，办理失败");
		}
	}
}
