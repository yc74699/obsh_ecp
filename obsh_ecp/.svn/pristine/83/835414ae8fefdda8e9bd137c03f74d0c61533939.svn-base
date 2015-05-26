package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

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
import com.xwtech.xwecp.service.logic.pojo.DEL010001Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 手机支付功能
 * 2012-09-06
 * @author chenxiaoming_1037
 * 
 */
public class PhonePaymentFunInvocation extends BaseInvocation implements ILogicalService   
{
	
	private static final Logger logger = Logger.getLogger(PhonePaymentFunInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;
	
	private static final String KT_BOSS_INTERFACE_PHONEPAYFUN = "cc_cupuserincrem_607";
	
	private static final String GB_BOSS_INTERFACE_PHONEPAYFUN = "cc_corderserv_101";
	
	private ParseXmlConfig config;

	public PhonePaymentFunInvocation() 
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config,
			List<RequestParameter> params)
	{
		DEL010001Result result = new DEL010001Result();
		result.setResultCode(LOGIC_ERROR);
		int optType = Integer.parseInt(getParameters(params,"oprType").toString());
		RequestParameter paramMonth = new RequestParameter("currentDate",DateTimeUtil.getTodayChar8());
		params.add(paramMonth);

		switch(optType)
		{
			//手机支付功能开通
			case 1:
				phonePayFun(KT_BOSS_INTERFACE_PHONEPAYFUN,params, accessId,result);
				break;
			//手机支付功能关闭
			case 2:
				phonePayFun(GB_BOSS_INTERFACE_PHONEPAYFUN,params, accessId,result);
				break;
			//操作类型不正确
			default :
				setErrResult(result);
				break;
		}
		return result;
	}
	
	private void setErrResult(DEL010001Result res) 
	{
		res.setResultCode(LOGIC_ERROR);
		res.setErrorCode("-20001");
		res.setErrorMessage("操作类型不正确，应为1或2！");
	}

	private void phonePayFun(String bossTemplate, List<RequestParameter> params,
			String accessId, DEL010001Result res)
	{
		try
		{
			String rspXml = (String) this.remote.callRemote(new StringTeletext(
					this.bossTeletextUtil.mergeTeletext(bossTemplate, params),
					accessId, bossTemplate, this.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml))
			{
				setResResult(res, rspXml);
			}
			else
			{
				setErrorResult(res);
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
			setErrorResult(res);
		}
	}


	private void setResResult(DEL010001Result res, String rspXml) {
		Element root = this.config.getElement(rspXml);
		String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
		String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
		res.setResultCode("0000".equals(resp_code)?"0":"1");
		res.setErrorCode(resp_code);
		res.setErrorMessage(resp_desc);
	}
}
