package com.xwtech.xwecp.service.logic.resolver;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.communication.RemoteCallContext;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.SequenceGenerator;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.ServiceExecutor;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;
import com.xwtech.xwecp.service.logic.pojo.VIPUser;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class GetCustVIPInfoResolver implements ITeletextResolver
{
	private static final Logger logger = Logger.getLogger(GetCustVIPInfoResolver.class);
	
	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;
	
	private SequenceGenerator accessIdGenerator = new SequenceGenerator();

	private Map map;
	
	public GetCustVIPInfoResolver()
	{

		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
		
		if (this.map == null)
		{
			this.map = new HashMap();
		}
	
	}
	
	public void resolve(BaseServiceInvocationResult result, Object bossResponse, 
            List<RequestParameter> param) throws Exception
    {
		VIPUser dt = null;
		List<VIPUser> list = null;
		List<VIPUser> reList = new ArrayList();
		
		try
		{
			QRY020001Result ret = (QRY020001Result)result;
			list = ret.getVipUser();
			if (null != list && list.size() > 0)
			{
				resetVIPInfo(list,reList,param);
			}
			ret.setVipUser(reList);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
    }
	
	private List<VIPUser> resetVIPInfo(List<VIPUser> list, List<VIPUser> reList,List<RequestParameter> param) 
	{
		String grade="";
		for(VIPUser vipUser : list)
		{
			grade = vipUser.getGrade();
			if(null != grade && !grade.equals(""))
			{
				grade = resetGrade(grade,param);
				vipUser.setGrade(grade);
				reList.add(vipUser);
			}
		}
		return reList;
	}
	
	private String resetGrade(String grade,List<RequestParameter> param)
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		String resp_desc = "";
		ErrorMapping errDt = null;
		param.add(new RequestParameter("dict_type",127));
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_get_dictlist_78", param);
			logger.debug(" ====== 查询数据字典 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, this.accessIdGenerator.next(), "cc_get_dictlist_78", generateCity(param)));
				logger.debug(" ====== 查询数据字典  接收报文 ====== \n" + rspXml);
			}
			root = this.getElement(rspXml.getBytes());
			resp_code = root.getChild("response").getChildText("resp_code");
			if (null != resp_code && "0000".equals(resp_code)) {
				List dictList = this.getContentList(root, "list_size");
				if (null != dictList && dictList.size() > 0)
				{
					for (int i = 0; i < dictList.size(); i++)
					{
						if (null == this.map.get(this.getChildText((Element)dictList.get(i), "dict_code")))
						{
							this.map.put(
									this.getChildText((Element)dictList.get(i), "dict_code"), 
									this.getChildText((Element)dictList.get(i), "dict_code_desc"));
						}
					}
				}
			}
		} catch (Exception e) 
		{
			logger.error(e, e);
		}
		if(this.map.size() > 0)
		{
			grade = (String) this.map.get(grade);
		}
		return grade;
	}
	
	public List getContentList(Element root, String name)
	{
		List list = null;
		try
		{
			list = root.getChild("content").getChildren(name);
		}
		catch (Exception e)
		{
			list = null;
		}
		return list;
	}
	
	public Element getElement(byte[] tmp)
	{
		Element root = null;
		try
		{
			ByteArrayInputStream ins = new ByteArrayInputStream(tmp);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(ins);
			root = doc.getRootElement();
			return root;
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		return root;
	}
	public String getChildText(Element e, String childName)
	{
		String str = "";
		
		try
		{
			str = e.getChildText(childName)==null?"":e.getChildText(childName).trim();
		}
		catch(Exception ex)
		{
			//logger.error(ex, ex);
			str = "";
		}
		
		return str;
	}
	protected RemoteCallContext generateCity (List<RequestParameter> params) throws Exception {
		
		RequestParameter userCityParam = this.getParameter(params, ServiceExecutor.ServiceConstants.USER_CITY);
		String userCity = userCityParam != null ? userCityParam.getParameterValue().toString() : "";
		RequestParameter userChannelParam = this.getParameter(params, ServiceExecutor.ServiceConstants.INVOKE_CHANNEL);
		String userChannel = userChannelParam != null ? userChannelParam.getParameterValue().toString() : "";
		RemoteCallContext remoteCallContext = new RemoteCallContext();
		remoteCallContext.setUserCity(userCity);
		remoteCallContext.setInvokeChannel(userChannel);
		
		return remoteCallContext;
	}
	
	protected RequestParameter getParameter(List<RequestParameter> params, String name)
	{
		if (params != null && params.size() > 0) {
			for(int i = 0; i<params.size(); i++)
			{
				RequestParameter param = params.get(i);
				if(param.getParameterName().equals(name))
				{
					return param;
				}
			}
		}
		
		return null;
	}
	public SequenceGenerator getAccessIdGenerator() {
		return accessIdGenerator;
	}

	public void setAccessIdGenerator(SequenceGenerator accessIdGenerator) {
		this.accessIdGenerator = accessIdGenerator;
	}
}