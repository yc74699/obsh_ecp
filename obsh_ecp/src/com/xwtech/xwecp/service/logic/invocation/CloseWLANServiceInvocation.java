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
import com.xwtech.xwecp.service.logic.pojo.DEL010015Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 注消WLAN国际漫游 DEL010015
 * @author yuantao
 * 2010-01-21
 */
public class CloseWLANServiceInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(CloseWLANServiceInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private ParseXmlConfig config;
	
	public CloseWLANServiceInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		DEL010015Result res = new DEL010015Result ();
		
		try
		{
			this.closeWlanService(accessId, config, params, res);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 注销wlan, 注销wlan国际漫游
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public void closeWlanService(String accessId, ServiceConfig config, 
			                 List<RequestParameter> params, DEL010015Result res)
	{
		String reqXml = "";  //发送报文
		String rspXml = "";  //接收报文
		String resp_code = "";  //返回码
		
		String template = "cc_cnewclosewlan_858";
		
		boolean flag = checkOrderType(params);
		if(flag)
		{
			template = "cc_cnewchgwlan_857";
		}
		try
		{
			reqXml = this.bossTeletextUtil.mergeTeletext(template, params);
			logger.debug(" ====== 发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				//发送并接收报文
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, template, this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
			}
			//解析BOSS报文
			if (null != rspXml && !"".equals(rspXml))
			{
				//解析报文 根节点
				Element root = this.config.getElement(rspXml);
				//获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				//错误描述
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				//设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code, resp_desc, template);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
	private boolean checkOrderType(List<RequestParameter> params)
	{
		Object orderType = getParameters(params, "orderType");
		
		if(null == orderType || "".equals(orderType))
		{
			return false;
		}
		else if("2".equals(String.valueOf(orderType)))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 设置结果信息
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @param resp_code
	 */
	public void getErrInfo (String accessId, ServiceConfig config, 
                            List<RequestParameter> params, DEL010015Result res, 
                            String resp_code, String resp_desc, String xmlName)
	{
		ErrorMapping errDt = null;  //错误编码解析
		
		try
		{
			//失败
			if (!"0000".equals(resp_code))
			{
				//解析错误信息
				errDt = this.wellFormedDAO.transBossErrCode("DEL040017", xmlName, resp_code);
				if (null != errDt)
				{
					resp_code = errDt.getLiErrCode();  //设置错误编码
					resp_desc = errDt.getLiErrMsg();  //设置错误信息
				}
			}
			//设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code)?"0":"1");
			res.setErrorCode(resp_code);  //设置错误编码
			res.setErrorMessage(resp_desc);  //设置错误信息
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
}
