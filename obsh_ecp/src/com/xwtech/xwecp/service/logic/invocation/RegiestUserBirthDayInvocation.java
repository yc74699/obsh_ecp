package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.IPackageChangeDAO;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL030008Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;

import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 生日套餐
 * 
 * @author 汪洪广
 * 
 */
public class RegiestUserBirthDayInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(RegiestUserBirthDayInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");

	private IPackageChangeDAO packageChangeDAO;

	public RegiestUserBirthDayInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.packageChangeDAO = (IPackageChangeDAO) (springCtx.getBean("packageChangeDAO"));

	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) 
	{
		DEL030008Result res = new DEL030008Result();
		ErrorMapping errDt = null;
		
		String bossTemplate_01 = "cc_check_birth";//校验是否可以录入
		String bossTemplate_02 = "cc_rec_birth";//受理生日
		
		String reqXml = "";
		String rspXml = "";
		Boolean  flag =false;
		
		String regeistValue= "";
		try{

			reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate_01, params);
			logger.info(reqXml);
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, 
					bossTemplate_01, this.generateCity(params)));
			
			if(null !=rspXml && !"".equals(rspXml)){
				Element root01 = this.getElement(rspXml.getBytes());

				regeistValue = root01.getChild("content").getChildText("canregist").trim();
				if(!regeistValue.equals("1")){
				res.setResultCode("-1");
				res.setErrorCode("-1");
				res.setErrorMessage("校验失败");
				}
				else 
				{
					reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate_02, params);
					logger.info(reqXml);
					rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, 
							bossTemplate_01, this.generateCity(params)));
					if(null !=rspXml && !"".equals(rspXml)){
						Element root02 = this.getElement(rspXml.getBytes());
						String errCode = root02.getChild("response").getChildText("resp_code");
						String errDesc = root02.getChild("response").getChildText("resp_desc");
						if(!BOSS_SUCCESS.equals(errCode)){
							errDt = this.wellFormedDAO.transBossErrCode("DEL030001", bossTemplate_02, errCode);
							if (null != errDt)
							{
								errCode = errDt.getLiErrCode();
								errDesc = errDt.getLiErrMsg();
							}
						}
							res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
							res.setErrorCode(errCode);
							res.setErrorMessage(errDesc);
					}
				}
				}
		}
		catch(Exception e){
			logger.error(e,e);
		}
		return  res;

	}



	



	

	

}