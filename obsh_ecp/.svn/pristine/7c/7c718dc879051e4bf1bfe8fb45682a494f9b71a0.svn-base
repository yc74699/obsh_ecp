package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.XMLUtil;

/**
 * 用户已订购集团V网分组产品套餐查询
 * 
 * @author wanghg
 * 
 */
public class FindVNetPkgInfoInvocation extends BaseInvocation implements
		ILogicalService {
	private static final Logger logger = Logger
			.getLogger(FindVNetPkgInfoInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private ParseXmlConfig config;

	private Map<String,String> map;

	public FindVNetPkgInfoInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.config = new ParseXmlConfig();
		
		if (null == this.map) {
			map = new HashMap<String, String>();
			this.map.put("2000001586", "1元包本地主叫200分钟(09版集团套餐)");
			this.map.put("2000001587", "3元包本地主叫500分钟(09版集团套餐)");
			this.map.put("2000001588", "5元包本地主叫800分钟(09版集团套餐)");
			this.map.put("2000001589", "5元包省内主叫500分钟(09版集团套餐)");
			this.map.put("2000001590", "10元包省内主叫800分钟(09版集团套餐)");
			this.map.put("2000003711", "5元包集团200分钟和本地网内100分钟");
			this.map.put("2000003712", "10元包集团500分钟和本地网内200分钟");
			this.map.put("2000003713", "15元包集团800分钟和本地网内300分钟");
			this.map.put("2000005873", "1元包本地主叫300分钟");
			this.map.put("2000005874", "3元包省内主叫500分钟");
			this.map.put("2000005875", "5元包省内主叫800分钟");
			this.map.put("2000005876", "10元包省内主叫1500分钟");
			this.map.put("2000003711", "5元包集团200分钟和本地网内100分钟（月最低消费68元）");
			this.map.put("2000003712", "集团500分钟和本地网内200分钟（月最低消费68元）");
			this.map.put("2000003713", "15元包集团800分钟和本地网内300分钟（月最低消费68元）");
			this.map.put("2000003342", "5元包集团200分钟和本地网内100分钟（月最低消费48元）");
			this.map.put("2000003343", "10元包集团500分钟和本地网内200分钟（月最低消费48元）");
			this.map.put("2000003344", "15元包集团800分钟和本地网内300分钟（月最低消费48元）");
			this.map.put("2000003316", "5元包集团200分钟和本地网内100分钟（月最低消费58元）");
			this.map.put("2000003317", "10元包集团500分钟和本地网内200分钟（月最低消费58元）");
			this.map.put("2000003318", "15元包集团800分钟和本地网内300分钟（月最低消费58元）");
			this.map.put("2000005124", "10元包主叫300元");
			this.map.put("2000005504", "5元包主叫300元");
			this.map.put("2000003527", "10元包主叫300元");
			this.map.put("2000005133", "5元包主叫200元");
			this.map.put("2000005224", "10元包主叫300元");
			this.map.put("2000005056", "30元包主叫600元");
			this.map.put("2000005055", "30元包主叫500元");
			
			this.map.put("2000003816", "5元包集团200分钟和网内长市100分钟（2014版，58元月最低消费，不足需补齐）");
			this.map.put("2000003817", "10元包集团500分钟和网内长市200分钟（2014版，58元月最低消费，不足需补齐）");
			this.map.put("2000003818", "15元包集团800分钟和网内长市300分钟（2014版，58元月最低消费，不足需补齐）");
			this.map.put("2000003822", "5元包网内长市100分钟（2014版，58元月最低消费，不足需补齐）");
			this.map.put("2000003823", "10元包网内长市200分钟（2014版，58元月最低消费，不足需补齐）");
			this.map.put("2000003824", "15元包网内长市300分钟（2014版，58元月最低消费，不足需补齐）");
			
			this.map.put("2000003825", "5元包集团200分钟和网内长市100分钟(2014版，68元月最低消费，不足需补齐)");
			this.map.put("2000003826", "10元包集团500分钟和网内长市200分钟(2014版，68元月最低消费，不足需补齐)");
			this.map.put("2000003827", "15元包集团800分钟和网内长市300分钟(2014版，68元月最低消费，不足需补齐)");
			this.map.put("2000003831", "5元包网内长市100分钟(2014版，68元月最低消费，不足需补齐)");
			this.map.put("2000003832", "10元包网内长市200分钟(2014版，68元月最低消费，不足需补齐)");
			this.map.put("2000003833", "15元包网内长市300分钟(2014版，68元月最低消费，不足需补齐)");
			
			this.map.put("2000003819", "5元包家庭500分钟和网内长市100分钟（2014版，58元月最低消费，不足需补齐）");
			this.map.put("2000003820", "10元包家庭500分钟和网内长市200分钟（2014版，58元月最低消费，不足需补齐）");
			this.map.put("2000003821", "15元包家庭500分钟和网内长市300分钟（2014版，58元月最低消费，不足需补齐）");
			this.map.put("2000003828", "5元包家庭500分钟和网内长市100分钟(2014版，68元月最低消费，不足需补齐)");
			this.map.put("2000003829", "10元包家庭500分钟和网内长市200分钟(2014版，68元月最低消费，不足需补齐)");
			this.map.put("2000003830", "15元包家庭500分钟和网内长市300分钟(2014版，68元月最低消费，不足需补齐)");
		}
		
		}


	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		QRY020001Result res = new QRY020001Result();
		List<GommonBusiness> gbList = new ArrayList<GommonBusiness>();
		String reqXml = "";
		String rspXml = "";
		
		String channel = (String) getParameters(params,"channel");
		
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("ccqueryvwinfo",params);
			logger.info(" ====== 发送报文 ======\n" + reqXml);
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId,"ccqueryvwinfo", this.generateCity(params)));
			logger.info(" ====== 返回报文 ======\n" + rspXml);
			
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				
				if (!"0000".equals(resp_code))
				{
					res.setErrorCode(resp_code);
					res.setErrorMessage(this.config.getChildText(this.config.getElement(root, "response"), "resp_desc"));
				} 
				else
				{

					GommonBusiness gb = null;
					List gsubs = this.getContentList(root,
							"order_info");
					if (null != gsubs && gsubs.size() > 0)
					{
						for (int i = 0; i < gsubs.size(); i++)
						{
							gb = new GommonBusiness();
							Element grpDT = ((Element) gsubs.get(i));
							
							if (null != grpDT)
							{
								gb.setReserve1(XMLUtil.getChildText(grpDT,"customer_name"));
								gb.setReserve2(XMLUtil.getChildText(grpDT,"dataunit")); 
								gb.setBeginDate(DateTimeUtil.getFormatNormal(XMLUtil.getChildText(grpDT,"start_date").substring(0, 8)));
								if(!"".equals(XMLUtil.getChildText(grpDT,"end_date")) && null !=XMLUtil.getChildText(grpDT,"end_date"))
								{
									gb.setEndDate(DateTimeUtil.getFormatNormal(XMLUtil.getChildText(grpDT,"end_date").substring(0,8)));
								}
								else
								{
									gb.setEndDate("");
								}
								//新加判断，因网厅的CRM和掌厅网页版的CRM返回的报文不一样，所以根据渠道名来取对应的值
								String prodId = "";
								if("wap_channel".equals(channel))
								{
									prodId = XMLUtil.getChildText(grpDT,"package_code");
				
								}
								else
								{
									prodId = XMLUtil.getChildText(grpDT,"product_code");
								
								}
								gb.setJtvwId(String.valueOf(XMLUtil.getChildText(grpDT,"customer_code")));//集团编码
								gb.setId(prodId);
					
								gb.setName(this.map.get(prodId) == null ? "集团V网分组产品" : this.map.get(prodId).toString());
							}
							gbList.add(gb);
						}
					}
				}
				res.setGommonBusiness(gbList);
			}
		} 
		catch (Exception e)
		{
			logger.error(e, e);
		}

		return res;
	}

	

	/*
	 * 获取content下父节点信息
	 * 
	 * @param root @param name @return
	 * 
	 * 
	 */
	public List<String> getContentList(Element root, String name) {
		List<String> list = null;
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
	
	/**
	 * 获取子节点
	 * 
	 * @param e
	 * @param name
	 * @return
	 */
	public Element getElement(Element e, String name) {
		Element dt = null;
		try 
		{
			dt = e.getChild(name);
		}
		catch (Exception ex)
		{
			dt = null;
		}
		return dt;
	}
}