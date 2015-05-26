package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
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
import com.xwtech.xwecp.service.logic.pojo.DEL010012Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 十户联防，邀请成员/成员退出
 * @author chenxiaoming_1037
 * 2011-10-19
 */
public class ChgProTenDefMemberInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(ChgProTenDefMemberInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private ParseXmlConfig config;
	
	public ChgProTenDefMemberInvocation ()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		DEL010012Result res = new DEL010012Result ();
		String mainNum = "";  //主卡号码
		String subNum = "";  //副卡号码
		String oprType = "";  //操作类型 0、成员(邀请)1、成员(退出) 
		String chooseFlag = "d0";  //生效方式 d0、立即 d1、次日 m1、次月, 不传按d0(立即)处理
		boolean segBoolean = true;  //归属地比较标识
		
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
					if("chooseFlag".equals(p.getParameterName()))
					{
						if ( null == p.getParameterValue()
								||"".equals(p.getParameterValue())
								|| "0".equals(String.valueOf(p.getParameterValue())))
						{
							p.setParameterValue(chooseFlag);
						}
						else if("1".equals(p.getParameterValue()))
						{
							p.setParameterValue("d1");
						}
						else if("2".equals(p.getParameterValue()))
						{
							p.setParameterValue("m1");
						}
					}
				}
			}
			
			this.addOrQuitTenMember(accessId, config, params, res);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 十户联防办理 --增加 ，退出
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public void addOrQuitTenMember(String accessId, ServiceConfig config, 
			                 List<RequestParameter> params, DEL010012Result res)
	{
		String reqXml = "";  //发送报文
		String rspXml = "";  //接收报文
		String resp_code = "";  //返回码
		String operType = String.valueOf(getParameters(params, "oprType"));
		String template = "cc_cdeltendefmember_69";
		try
		{
			if("0".equals(operType)) //增加成员
			{  
				template = "cc_caddtendefmember_69";
			}
			else if("1".equals(operType)) // 退出成员
			{
				template = "cc_cdeltendefmember_69";
			}
			else
			{
				logger.debug(" 操作类型错误 ");
			}	
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
	
	/**
	 * 归属地信息查询
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	public String[] getNumSeg (String accessId, ServiceConfig config, 
			                 List<RequestParameter> params, DEL010012Result res, 
			                 String phoneNum)
	{
		String reqXml = "";  //发送报文
		String rspXml = "";  //接收报文
		String resp_code = "";  //返回码
		String[] str = {"", ""};  //省，地市
		boolean phoneBoolean = true;  //参数标识
		RequestParameter par = null;
		
		try
		{
			//新增参数
			if (null != params && params.size() > 0)
			{
				for (RequestParameter p : params)
				{
					if ("phoneNum".equals(p.getParameterName()))
					{
						phoneBoolean = false;
						p.setParameterValue(phoneNum);
					}
				}
			}
			//修改参数
			if (phoneBoolean)
			{
				par = new RequestParameter ();
				par.setParameterName("phoneNum");
				par.setParameterValue(phoneNum);
				params.add(par);
			}
			
			//组装发送报文
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetnumseg_551", params);
			logger.debug(" ====== 发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				//发送并接收报文
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_cgetnumseg_551", this.generateCity(params)));
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
				this.getErrInfo(accessId, config, params, res, resp_code, resp_desc, "cc_cgetnumseg_551");
				//成功
				if (null != resp_code && "0000".equals(resp_code))
				{
					str [0] = this.config.getChildText(this.config.getElement(root, "content"), "nhnumbersegment_name");
					str [1] = this.config.getChildText(this.config.getElement(root, "content"), "nhnumbersegment_city_name");
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return str;
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
                            List<RequestParameter> params, DEL010012Result res, 
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
	
	public static void main(String[] args)
	{
		RequestParameter request ;
		ArrayList<RequestParameter> lists = new ArrayList<RequestParameter>();
		
		for(int i = 0; i < 3; i++)
		{
			request = new RequestParameter();
			request.setParameterName("test"+i);
			request.setParameterValue(i);
			lists.add(request);
		}
		
		for(RequestParameter p : lists)
		{
			if(p.getParameterName().equals("test1"))
			{
				p.setParameterValue("nihao");
			}
		}
		for(RequestParameter p : lists)
		{
			System.out.println(p.getParameterName()+": "+p.getParameterValue());
		}
	}
}
