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
import com.xwtech.xwecp.service.logic.pojo.DEL040014Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 17202业务办理 DEL040014
 * @author yuantao
 * 2010-01-21
 */
public class Change17202Invocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(QueryPkgUseStateInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private ParseXmlConfig config;
	
	public Change17202Invocation ()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		DEL040014Result res = new DEL040014Result ();
		res.setResultCode("0");  //成功
		String oprType = "";  //操作类型
		String pkgCode = "";  //业务编码
		String limitFlag = "";  //唯一性
		
		try
		{
			//获取操作类型 0:开通1:恢复2:暂停 3:注销 4:密码修改 5:唯一性修改
			if (null != params && params.size() > 0)
			{
				for (RequestParameter p : params)
				{
					if ("oprType".equals(p.getParameterName()))
					{
						oprType = String.valueOf(p.getParameterValue());
					}
					if ("pkgCode".equals(p.getParameterName()))
					{
						pkgCode = String.valueOf(p.getParameterValue());
					}
					if ("limitFlag".equals(p.getParameterName()))
					{
						limitFlag = String.valueOf(p.getParameterValue());
					}
				}
			}
			if ("0".equals(oprType))  //开通
			{
				this.openUser17202(accessId, config, params, pkgCode, res);
			}
			else  //1:恢复2:暂停 3:注销 4:密码修改 5:唯一性修改
			{
				//17202变更
				this.changeUser17202(accessId, config, params, pkgCode, res, oprType, limitFlag);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		return res;
	}
	
	/**
	 * 变更17202
	 * @param accessId
	 * @param config
	 * @param params
	 * @param pkgCode
	 * @param res
	 * @param oprType
	 * @param limitFlag
	 */
	public void changeUser17202 (String accessId, ServiceConfig config, 
                                 List<RequestParameter> params, String pkgCode, 
                                 DEL040014Result res, String oprType, String limitFlag)
	{
		String reqXml = "";  //发送报文
		String rspXml = "";  //接收报文
		String resp_code = "";  //返回码
		RequestParameter par = null;  //参数
		String type = "";  //类型
		String user172State = "2";  //状态
		boolean limitBoolean = false;  //唯一性修改标识
		
		try
		{
			//0:开通1:恢复2:暂停 3:注销 4:密码修改 5:唯一性修改
			if ("1".equals(oprType)){
            	type = "0";
            	user172State = "1";
        	} else if("2".equals(oprType)){
        		type = "0";
        	} else if("3".equals(oprType)){
        		type = "4";
        	} else if("4".equals(oprType)){
        		type = "3";
        	} else if("5".equals(oprType)){
        		limitBoolean = true;
        		type = "2";
        	}
			//唯一性修改
			if (limitBoolean)
			{
				par = new RequestParameter ();
				par.setParameterName("user172_limit_flag");
				par.setParameterValue(limitFlag);
				params.add(par);
			}
			else
			{
				par = new RequestParameter ();
				par.setParameterName("user172_limit_flag");
				par.setParameterValue("");
				params.add(par);
			}
			//类型
			par = new RequestParameter ();
			par.setParameterName("type");
			par.setParameterValue(type);
			params.add(par);
			//状态
			par = new RequestParameter ();
			par.setParameterName("user172_state");
			par.setParameterValue(user172State);
			params.add(par);
			//资费类型，是否包月0：不；1：包
			par = new RequestParameter ();
			par.setParameterName("user172_fee_type");
			par.setParameterValue("");
			params.add(par);
			//user172_open_type
			par = new RequestParameter ();
			par.setParameterName("user172_open_type");
			par.setParameterValue("");
			params.add(par);
			//是否加密
			par = new RequestParameter ();
			par.setParameterName("user172_password_type");
			par.setParameterValue("1");
			params.add(par);
			
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cchguser172_708", params);
			logger.debug(" ====== 17202变更 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_cchguser172_708", this.generateCity(params)));
				logger.debug(" ====== 17202变更 接收报文 ====== \n" + rspXml);
			}
			
			//解析BOSS报文
			if (null != rspXml && !"".equals(rspXml))
			{
				//解析报文 根节点
				Element root = this.config.getElement(rspXml);
				//获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				//设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code, "cc_cchguser172_708");
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
	/**
	 * 17202业务开通
	 * @param accessId
	 * @param config
	 * @param params
	 * @param pkgCode
	 * @param res
	 */
	public void openUser17202 (String accessId, ServiceConfig config, 
			                   List<RequestParameter> params, String pkgCode, 
			                   DEL040014Result res)
	{
		String reqXml = "";  //发送报文
		String rspXml = "";  //接收报文
		String resp_code = "";  //返回码
		RequestParameter par = null;  //参数
		
		try
		{
			//绑定认证的属性 user172_bind_attr
			par = new RequestParameter ();
			par.setParameterName("user172_bind_attr");
			par.setParameterValue("");
			params.add(par);
			//服务类型101：窄带	201：ADSL 301：LAN user172_service_type
			par = new RequestParameter ();
			par.setParameterName("user172_service_type");
			par.setParameterValue("101");
			params.add(par);
			//0 - 不绑定 1 - 绑定 user172_bind_type
			par = new RequestParameter ();
			par.setParameterName("user172_bind_type");
			par.setParameterValue("0");
			params.add(par);
			//资费类型，是否包月0：不；1：包 user172_fee_type
			par = new RequestParameter ();
			par.setParameterName("user172_fee_type");
			if ("3006".equals(pkgCode))
			{
				par.setParameterValue("0");
			}
			else
			{
				par.setParameterValue("1");
			}
			params.add(par);
			
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_copenuser172_571", params);
			logger.debug(" ====== 17202业务开通 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_copenuser172_571", this.generateCity(params)));
				logger.debug(" ====== 17202业务开通 接收报文 ====== \n" + rspXml);
			}
			
			//解析BOSS报文
			if (null != rspXml && !"".equals(rspXml))
			{
				//解析报文 根节点
				Element root = this.config.getElement(rspXml);
				//获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				//设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code, "cc_copenuser172_571");
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
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
            List<RequestParameter> params, DEL040014Result res, String resp_code, String xmlName)
	{
		ErrorMapping errDt = null;  //错误编码解析
		
		try
		{
			//设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code)?"0":"1");
			
			//失败
			if (!"0000".equals(resp_code))
			{
				//解析错误信息
				errDt = this.wellFormedDAO.transBossErrCode("DEL040014", xmlName, resp_code);
				if (null != errDt)
				{
					res.setErrorCode(errDt.getLiErrCode());  //设置错误编码
					res.setErrorMessage(errDt.getLiErrMsg());  //设置错误信息
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
}
