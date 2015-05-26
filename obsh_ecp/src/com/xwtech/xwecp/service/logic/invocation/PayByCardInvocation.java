package com.xwtech.xwecp.service.logic.invocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.communication.RemoteCallContext;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.DEL040005Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 充值卡充值
 * @author yuantao
 *
 */
public class PayByCardInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(PayByCardInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	public PayByCardInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		DEL040005Result ret = new DEL040005Result();
		String rsp = "";
		String resp_code = "";
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		String ivr_ret = "";  //错误代码 0 一切正常 1 不是移动手机号码 2 已经销户 3 不是本地号码 4 无充值权限 5 超过充值试用次数 <0 失败
		Map map = new HashMap();
		map.put("0", "一切正常");
		map.put("1", "不是移动手机号码");
		map.put("2", "已经销户");
		map.put("3", "不是本地号码");
		map.put("4", "无充值权限");
		map.put("5", "超过充值试用次数");
		map.put("00", "正常");
		map.put("20", "卡已使用");
		map.put("22", "过期卡");
		map.put("23", "未激活卡");
		map.put("24", "黑户卡");
		
		try
		{
			String flag = "1"; // 为自己充值
			String fromNum = "";
			String toNum = "";
			String cardNo = "";
			for (int i = 0; i < params.size(); i++) {
				if (params.get(i).getParameterName().equals("fromNum")) {
					fromNum = String.valueOf(params.get(i).getParameterValue());
				}
				if (params.get(i).getParameterName().equals("toNum")) {
					toNum = String.valueOf(params.get(i).getParameterValue());
				}
				if (params.get(i).getParameterName().equals("cardNo")) {
					cardNo = String.valueOf(params.get(i).getParameterValue());
				}
			}

			String route_type = (String) this.getParameters(params,
					"context_route_type");
			String route_value = (String) this.getParameters(params,
					"context_route_value");
			this.setParameter(params, "context_route_type", "2");
			this.setParameter(params, "context_route_value", toNum);
			this.setParameter(params, "phoneNum", toNum);

			String route_city = "";

			BaseResult getCityResult = this.getCity(accessId, config, params);
			if (LOGIC_SUCESS.equals(getCityResult.getResultCode())) {
				Map map1 = (Map) getCityResult.getReObj();
				route_city = (String) map1.get("city");
			} else {
				ret.setResultCode(LOGIC_ERROR);
				ret.setErrorCode(getCityResult.getErrorCode());
				ret.setErrorMessage(getCityResult.getErrorMessage());
				return ret;
			}

			this.setParameter(params, "context_route_type", "1");
			this.setParameter(params, "context_route_value", route_city);

			// IVR充值校验
			rsp = (String) this.remote.callRemote(new StringTeletext(
					this.bossTeletextUtil.mergeTeletext("ac_civrcheckuser_610",
							params), accessId, "ac_civrcheckuser_610", this
							.generateCity(params)));
			if (null != rsp && !"".equals(rsp)) {
				Element root = this.getElement(rsp.getBytes());
				if (null != root) {
					resp_code = p
							.matcher(
									root.getChild("response").getChildText(
											"resp_code")).replaceAll("");
					if ("0".equals(resp_code) || "0000".equals(resp_code)) {
						ivr_ret = p.matcher(
								root.getChild("content")
										.getChildText("ivr_ret"))
								.replaceAll("");
					}
				}

				// 非正常状态
				if ("".equals(ivr_ret) || !"0".equals(ivr_ret)) {
					ret.setResultCode("1");
					ret.setErrorCode("1000");
					String message = String.valueOf(map.get(ivr_ret));
					ret.setErrorMessage("null".equals(message) ? "IVR充值校验失败"
							: message);
					return ret;
				}

				this.setParameter(params, "context_route_type", route_type);
				this.setParameter(params, "context_route_value", route_value);

				if (cardNo.length() == 18) {
					// 充值卡充值
					rsp = (String) this.remote.callRemote(new StringTeletext(
							this.bossTeletextUtil.mergeTeletext(
									"cc_csmsnhdeposit_421", params), accessId,
							"cc_csmsnhdeposit_421", this.generateCity(params)));

					if (null != rsp && !"".equals(rsp)) {
						Element nRoot = this.getElement(rsp.getBytes());
						if (null != nRoot) {
							resp_code = nRoot.getChild("response")
									.getChildText("resp_code");
							String resp_desc = nRoot.getChild("response")
									.getChildText("resp_desc");
							ret.setResultCode("0000".equals(resp_code) ? "0"
									: "1");
							ret.setErrorCode(resp_code);
							ret.setErrorMessage(resp_desc);
						}
					} else {
						setErrorResult(ret);
					}
				} else {
					flag = fromNum.equals(toNum) ? flag : "2";

					RequestParameter par = new RequestParameter();
					par.setParameterName("ivr_state");
					par.setParameterValue(flag);
					params.add(par);

					RemoteCallContext remoteCallContext = new RemoteCallContext();
					remoteCallContext.setUserCity(route_city);
					// 充值卡充值
					rsp = (String) this.remote.callRemote(new StringTeletext(
							this.bossTeletextUtil.mergeTeletext(
									"ac_civrcardaccount_611", params),
							accessId, "ac_civrcardaccount_611",
							remoteCallContext));
					if (null != rsp && !"".equals(rsp)) {
						Element nRoot = this.getElement(rsp.getBytes());
						if (null != nRoot) {
							resp_code = nRoot.getChild("response")
									.getChildText("resp_code");
							String resp_desc = nRoot.getChild("response")
									.getChildText("resp_desc");
							ret.setResultCode("0000".equals(resp_code) ? "0"
									: "1");
							ret.setErrorCode(resp_code);
							ret.setErrorMessage(resp_desc);
							if ("0000".equals(resp_code)) {
								ret.setResultCode(p.matcher(
										nRoot.getChild("content").getChildText(
												"ivr_ret")).replaceAll("")
										.equals("0") ? "0" : "1");
								ret.setErrorCode(ret.getResultCode());
								String ivr_card_state = String
										.valueOf(p
												.matcher(
														nRoot
																.getChild(
																		"content")
																.getChildText(
																		"ivr_card_state"))
												.replaceAll(""));
								ivr_card_state = ivr_card_state.equals("") ? "null"
										: ivr_card_state;

								ret.setErrorMessage(ret.getResultCode().equals(
										"0") ? "充值卡充值成功" : ("null"
										.equals(ivr_card_state) ? "未取到或卡不存在"
										: String.valueOf(map
												.get(ivr_card_state))));
								return ret;
							}
						}
					} else {
						setErrorResult(ret);
					}
				}

			} else {
				setErrorResult(ret);
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
			setErrorResult(ret);
		}
		return ret;
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
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL040005", "cc_cgetusercust_69", errCode);
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
			else
			{
				setErrorResult(res);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
}
