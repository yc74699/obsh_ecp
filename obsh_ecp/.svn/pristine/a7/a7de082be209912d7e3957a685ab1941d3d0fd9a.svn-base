package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.IPackageChangeDAO;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL100006Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 营销案回退，批量回退，中断
 * 
 * @author 汪洪广
 * 
 */
public class MarketBatchStopInvocation extends BaseInvocation implements
		ILogicalService {
	private static final Logger logger = Logger
			.getLogger(MarketBatchStopInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");

	private IPackageChangeDAO packageChangeDAO;
	
	private ParseXmlConfig config;

	public MarketBatchStopInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx
				.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx
				.getBean("wellFormedDAO"));
		this.packageChangeDAO = (IPackageChangeDAO) (springCtx
				.getBean("packageChangeDAO"));
		this.config = new ParseXmlConfig();

	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		DEL100006Result res = new DEL100006Result();

		String reqXml = "";
		String rspXml = "";
		int oprType = 0;

		for (RequestParameter parameter : params) {
			String pName = parameter.getParameterName();
			if (pName.equals("functionType")) {
				if (parameter.getParameterValue().toString().equals("1")) {
					oprType = 1;
				} else if (parameter.getParameterValue().toString().equals("2")) {
					oprType = 2;
				} else if (parameter.getParameterValue().toString().equals("3")) {
					oprType = 3;
				}
			}
		}
		String bossTemplate_01 = "cc_marketcancel";// 回退
		String bossTemplate_02 = "cc_marketbatchcancel";// 批量回退
		String bossTemplate_03 = "cc_marketstop";// 中断
		try {

			// 回退
			if (oprType == 1) {
				reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate_01,
						params);
				logger.info(reqXml);
				rspXml = (String) this.remote.callRemote(new StringTeletext(
						reqXml, accessId, bossTemplate_01, this
								.generateCity(params)));
				logger.info(rspXml);

			} else if (oprType == 2)// 批量回退
			{
				reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate_02,
						params);
				logger.info(reqXml);
				rspXml = (String) this.remote.callRemote(new StringTeletext(
						reqXml, accessId, bossTemplate_02, this
								.generateCity(params)));
				logger.info(rspXml);
			} else if (oprType == 3)// 中断
			{
				reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate_03,
						params);
				logger.info(reqXml);
				rspXml = (String) this.remote.callRemote(new StringTeletext(
						reqXml, accessId, bossTemplate_03, this
								.generateCity(params)));
				logger.info(rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText(
						"resp_code");
				res.setResultCode(BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS
						: LOGIC_ERROR);
				if(!"0000".equals(resp_code)){
					res.setErrorCode(resp_code);
					res.setErrorMessage(this.config.getChildText(
							this.config.getElement(root, "response"),
							"resp_desc"));
				}else
				{
					if(oprType==1 || oprType==2){
						res.setTaskId(this.config.getChildText(this.config
								.getElement(root, "content"), "Task_id"));
					}
				}
				
				
			}

		} catch (Exception e) {
			res.setResultCode("1");
			e.printStackTrace();
		}
		return res;

	}

}