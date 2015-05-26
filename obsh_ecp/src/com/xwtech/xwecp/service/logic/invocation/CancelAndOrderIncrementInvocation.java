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
import com.xwtech.xwecp.service.logic.pojo.DEL010018Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 统一增值业务。退订与订购
 * @author chenxiaoming_1037
 * 
 */
public class CancelAndOrderIncrementInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(CancelAndOrderIncrementInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private static String ORDER_TYPE = "1";
	
	private static String CANCEL_TYPE = "2";
	
	private String teletextTempate ;
	
	private boolean isOrder = true;
	
	public CancelAndOrderIncrementInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId, 
			                ServiceConfig config, List<RequestParameter> params)
	{
		DEL010018Result re = new DEL010018Result();
		String reqXml = "";
		String rspXml = "";
		String optType = getParameters(params, "optType").toString();
		ErrorMapping errDt = null;
		// 根据操作类型是退订还是订购,对应模板
		String teletextTemplate = getTempate(optType);
		try {
			// 组装发送报文
			reqXml = this.bossTeletextUtil.mergeTeletext(teletextTemplate,
					params);
			logger.info(" ====== 发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				// 发送并接收报文
				rspXml = (String) this.remote.callRemote(new StringTeletext(
						reqXml, accessId, teletextTemplate, this
								.generateCity(params)));
				logger.info(" ====== 返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml)) {
					Element root = this.getElement(rspXml.getBytes());

					String errCode = root.getChild("response").getChildText(
							"resp_code");
					String errDesc = root.getChild("response").getChildText(
							"resp_desc");
					 
					if (!BOSS_SUCCESS.equals(errCode)) {
						errDt = this.wellFormedDAO.transBossErrCode(
								"DEL010006", teletextTemplate, errCode);
						if (null != errDt) {
							errCode = errDt.getLiErrCode();
							errDesc = errDt.getLiErrMsg();
						}
					}
					
					String resultCode = BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR;
					re.setResultCode(resultCode);
					re.setErrorCode(errCode);
					
					String ordResultCode = "";
					if(isOrder)
					{
						ordResultCode = root.getChild("content").getChildText("rapidordresult");
						re.setResultCode(ordResultCode);
						re.setErrorCode(ordResultCode);
					}
					re.setErrorMessage(errDesc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
	}

	private String getTempate(String optType)
	{
		if(ORDER_TYPE.equals(optType) && null != optType)
		{
			this.teletextTempate = "cc_ordersmsusersp_1112";
		}
		else if(CANCEL_TYPE.equals(optType) || null == optType ||"".equals(optType))
		{
			this.teletextTempate = "cc_ccancelsmsusersp_922";
			
			isOrder = false;
		}
		return teletextTempate;
	}
	
	public String getTeletextTempate() 
	{
		return teletextTempate;
	}

	public void setTeletextTempate(String teletextTempate)
	{
		this.teletextTempate = teletextTempate;
	}

}
