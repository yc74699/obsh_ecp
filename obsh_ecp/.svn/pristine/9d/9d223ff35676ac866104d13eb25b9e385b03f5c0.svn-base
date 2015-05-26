package com.xwtech.xwecp.service.logic.invocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.xwtech.xwecp.service.logic.pojo.DEL010013Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class SMS2TimesInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(SMS2TimesInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;

	//普通短信二次确认
	private final String PT2TIMESSURE_TEMPLATE = "cc_cupdmiscwait_76";
	
	//彩铃铃音盒二次确认
	private final String CL2TIMESSURE_TEMPLATE = "cc_recfirmrbox_1006";
	
	//集团二次确认
	private final String GROUP2TIMESSURE_TEMPLATE = "cc_cupdgroupwait_1006";
	
	//数据实体卡二次确认
	private final String DATACARD2TIMESSURE_TEMPLATE = "cc_datacardconfirm_1006";
	
	//营销方案到期短信回复
	private final String MARKET2TIMESSURE_TEMPLATE = "cc_cdealmmssmsrp_1006";
	
	//新业务体验卡短信受理和到期确认
	private final String TYYW2TIMESSURE_TEMPLATE = "cc_csmsrevdeal_1006";

	//省统谈/地市自谈积分闭环
	private final String ZTJFBH2TIMESSURE_TEMPLATE = "cc_cdealsmsreply_1006";

	private Map<String,String> oprTypeTo2Times =  null;

	public SMS2TimesInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
		if(oprTypeTo2Times == null)
		{
			oprTypeTo2Times = new HashMap<String, String>();
			oprTypeTo2Times.put("1", CL2TIMESSURE_TEMPLATE);
			oprTypeTo2Times.put("2", PT2TIMESSURE_TEMPLATE);
			oprTypeTo2Times.put("3", GROUP2TIMESSURE_TEMPLATE);
			oprTypeTo2Times.put("4", DATACARD2TIMESSURE_TEMPLATE);
			oprTypeTo2Times.put("5", MARKET2TIMESSURE_TEMPLATE);
			oprTypeTo2Times.put("6", TYYW2TIMESSURE_TEMPLATE);
			oprTypeTo2Times.put("7", ZTJFBH2TIMESSURE_TEMPLATE);


		}
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		DEL010013Result res = new DEL010013Result();
		res.setResultCode("1");
		res.setErrorMessage("");
		String reqXml = "";
		String rspXml = "";
		String optType = getParameters(params,"oprType").toString();
		String template = getTemplate(optType);
		try
		{
			reqXml = this.bossTeletextUtil.mergeTeletext(template,
					params);
			if (null != reqXml && !"".equals(reqXml)) 
			{
				rspXml = (String) this.remote.callRemote(new StringTeletext(
						reqXml, accessId, template, super
								.generateCity(params)));
			}
			if (null != rspXml && !"".equals(rspXml)) 
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config
						.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					res.setErrorCode(resp_code);
					res.setErrorMessage(this.config.getChildText(this.config
							.getElement(root, "response"), "resp_desc"));
				}
			}
		}
		catch (Exception e) {
			logger.error(e, e);
		}
		
		return res;
	}

	private String getTemplate(String optType)
	{
		if(null == oprTypeTo2Times.get(optType))
		{
			return oprTypeTo2Times.get("2");
		}
		return oprTypeTo2Times.get(optType);
	}
}
