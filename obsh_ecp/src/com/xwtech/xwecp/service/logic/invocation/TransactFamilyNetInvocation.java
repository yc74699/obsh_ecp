package com.xwtech.xwecp.service.logic.invocation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.DEL011003Result;
import com.xwtech.xwecp.service.logic.pojo.DEL080002Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.FamilyShortInfo;
import com.xwtech.xwecp.service.logic.pojo.ProIncrement;
import com.xwtech.xwecp.service.logic.pojo.ProPackage;
import com.xwtech.xwecp.service.logic.pojo.ProSelf;
import com.xwtech.xwecp.service.logic.pojo.ProService;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 家庭网业务受理
 *
 */
public class TransactFamilyNetInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(TransactFamilyNetInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public TransactFamilyNetInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		DEL080002Result res = new DEL080002Result();
		try
		{
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			
			//用户在用的套餐
			List<FamilyShortInfo> familyShortInfos = null;
			
			for (RequestParameter param:params) {
				if ("familyShorts".equals(param.getParameterName())) {
					familyShortInfos = (List<FamilyShortInfo>)param.getParameterValue();
				}
			}
			
			params.add(new RequestParameter("shortNums", familyShortInfos));
			
			BaseResult transactRet = this.TransactFamilyShortNum(accessId, config, params);
			if (!LOGIC_SUCESS.equals(transactRet.getResultCode())) {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(transactRet.getErrorCode());
				res.setErrorMessage(transactRet.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			res.setResultCode(LOGIC_ERROR);
			res.setErrorCode(LOGIC_EXCEPTION);
			logger.error(e, e);
		}
		return res;
	}
	
	
	protected BaseResult TransactFamilyShortNum(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cuphomevnet_1042", params);

			logger.debug(" ====== 家庭网办理请求报文 ======\n" + reqXml);
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cuphomevnet_1042", this.generateCity(params)));
			logger.debug(" ====== 家庭网办理返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL080002", "cc_cuphomevnet_1042", errCode);
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
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
}