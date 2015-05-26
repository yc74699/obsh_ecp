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
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;
import com.xwtech.xwecp.service.logic.pojo.DEL010001Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 附加功能定制提交
 * @author yuantao
 * 
 */
public class DealMonternetInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(DealMonternetInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	public DealMonternetInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId, 
			                ServiceConfig config, List<RequestParameter> params)
	{
		DEL010001Result re = new DEL010001Result();
		List<BossParmDT> parList = null;
		String reqXml = "";
		String rspXml = "";
//		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		String oprType = "";
		String id = "";
		boolean bizFlag = true;
		boolean flag = false;
		ErrorMapping errDt = null;
				
		try
		{
			//根据业务编码获取其子业务所对应的BOSS实现
			for (RequestParameter r : params)
			{
				if ("id".equals(r.getParameterName()))
				{
					id = String.valueOf(r.getParameterValue());
					parList = this.wellFormedDAO.getSubBossParmList(id);
				}
				if ("oprType".equals(r.getParameterName()))
				{
					oprType = String.valueOf(r.getParameterValue());
				}
			}
			
//			boolean SJB = id.length() >= 3 && "SJB".equals(id.substring(0, 3));  //手机报
//			boolean TYXXS = id.length() >= 5 && "TYXXS".equals(id.substring(0, 5));  //体育小信使
			boolean FXYW = id.length() >= 4 && "FXYW".equals(id.substring(0, 4));  //飞信业务
			boolean QXBY = id.length() >= 9 && ( "12580QXBY".equals(id.substring(0, 9)) || "12580XQBY".equals(id.substring(0, 9)));  //12580前向包月业务
			boolean YYBK = id.length() >= 4 && ( "YYBK".equals(id.substring(0, 4)) || "YYBK".equals(id.substring(0, 4)));  //营养百科
			boolean SJDS = id.length() >= 4 && "SJDS".equals(id.subSequence(0, 4));//手机电视
			boolean SJYD = id.length() >= 4 && "SJYD".equals(id.subSequence(0, 4));//手机阅读
//			boolean SJYL = id.length() >= 4 && ( "SJYL".equals(id.substring(0, 4)) || "SJYL".equals(id.substring(0, 4)));  //手机医疗
			
			if (FXYW || QXBY || YYBK || SJDS || SJYD)
			{
				flag = true;
			}
			
			if ("1".equals(oprType) && flag)
			{
				bizFlag = false;
			}
			
			//开通 cc_cdealmonternet_72  飞信开通非此接口
			if ("1".equals(oprType) && bizFlag)
			{
				if (null != parList && parList.size() > 0)
				{
					RequestParameter par = new RequestParameter();
					par.setParameterName("codeCount");
					par.setParameterValue(parList);
					params.add(par);
				}
				
				//组装发送报文
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cdealmonternet_72", params);
				logger.debug(" ====== 发送报文 ======\n" + reqXml);
				if (null != reqXml && !"".equals(reqXml))
				{
					//发送并接收报文
					rspXml = (String)this.remote.callRemote(
							 new StringTeletext(reqXml, accessId, "cc_cdealmonternet_72", this.generateCity(params)));
					logger.debug(" ====== 返回报文 ======\n" + rspXml);
					if (null != rspXml && !"".equals(rspXml))
					{
						Element root = this.getElement(rspXml.getBytes());
						String resp_code = root.getChild("response").getChildText("resp_code");
						String resp_desc = root.getChild("response").getChildText("resp_desc");
						setCommonResult(re,resp_code,resp_desc);
						if (!"0000".equals(resp_code))
						{
							errDt = this.wellFormedDAO.transBossErrCode("DEL010001", "cc_cdealmonternet_72", resp_code);
							if (null != errDt)
							{
								re.setErrorCode(errDt.getLiErrCode());
								re.setErrorMessage(errDt.getLiErrMsg());
							}
						}
					}
				}
				
			}
			else if ( !bizFlag || "2".equals(oprType))  //关闭 cc_cchgspuserreg_70_SJB 飞信 开通关闭
			{
				RequestParameter par = new RequestParameter();
				par.setParameterName("operating_type");
				if (!bizFlag)
				{
					par.setParameterValue("7");
				}
				else
				{
					par.setParameterValue("8");
				}
				params.add(par);
				
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cchgspuserreg_70_SJB", params);
				logger.debug(" ====== 发送报文 ======\n" + reqXml);
				if (null != reqXml && !"".equals(reqXml))
				{
					//发送并接收报文
					rspXml = (String)this.remote.callRemote(
							 new StringTeletext(reqXml, accessId, "cc_cchgspuserreg_70_SJB", this.generateCity(params)));
					logger.debug(" ====== 返回报文 ======\n" + rspXml);
					if (null != rspXml && !"".equals(rspXml))
					{
						Element root = this.getElement(rspXml.getBytes());
						
						String resCode = root.getChild("response").getChildText("resp_code");
						re.setResultCode("0000".equals(resCode)?"0":"1");
						if (!"0000".equals(resCode))
						{
							re.setErrorCode(root.getChild("response").getChildText("resp_code"));
							re.setErrorMessage(root.getChild("response").getChildText("resp_desc"));
							
							errDt = this.wellFormedDAO.transBossErrCode("DEL010001", "cc_cchgspuserreg_70_SJB", resCode);
							if (null != errDt)
							{
								re.setErrorCode(errDt.getLiErrCode());
								re.setErrorMessage(errDt.getLiErrMsg());
							}
						}
						
						
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		return re;
	}
	
}
