package com.xwtech.xwecp.service.logic.invocation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY040050Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

public class QueryWlanUsedInfoInvocation extends BaseInvocation implements
		ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryWlanUsedInfoInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;
	/*校园WLAN的套餐编码*/
	private String wlanSchool = "1064";
	/*WLAN的套餐编码*/
	private String wlan = "1055";
	
	public QueryWlanUsedInfoInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		QRY040050Result res = new QRY040050Result();
		checkWlan(params,accessId,res);
		return res;
	}
	
	
	/*查询个人套餐*/
	private void checkWlan(List<RequestParameter> params ,String accessId,QRY040050Result res)
	{
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		int nowDate = Integer.parseInt(DateTimeUtil.getTodayChar8());
		try{
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_find_package_62_TC", params);
			logger.debug(" ====== 查询个人套餐信息发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_find_package_62_TC", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element personalInfo = this.getElement(rspXml.getBytes());
					String resp_code = personalInfo.getChild("response").getChildText("resp_code");
					String resp_desc = personalInfo.getChild("response").getChildText("resp_desc");
				
					String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
					
					if(!LOGIC_SUCESS.equals(resultCode))
					{
						res.setResultCode(resultCode);
						res.setErrorCode(resp_code);
						res.setErrorMessage(resp_desc);
					}
					else
					{
						List package_code = personalInfo.getChild("content").getChildren("package_code");
						int size = package_code.size();
						
						if (null != package_code && size > 0)
						{
							for (int i = 0; i < size; i++)
							{
								Element cplanpackagedt = ((Element)package_code.get(i)).getChild("cplanpackagedt");

								String packageType = p.matcher(cplanpackagedt.getChildText("package_type")).replaceAll("");
								String packageCode = p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll("");
								int userDate = Integer.parseInt(p.matcher(
										cplanpackagedt.getChildText("package_use_date")).replaceAll("").substring(0, 8));
								if((wlanSchool.equals(packageType) || wlan.equals(packageType)) && nowDate >= userDate)
								{
									setParameter(params, "a_package_id", packageCode);
									String wlanName = p.matcher(cplanpackagedt.getChildText("package_name")).replaceAll("");
									res.setWlanName(wlanName);
									getWlanInfo(res,params,accessId);
									break;
								}
							}
							if(null == res.getTotalWlan() || "".equals(res.getTotalWlan()))
							{
								res.setResultCode(resultCode);
								res.setErrorCode(resp_code);
								res.setErrorMessage(resp_desc);
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
	}
	
	/*查询Wlan的使用情况*/
	private void getWlanInfo(QRY040050Result res,List<RequestParameter> params ,String accessId)
	{
		try{
			String reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetfreeitem_517", params);
			logger.debug(" ====== 查询用户流量使用明细 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "ac_agetfreeitem_517", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element packageInfo = this.getElement(rspXml.getBytes());
					String resp_code = packageInfo.getChild("response").getChildText("resp_code");
					String resp_desc = packageInfo.getChild("response").getChildText("resp_desc");
					
					String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
					
					if(!LOGIC_SUCESS.equals(resultCode))
					{
						res.setResultCode(resultCode);
						res.setErrorCode(resp_code);
						res.setErrorMessage(resp_desc);
					}
					else
					{
						Document doc = DocumentHelper.parseText(rspXml);
						List<Node> freeItemNodes = doc.selectNodes("/operation_out/content/freeitem_dt");
						int totalValue = 0;
						int userdValue = 0;
						for(Node freeItemNode : freeItemNodes)
						{
							String totalWlan = freeItemNode.selectSingleNode("a_freeitem_total_value").getText().trim();
							String usedWlan = freeItemNode.selectSingleNode("a_freeitem_value").getText().trim();
							res.setTotalWlan(totalWlan);
							res.setUsedWlan(usedWlan);
							res.setResultCode(resultCode);
							res.setErrorCode(resp_code);
							res.setErrorMessage(resp_desc);
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
	}
}