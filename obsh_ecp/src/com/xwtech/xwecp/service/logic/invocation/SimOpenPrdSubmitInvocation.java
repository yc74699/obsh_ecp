package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;
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
import com.xwtech.xwecp.service.logic.pojo.DEL110004Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;


/**
 * 号卡销售临时卡预约入网-客户资料提交
 * 
 * @author YG
 *
 */
public class SimOpenPrdSubmitInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(SimOpenPrdSubmitInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public SimOpenPrdSubmitInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		DEL110004Result res = new DEL110004Result();
		try
		{
			res.setResultCode(LOGIC_ERROR);
			res.setErrorMessage("");
			
			try{
				BaseResult customDataSubmit = this.customDataSubmit(accessId, config, params);
				if (LOGIC_SUCESS.equals(customDataSubmit.getResultCode())) {
					res.setResultCode(LOGIC_SUCESS);
					res.setBookId((String)customDataSubmit.getReObj());
				} else {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(customDataSubmit.getErrorCode());
					res.setErrorMessage(customDataSubmit.getErrorMessage());
				}

			}catch(Exception e){
				
			}

			
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	
	/**
	 * 手机号码及话费校验
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private boolean checkMsisdnAndfee(String accessId, ServiceConfig config, List<RequestParameter> params, DEL110004Result res) {
		boolean result = false;
		String reqXml = "";
		String rspXml = "";
		// 查一次接口取得的actFee
		int actFee = 0;
		try {
			String phoneNum = (String) getParameters(params, "phoneNum");
			String totalFee = (String) getParameters(params, "totalFee");
			
			if(StringUtils.isNotBlank(totalFee)){
				actFee = Integer.valueOf(totalFee);
				
			}
			params.add(new RequestParameter("todaychar8", DateTimeUtil.getTodayChar8()));
			
			//查询一次号码资源，接口返回500条
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetnumressite_565", params);
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetnumressite_565", this.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				//查询号码资源接口不成功
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL110004", "cc_cgetnumressite_565", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				//成功开始校验
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath.newInstance("/operation_out/content/msisdn_id/cmsisdn_dt");
					List<Element> list = xpath.selectNodes(root);
					if(list != null){
						String msisdn = "";
						int fee = 0;
						String islock = "";
						
						for (Element element : list) {
							msisdn = element.getChildText("msisdn_id").trim();
							fee = Integer.parseInt(element.getChildText("msisdn_choice_fee").trim());
							islock = element.getChildText("msisdn_is_lock");
							if (msisdn.equals(phoneNum) && islock.equals("0")) {
								if(fee == 0){
									if(actFee >= 5000){
										result = true;
										break;
									}
								}else if(fee > 0){
									if(actFee >= fee){
										result = true;
										break;
									}
								}else{
									result = false;
									break;
								}
								
								
							}
						}
						
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return result;
	}
	
	
	/**
	 * 号卡销售-号码预占和释放（目前只要预占）
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private boolean doLock(String accessId, ServiceConfig config, List<RequestParameter> params, BaseResult res) {
		boolean result = false;
		String reqXml = "";
		String rspXml = "";
		try {
			//查询一次号码资源，接口返回100条
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_chosetelnumcontrol", params);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_chosetelnumcontrol", this.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				//查询号码资源接口不成功
				//成功开始校验
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					result = true;
				}else {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("DEL110004", "cc_chosetelnumcontrol", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return result;
	}
	
	/**
	 * 客户资料提交
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult customDataSubmit(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;

		try {
			List<RequestParameter> paramNew = copyParam(params);
			paramNew.add(new RequestParameter("todaychar8", DateTimeUtil.getTodayChar8()));

			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cTpSimopenprd", paramNew);
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cTpSimopenprd", this.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL110004", "cc_cTpSimopenprd", errCode);
					if (null != errDt)
					{
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {

					boolean resLockNumber = this.doLock(accessId,config,params,res);
					if(resLockNumber){
						Element content = root.getChild("content");
						String bookId = p.matcher(content.getChildText("webcustinfo_web_booking_id")).replaceAll("");
						res.setReObj(bookId);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	//Bean ====
	
	
}
