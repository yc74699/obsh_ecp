package com.xwtech.xwecp.service.logic.invocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.DEL030001Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 用户停复机
 * 
 * @author 吴宗德
 *
 */
public class SuppendResumeInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(SuppendResumeInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;

	private static String OPRT_ERROR = "-20079"; // 停机状态再停机，正常状态再复机的操作错误编码
	
	private static String STATE_ERROR = "-20080"; //状态非1和2的停复机操作的错误编码
	public SuppendResumeInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		DEL030001Result res = new DEL030001Result();
		String state = "";
		try
		{
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			//根据手机号码查询用户状态
			BaseResult userInfoResult = getUserState(accessId, config, params);
			if (LOGIC_SUCESS.equals(userInfoResult.getResultCode())) {
				Map userInfoMap = (Map)userInfoResult.getReObj();
				
				state = (String)userInfoMap.get("state");
			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(userInfoResult.getErrorCode());
				res.setErrorMessage(userInfoResult.getErrorMessage());
				return res;
			}

			
			this.setParameter(params, "userState", state);
			String bossTemplate = "cc_suspend_resume_serv_306";

			//用户停复机
			BaseResult oprtResult = doSuspendOrResume(accessId, config, params, bossTemplate);

			
			if (!LOGIC_SUCESS.equals(oprtResult.getResultCode())) {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(oprtResult.getErrorCode());
				res.setErrorMessage(oprtResult.getErrorMessage());
			}
			
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	
	/**
	 * 停复机操作
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult doSuspendOrResume(final String accessId, final ServiceConfig config, final List<RequestParameter> params, final String bossTemplate) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		String state = (String)getParameters(params, "userState");
		String oprtType =  String.valueOf(getParameters(params, "oprType"));
		try {
			if(!StringUtils.isBlank(oprtType) && !StringUtils.isBlank(state)){
				if("1".equals(state) && !"0".equals(oprtType)){ //用户 状态正常 -1 。只能做停机操作
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(OPRT_ERROR);
					res.setErrorMessage("当前状态不能进行复机操作");
				}else if ("2".equals(state) && !"1".equals(oprtType)){ //用户 状态停机保号-2 。只能做复机机操作
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(OPRT_ERROR);
					res.setErrorMessage("当前状态不能进行停机操作");
				}else if(!"1".equals(state) && !"2".equals(state) ){
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(STATE_ERROR);
					res.setErrorMessage("对不起，只有停机保号和正常状态的号码才能进行停复机操作");
					
				}else{
					reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate, params);

					rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, bossTemplate, this.generateCity(params)));
					if (null != rspXml && !"".equals(rspXml))
					{
						Element root = this.getElement(rspXml.getBytes());
						String errCode = root.getChild("response").getChildText("resp_code");
						String errDesc = root.getChild("response").getChildText("resp_desc");
						if (!BOSS_SUCCESS.equals(errCode))
						{
							errDt = this.wellFormedDAO.transBossErrCode("DEL030001", bossTemplate, errCode);
							if (null != errDt)
							{
								errCode = errDt.getLiErrCode();
								errDesc = errDt.getLiErrMsg();
							}
						}
					
						res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
						res.setErrorCode(errCode);
						res.setErrorMessage(errDesc);
					}
				}
				
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 查询用户信息-状态
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getUserState(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
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
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL030001", "cc_cgetusercust_69", errCode);
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
					
					XPath xpath = XPath.newInstance("/operation_out/content/user_info/user_state");
					String state = ((Element) xpath.selectSingleNode(root)).getText();
					retMap.put("state", state);
					res.setReObj(retMap);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

}