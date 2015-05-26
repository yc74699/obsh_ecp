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
import com.xwtech.xwecp.service.logic.pojo.DEL040017Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 省内亲情号码组合办理 DEL040017
 * @author yuantao
 * 2010-01-27
 */
public class ChgProFamailyMsisdnInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(ChgProFamailyMsisdnInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private ParseXmlConfig config;
	
	public ChgProFamailyMsisdnInvocation ()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	/**
	 * 新大陆提供的密钥，需要每两位转成1个字节
	 */
	private static byte[] BOSS_SECRET_KEY = {
		0x0b,0x33,(byte)0xe7,(byte)0xb2,0x51,0x0d,0x75,(byte)0xc3,0x4e,
		(byte)0xdd,(byte)0x3b,(byte)0x51,0x24,0x36,(byte)0xa8,(byte)0x28,
		0x0b,0x33,(byte)0xe7,(byte)0xb2,0x51,0x0d,0x75,(byte)0xc3	
	};
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		DEL040017Result res = new DEL040017Result ();
		String mainNum = "";  //主卡号码
		String subNum = "";  //省内亲情号码：开通、变更时必填
		String oprType = "";  //操作类型 1：开通 2：关闭 3：变更
		String pwd = "";  //用户密码
		
		String chooseFlag ="";//生效方式 0：立即，1：次日，2：次月，关闭：生效的是：次月，未生效的：立即
		
		try
		{
			if (null != params && params.size() > 0)
			{
				
				for (RequestParameter p : params)
				{
					if ("mainNum".equals(p.getParameterName()))
					{
						mainNum = String.valueOf(p.getParameterValue());
					}
					if ("subNum".equals(p.getParameterName()))
					{
						subNum = String.valueOf(p.getParameterValue());
					}
					if ("oprType".equals(p.getParameterName()))
					{
						oprType = String.valueOf(p.getParameterValue());
					}
					if ("pwd".equals(p.getParameterName()))
					{
						pwd = String.valueOf(p.getParameterValue());
					}
					if ("chooseFlag".equals(p.getParameterName()))
					{
						chooseFlag = String.valueOf(p.getParameterValue());
					}
					
				}
			}
			this.chgProFamailyMsisdn(accessId, config, params, res);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		return res;
	}
	
	/**
	 * 国内亲情号码组合办理 --开通关闭变更
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public void chgProFamailyMsisdn (String accessId, ServiceConfig config, 
			                 List<RequestParameter> params, DEL040017Result res)
	{
		String reqXml = "";  //发送报文
		String rspXml = "";  //接收报文
		String resp_code = "";  //返回码
		String content = "";  //content 根据协议，这个操作的接口要判断content下的内容。是否有用未知：处理结果代码 0：成功（其它值需要等开发完确定）

		try
		{
			//组装发送报文
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cdealprovfamily_420", params);
			logger.debug(" ====== 发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				//发送并接收报文
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_cdealprovfamily_420", this.generateCity(params)));
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
				this.getErrInfo(accessId, config, params, res, resp_code, resp_desc, "cc_cdealprovfamily_420");
				
				//0：成功（其它值需要等开发完确定）
				content = this.config.getChildText(this.config.getElement(root, "content"), "result");
				if("0".equals(content) && "0000".equals(resp_code)){
					
				}else{
					res.setResultCode("1");
					res.setErrorCode(resp_code);
					res.setErrorMessage(resp_desc);
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
	//以下的操作废弃，20120409
	/**
	 * 省内亲情号码组合办理 --增加 ，删除
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
//	public void addOrRemoveProFamailyMsisdn(String accessId, ServiceConfig config, 
//			                 List<RequestParameter> params, DEL040017Result res)
//	{
//		String reqXml = "";  //发送报文
//		String rspXml = "";  //接收报文
//		String resp_code = "";  //返回码
//		String operType = String.valueOf(getParameters(params, "oprType"));
//		try
//		{
//			if("4".equals(operType)){  //增加成员
//				//组装发送报文
//				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cdealprovfamily_420_add", params);
//				logger.debug(" ====== 发送报文 ======\n" + reqXml);
//				if (null != reqXml && !"".equals(reqXml))
//				{
//					//发送并接收报文
//					rspXml = (String)this.remote.callRemote(
//							 new StringTeletext(reqXml, accessId, "cc_cdealprovfamily_420_add", this.generateCity(params)));
//					logger.debug(" ====== 返回报文 ======\n" + rspXml);
//				}
//				//解析BOSS报文
//				if (null != rspXml && !"".equals(rspXml))
//				{
//					//解析报文 根节点
//					Element root = this.config.getElement(rspXml);
//					//获取错误编码
//					resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
//					//错误描述
//					String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
//					//设置结果信息
//					this.getErrInfo(accessId, config, params, res, resp_code, resp_desc, "cc_cdealprovfamily_420_add");
//				}
//			}else if("5".equals(operType)){   //删除成员
//				//组装发送报文
//				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cdealprovfamily_420_del", params);
//				logger.debug(" ====== 发送报文 ======\n" + reqXml);
//				if (null != reqXml && !"".equals(reqXml))
//				{
//					//发送并接收报文
//					rspXml = (String)this.remote.callRemote(
//							 new StringTeletext(reqXml, accessId, "cc_cdealprovfamily_420_del", this.generateCity(params)));
//					logger.debug(" ====== 返回报文 ======\n" + rspXml);
//				}
//				//解析BOSS报文
//				if (null != rspXml && !"".equals(rspXml))
//				{
//					//解析报文 根节点
//					Element root = this.config.getElement(rspXml);
//					//获取错误编码
//					resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
//					//错误描述
//					String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
//					//设置结果信息
//					this.getErrInfo(accessId, config, params, res, resp_code, resp_desc, "cc_cdealprovfamily_420_del");
//				}
//			}else{
//				
//				logger.debug(" 操作类型错误 ");
//			}
//			
//			
//			
//		}
//		catch (Exception e)
//		{
//			logger.error(e, e);
//		}
//	}
	
	//对方号码是国内都可以开通。取消归属地的限制
//	/**
//	 * 归属地信息查询
//	 * @param accessId
//	 * @param config
//	 * @param params
//	 * @param res
//	 * @return
//	 */
//	public String[] getNumSeg (String accessId, ServiceConfig config, 
//			                 List<RequestParameter> params, DEL040017Result res, 
//			                 String phoneNum)
//	{
//		String reqXml = "";  //发送报文
//		String rspXml = "";  //接收报文
//		String resp_code = "";  //返回码
//		String[] str = {"", ""};  //省，地市
//		boolean phoneBoolean = true;  //参数标识
//		RequestParameter par = null;
//		
//		try
//		{
//			//新增参数
//			if (null != params && params.size() > 0)
//			{
//				for (RequestParameter p : params)
//				{
//					if ("phoneNum".equals(p.getParameterName()))
//					{
//						phoneBoolean = false;
//						p.setParameterValue(phoneNum);
//					}
//				}
//			}
//			//修改参数
//			if (phoneBoolean)
//			{
//				par = new RequestParameter ();
//				par.setParameterName("phoneNum");
//				par.setParameterValue(phoneNum);
//				params.add(par);
//			}
//			
//			//组装发送报文
//			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetnumseg_551", params);
//			logger.debug(" ====== 发送报文 ======\n" + reqXml);
//			if (null != reqXml && !"".equals(reqXml))
//			{
//				//发送并接收报文
//				rspXml = (String)this.remote.callRemote(
//						 new StringTeletext(reqXml, accessId, "cc_cgetnumseg_551", this.generateCity(params)));
//				logger.debug(" ====== 返回报文 ======\n" + rspXml);
//			}
//			//解析BOSS报文
//			if (null != rspXml && !"".equals(rspXml))
//			{
//				//解析报文 根节点
//				Element root = this.config.getElement(rspXml);
//				//获取错误编码
//				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
//				//错误描述
//				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
//				//设置结果信息
//				this.getErrInfo(accessId, config, params, res, resp_code, resp_desc, "cc_cgetnumseg_551");
//				//成功
//				if (null != resp_code && "0000".equals(resp_code))
//				{
//					str [0] = this.config.getChildText(this.config.getElement(root, "content"), "nhnumbersegment_name");
//					str [1] = this.config.getChildText(this.config.getElement(root, "content"), "nhnumbersegment_city_name");
//				}
//			}
//		}
//		catch (Exception e)
//		{
//			logger.error(e, e);
//		}
//		return str;
//	}
//	
	/**
	 * 设置结果信息
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @param resp_code
	 */
	public void getErrInfo (String accessId, ServiceConfig config, 
                            List<RequestParameter> params, DEL040017Result res, 
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
