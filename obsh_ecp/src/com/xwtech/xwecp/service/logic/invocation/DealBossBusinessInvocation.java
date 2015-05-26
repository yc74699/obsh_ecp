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
import com.xwtech.xwecp.service.logic.pojo.DEL010005Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * boss业务办理
 * @author 吴宗德
 * 
 */
public class DealBossBusinessInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(DealBossBusinessInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	public DealBossBusinessInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId, 
			                ServiceConfig config, List<RequestParameter> params)
	{
		DEL010005Result re = new DEL010005Result();
		String reqXml = "";
		String rspXml = "";
		String id = "";
		String bizType = "";
		ErrorMapping errDt = null;
				
		try
		{
			//根据业务编码获取其子业务所对应的BOSS实现
			for (RequestParameter r : params)
			{
				//1、增值业务 2、自有业务
				if ("reserve1".equals(r.getParameterName()))
				{
					bizType = String.valueOf(r.getParameterValue());
				}
				if ("id".equals(r.getParameterName()))
				{
					id = String.valueOf(r.getParameterValue());
				}
			}
			
			
			//增值业务
			if ("1".equals(bizType))
			{
				//组装发送报文
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cupuserincrem_607_boss", params);
				logger.info(" ====== 发送报文 ======\n" + reqXml);
				if (null != reqXml && !"".equals(reqXml))
				{
					//发送并接收报文
					rspXml = (String)this.remote.callRemote(
							 new StringTeletext(reqXml, accessId, "cc_cupuserincrem_607_boss", this.generateCity(params)));
					logger.info(" ====== 返回报文 ======\n" + rspXml);
					if (null != rspXml && !"".equals(rspXml))
					{
						Element root = this.getElement(rspXml.getBytes());
						
						String errCode = root.getChild("response").getChildText("resp_code");
						String errDesc = root.getChild("response").getChildText("resp_desc");
						
						if (!BOSS_SUCCESS.equals(errCode))
						{
							errDt = this.wellFormedDAO.transBossErrCode("DEL010005", "cc_cupuserincrem_607_boss", errCode);
							if (null != errDt)
							{
								errCode = errDt.getLiErrCode();
								errDesc = errDt.getLiErrMsg();
							}
						}
						re.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
						re.setErrorCode(errCode);
						re.setErrorMessage(errDesc);
					}
				}
				
			}
			//自有业务
			else if ("2".equals(bizType))
			{
				String [] bossIds = id.split(",");
				//平台编码
				params.add(new RequestParameter("domain_code", bossIds[0]));
				//业务编码
				params.add(new RequestParameter("biz_code", bossIds[1]));
				//产品编码
				params.add(new RequestParameter("prd_code", bossIds[2]));
				
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cdealspchg_71_boss", params);
				logger.info(" ====== 发送报文 ======\n" + reqXml);
				if (null != reqXml && !"".equals(reqXml))
				{
					//发送并接收报文
					rspXml = (String)this.remote.callRemote(
							 new StringTeletext(reqXml, accessId, "cc_cdealspchg_71_boss", this.generateCity(params)));
					logger.info(" ====== 返回报文 ======\n" + rspXml);
					if (null != rspXml && !"".equals(rspXml))
					{
						Element root = this.getElement(rspXml.getBytes());
						
						String errCode = root.getChild("response").getChildText("resp_code");
						String errDesc = root.getChild("response").getChildText("resp_desc");
						
						if (!BOSS_SUCCESS.equals(errCode))
						{
							errDt = this.wellFormedDAO.transBossErrCode("DEL010005", "cc_cdealspchg_71_boss", errCode);
							if (null != errDt)
							{
								errCode = errDt.getLiErrCode();
								errDesc = errDt.getLiErrMsg();
							}
						}
						re.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
						re.setErrorCode(errCode);
						re.setErrorMessage(errDesc);
					}
				}
			}
			//套餐
			else if ("4".equals(bizType))
			{
				String [] bossIds = id.split(",");
				//套餐大类
				params.add(new RequestParameter("package_type", bossIds[0]));
				//套餐编码
				params.add(new RequestParameter("package_code", bossIds[1]));
				
				//组装发送报文
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cchgpkgforpro_76_boss", params);
				logger.info(" ====== 发送报文 ======\n" + reqXml);
				if (null != reqXml && !"".equals(reqXml))
				{
					//发送并接收报文
					rspXml = (String)this.remote.callRemote(
							 new StringTeletext(reqXml, accessId, "cc_cchgpkgforpro_76_boss", this.generateCity(params)));
					logger.info(" ====== 返回报文 ======\n" + rspXml);
					if (null != rspXml && !"".equals(rspXml))
					{
						Element root = this.getElement(rspXml.getBytes());
						
						String errCode = root.getChild("response").getChildText("resp_code");
						String errDesc = root.getChild("response").getChildText("resp_desc");
						
						if (!BOSS_SUCCESS.equals(errCode))
						{
							errDt = this.wellFormedDAO.transBossErrCode("DEL010005", "cc_cchgpkgforpro_76_boss", errCode);
							if (null != errDt)
							{
								errCode = errDt.getLiErrCode();
								errDesc = errDt.getLiErrMsg();
							}
						}
						re.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
						re.setErrorCode(errCode);
						re.setErrorMessage(errDesc);
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
