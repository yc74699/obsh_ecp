package com.xwtech.xwecp.service.logic.invocation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

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
import com.xwtech.xwecp.service.logic.pojo.DEL010001Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;
import com.xwtech.xwecp.util.XMLUtil;

public class StopCmnetAndCmwapInvocation extends BaseInvocation implements
		ILogicalService {
	private static final Logger logger = Logger.getLogger(StopCmnetAndCmwapInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	/*新加入无线公话无线赏花禁止办理GJTGAMY和GJTGACT业务*/
	private Set<String> wXGHSets;
	
	private Set<String> wXSHSets;

	private String CMNET = "GPRSGN_CMNET";
	
	private String CMWAP = "GPRSGN_CMWAP";
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	//请求获取个人产品信息失败
	private static  final String errorGetProInfo = "-345";
	/*end*/
	public StopCmnetAndCmwapInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));

		wXSHSets = new HashSet<String>();
		wXGHSets = new HashSet<String>();
		wXGHSets.add("1000100001");
		wXGHSets.add("1000100005");
		wXGHSets.add("1000100040");
		wXGHSets.add("100001");
		wXGHSets.add("100005");
		wXGHSets.add("100040");
		
		wXSHSets.add("1000100097");
		wXSHSets.add("1000100098");
		wXSHSets.add("1000100133");
		wXSHSets.add("1000100134");
		wXSHSets.add("1100006660");
		wXSHSets.add("1400100133");
		wXSHSets.add("1400100134");
		wXSHSets.add("100097");
		wXSHSets.add("100098");
		wXSHSets.add("100133");
		wXSHSets.add("100134");
		wXSHSets.add("006660");
		wXSHSets.add("100133");
		wXSHSets.add("100134");
	}
	
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		DEL010001Result res = new DEL010001Result();
		String id = String.valueOf(getParameters(params,"id"));
		String oprType = String.valueOf(getParameters(params,"oprType"));
		
		if((CMNET.equals(id) || CMWAP.equals(id)) && ("1".equals(oprType) || "3".equals(oprType)))
		{
			if(stopCmnetAndCmwap(res,accessId,config,params))
			{
				return res;
			}
		}
		res = cupuserincrem(res,accessId,config,params);
		
		return res;
	}
	
	private boolean stopCmnetAndCmwap(DEL010001Result res,String accessId, ServiceConfig config,List<RequestParameter> params)
	{
		try
		{
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetproinfo_345", params);
			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);
			String rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetproinfo_345", this.generateCity(params)));
			logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = XMLUtil.getChildText(root,"response","resp_code");
				String errDesc = XMLUtil.getChildText(root,"response","resp_desc");
				
				String resultCode = BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR;
				if(LOGIC_SUCESS.equals(resultCode))
				{
					List userproductinfo_product_info_id = null;
					try {
						userproductinfo_product_info_id = root.getChild("content").getChildren("userproductinfo_product_info_id");
					}
					catch(Exception e)
					{
						userproductinfo_product_info_id = null;
						return false;
					}
					int size =  userproductinfo_product_info_id.size();
					if (null != userproductinfo_product_info_id && size > 0)
					{
						for (int i = 0; i <size; i++)
						{
							Element cuserproductinfodt = ((Element) userproductinfo_product_info_id.get(i)).getChild("cuserproductinfodt");
							if (null != cuserproductinfodt)
							{
								String prodctId = p.matcher(cuserproductinfodt.getChildText("userproductinfo_product_id")).replaceAll("");
								if(wXGHSets.contains(prodctId))
								{
									res.setErrorMessage("您已经办理无线公话业务，不能开通CMNET和CMWAP功能");
									res.setErrorCode(LOGIC_INFO);
									res.setResultCode(LOGIC_ERROR);
									return true;
								}
								else if(wXSHSets.contains(prodctId))
								{
									res.setErrorMessage("您已经办理无线商话业务，不能开通CMNET和CMWAP功能");
									res.setErrorCode(LOGIC_INFO);
									res.setResultCode(LOGIC_ERROR);
									return true;
								}
							}
							return false;
						}
					}
				}
				else
				{
					res.setAccessId(accessId);
					res.setErrorMessage(errDesc);
					res.setErrorCode(errorGetProInfo);
					res.setResultCode(resultCode);
					return true;
				}
			}
		}catch(Exception e)
		{
			logger.error(e, e);
		}
		return true;
	}
	
	private DEL010001Result cupuserincrem(DEL010001Result res,String accessId, ServiceConfig config,List<RequestParameter> params)
	{
		try
		{
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_cupuserincrem_607", params);
			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);
			String rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cupuserincrem_607", this.generateCity(params)));
			logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = XMLUtil.getChildText(root,"response","resp_code");
				String errDesc = XMLUtil.getChildText(root,"response","resp_desc");
				String resultCode = BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR;
				
				res.setErrorMessage(errDesc);
				res.setErrorCode(errCode);
				res.setResultCode(resultCode);
			}
		}catch(Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
}