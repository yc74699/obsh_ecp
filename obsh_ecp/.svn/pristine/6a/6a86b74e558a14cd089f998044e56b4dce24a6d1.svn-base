package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * 手机报
 * @author yuantao
 * 2009-11-07
 */
public class MobilePaperInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(MobilePaperInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	public MobilePaperInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY020001Result ret = new QRY020001Result();
		List list = new ArrayList();
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		String req = "";  //请求报文
		String rsp = "";  //返回报文
		GommonBusiness busi = null;
		
		try
		{
			//用户订购关系查询
			req = this.bossTeletextUtil.mergeTeletext("YHDGGXCX", params);
			if (null != req && !"".equals(req))
			{
				//发送并接收报文
				rsp = (String)this.remote.callRemote(new StringTeletext(req, accessId, "YHDGGXCX", this.generateCity(params)));
				if (null != rsp && !"".equals(rsp))
				{
					Element root = this.getElement(rsp.getBytes());
					String resp_code = root.getChild("response").getChildText("resp_code");
					String resp_desc = root.getChild("response").getChildText("resp_desc");
					ret.setErrorCode(resp_code);
					ret.setResultCode("0000".equals(resp_code)?"0":"1");
					ret.setErrorMessage(resp_desc);
					
					if ("0000".equals(resp_code))
					{
						String ret_code = root.getChild("content").getChildText("ret_code");
						if ("0".equals(ret_code))
						{
							List spbizList = root.getChild("content").getChildren("spbizreg_gsm_user_id");
							if (null != spbizList && spbizList.size() > 0)
							{
								for (int i = 0; i < spbizList.size(); i++)
								{
									busi = new GommonBusiness();
									Element bizDt = ((Element)spbizList.get(i)).getChild("cspbizregdt");
									busi.setId(p.matcher(bizDt.getChildText("spbizreg_sub_biz_val")).replaceAll(""));
									busi.setName(p.matcher(bizDt.getChildText("spbizreg_busi_name")).replaceAll(""));
									busi.setState(Integer.parseInt(p.matcher(bizDt.getChildText("spbizreg_status")).replaceAll("")));
									busi.setBeginDate(p.matcher(bizDt.getChildText("spbizreg_effect_time")).replaceAll(""));
									busi.setEndDate(p.matcher(bizDt.getChildText("spbizreg_end_time")).replaceAll(""));
									list.add(busi);
								}
							}
						}
					}
				}
				else
				{
					ret.setErrorCode("1000");
					ret.setResultCode("1");
					ret.setErrorMessage("未接收到相应报文信息");
				}
			}
			
			//查询用户增值业务信息
			req = this.bossTeletextUtil.mergeTeletext("CXYHZZYWXX", params);
			if (null != req && !"".equals(req))
			{
				rsp = (String)this.remote.callRemote(new StringTeletext(req, accessId, "CXYHZZYWXX", this.generateCity(params)));
				if (null != rsp && !"".equals(rsp))
				{
					Element root = this.getElement(rsp.getBytes());
					String resp_code = root.getChild("response").getChildText("resp_code");
					String resp_desc = root.getChild("response").getChildText("resp_desc");
					ret.setErrorCode(resp_code);
					ret.setResultCode("0000".equals(resp_code)?"0":"1");
					ret.setErrorMessage(resp_desc);
					if ("0000".equals(resp_code))
					{
						String ret_code = root.getChild("content").getChildText("ret_code");
						if ("0".equals(ret_code))
						{
							Element csmscalldt = root.getChild("content").getChild("csmscalldt");
							busi = new GommonBusiness();
							busi.setId(p.matcher(csmscalldt.getChildText("smscall_deal_code")).replaceAll(""));
							busi.setName("增值业务");
							busi.setState(Integer.parseInt(p.matcher(csmscalldt.getChildText("smscall_state")).replaceAll("")));
							busi.setBeginDate(p.matcher(csmscalldt.getChildText("smscall_start_date")).replaceAll(""));
							busi.setEndDate(p.matcher(csmscalldt.getChildText("smscall_end_date")).replaceAll(""));
							list.add(busi);
						}
					}
				}
				else
				{
					ret.setErrorCode("1000");
					ret.setResultCode("1");
					ret.setErrorMessage("未接收到相应报文信息");
				}
			}
			
			//查询梦网显示配置表信息
			req = this.bossTeletextUtil.mergeTeletext("CXMWXSPZBXX", params);
			if (null != req && !"".equals(req))
			{
				rsp = (String)this.remote.callRemote(new StringTeletext(req, accessId, "CXMWXSPZBXX", this.generateCity(params)));
				if (null != rsp && !"".equals(rsp))
				{
					Element root = this.getElement(rsp.getBytes());
					String resp_code = root.getChild("response").getChildText("resp_code");
					String resp_desc = root.getChild("response").getChildText("resp_desc");
					ret.setErrorCode(resp_code);
					ret.setResultCode("0000".equals(resp_code)?"0":"1");
					ret.setErrorMessage(resp_desc);
					if ("0000".equals(resp_code))
					{
						String ret_code = root.getChild("content").getChildText("ret_code");
						if ("0".equals(ret_code))
						{
							List spidList = root.getChild("content").getChildren("monternetcfg_spid");
							if (null != spidList && spidList.size() > 0)
							{
								for (int i = 0; i < spidList.size(); i++)
								{
									busi = new GommonBusiness();
									Element cfgDt = ((Element)spidList.get(i)).getChild("cmonternetcfgdt");
									busi.setId(p.matcher(cfgDt.getChildText("monternetcfg_bizcode")).replaceAll(""));
									busi.setName(p.matcher(cfgDt.getChildText("monternetcfg_bizname")).replaceAll(""));
									busi.setState(0);
									busi.setBeginDate("");
									busi.setEndDate("");
									list.add(busi);
								}
							}
						}
					}
				}
				else
				{
					ret.setErrorCode("1000");
					ret.setResultCode("1");
					ret.setErrorMessage("未接收到相应报文信息");
				}
			}
			
			if (list.size() > 0)
			{
				ret.setGommonBusiness(list);
				ret.setResultCode("0");
				ret.setErrorCode("0000");
				ret.setErrorMessage("");
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		
		return ret;
	}
	
	/**
	 * 解析报文
	 * @param tmp
	 * @return
	 */
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
}
