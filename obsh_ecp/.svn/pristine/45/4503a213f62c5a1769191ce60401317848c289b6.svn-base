package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.CommunicateException;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.TeletextParseUtils;

/**
 * 其他业务列表查询(增值业务，自有业务，无线音乐)
 * @author chenxiaoming
 *
 */
public class OtherBusiness4SMSInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(OtherBusiness4SMSInvocation.class);
	
	private String BIZTYPE_ZZYW = "1";//增值业务 
	private String BIZTYPE_ZYYW = "2";//自有业务
	private String BIZTYPE_WXYYJLB = "3";//无线音乐俱乐部

	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Map map;
	
	private Map zyywMap;

	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");

	public OtherBusiness4SMSInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
		if (null == this.map)
		{
			map = new HashMap();
			this.map.put("5000", "短信呼");
			this.map.put("5001", "短信话费提醒(原)");
			this.map.put("5002", "无线终端");
			this.map.put("5003", "盐城特预卡");
			this.map.put("5004", "手机证券");
			this.map.put("5005", "集团GPRS");
			this.map.put("5006", "彩铃");
			this.map.put("5007", "双号传真");
			this.map.put("5008", "双号数据");
			this.map.put("5010", "短信帐单");
			this.map.put("5011", "纸帐单");
			this.map.put("5012", "理财助手(话费易)");
			this.map.put("5013", "动感易");
			this.map.put("5014", "积分短信申请");
			this.map.put("5015", "套餐易");
			this.map.put("5016", "呼转特定号码");
			this.map.put("5017", "生日祝福");
			this.map.put("5018", "积分申请");
			this.map.put("5019", "动感M值计划");
			this.map.put("5020", "彩音");
			this.map.put("5021", "亲情易");
			this.map.put("5022", "被叫易");
			this.map.put("5023", "短信回呼");
			this.map.put("5024", "忙时通");
			this.map.put("5025", "CMNET业务");
			this.map.put("5026", "CMWAP业务");
			this.map.put("5027", "梦网易");
			this.map.put("5028", "终端管理CMDM");
			this.map.put("5029", "彩信账单");
			this.map.put("5030", "来电提醒");
			this.map.put("5032", "数据流量提醒");


		}
		//特殊增值业务
		if(this.zyywMap == null)
		{
			zyywMap = new HashMap();
			zyywMap.put("2200006000", "手机一卡通业务");
			zyywMap.put("2300750009", "家信通");
			zyywMap.put("2400000101", "数据流量提醒");
			zyywMap.put("2200005034", "家庭易");

		}
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY020001Result res = new QRY020001Result();
		List<BossParmDT> parList = null;
		List<GommonBusiness> reList = null;
		String bizId = "";
		try
		{
			//根据业务编码获取其子业务所对应的BOSS实现
			for (RequestParameter r : params)
			{
				if ("bizId".equals(r.getParameterName()))
				{
					bizId = String.valueOf(r.getParameterValue());
					//其他业务 无子业务 展现全返回信息
					parList = this.wellFormedDAO.getSubBossParmList(bizId);
				}
			}
			
			if (!"TCJYWCX_QTYW_SMS".equals(bizId) && null != parList && parList.size() > 0)
			{
				RequestParameter par = new RequestParameter();
				par.setParameterName("codeCount");
				par.setParameterValue(parList);
				params.add(par);
			}
			
			reList = new ArrayList<GommonBusiness>();
			
			// 查询增值业务列表
			List tempList = new ArrayList();
    		Map map1 = new HashMap();
    		map1.put("parm1", "");
    		tempList.add(map1);
    		params.add(new RequestParameter("codeCount", tempList));

			cgetincinfo(params, accessId, res, reList); 
			logger.debug(" ====== 查询增值业务列表 ====== " + res.getResultCode());
			
			// 查询自有业务
			cgetspuserinfo(params, accessId, res, reList);
			logger.debug(" ====== 查询自有业务 ====== " + res.getResultCode());

			// 查询无线音乐俱乐部
			cgetmusicinfo(params, accessId, res, reList);
			logger.debug(" ====== 查询无线音乐俱乐部 ====== " + res.getResultCode());

	
			for (GommonBusiness busi : reList)
			{
				if (null != busi.getEndDate() && !"".equals(busi.getEndDate()))
				{
					busi.setState(4);
				}
			}
			
			res.setGommonBusiness(reList);
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
		
		return res;
	}
	
	//增值业务
	private void cgetincinfo(List<RequestParameter> params, String accessId, BaseServiceInvocationResult res, List<GommonBusiness> reList) throws CommunicateException, Exception {
		String reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetincinfo_346", params);
//		logger.debug(" ====== 查询增值业务发送报文 ======\n" + reqXml);
		if (null != reqXml && !"".equals(reqXml))
		{
			//发送并接收报文
			String rspXml = (String)this.remote.callRemote(
					 new StringTeletext(reqXml, accessId, "cc_cgetincinfo_346", this.generateCity(params)));
//			logger.debug(" ====== 返回增值业务报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml) )
			{
				// 查询用户在用增值业务列表
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				String resp_desc = root.getChild("response").getChildText("resp_desc");
				res.setResultCode(isScuccess(resp_code) ?"0":"1");
				if (!isScuccess(resp_code))
				{
					res.setErrorCode(resp_code);
					res.setErrorMessage(resp_desc);
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY020001", "cc_cgetincinfo_346", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				if (isScuccess(resp_code))
				{
					//增值业务
					List spbizList = root.getChild("content").getChildren("smscall_deal_code");
					if (null != spbizList && spbizList.size() > 0)
					{
						for (int i = 0; i < spbizList.size(); i++)
						{
							Element csmscalldt = ((Element)spbizList.get(i)).getChild("csmscalldt");
							GommonBusiness dt = new GommonBusiness();
							dt.setId(p.matcher(csmscalldt.getChildText("smscall_deal_code")).replaceAll("").trim());
							if("5019".equals(dt.getId()))//屏蔽 动感M值计划，boss未处理
								continue;
							String prdName = "";
							if(this.map.get(dt.getId()) == null)
							{
								if(this.zyywMap.get(dt.getId()) != null)
								{
									prdName = (String)this.zyywMap.get(dt.getId());
								}
							}
							else
							{
								prdName = (String) this.map.get(dt.getId()); 
							}
							dt.setName(prdName);
							dt.setReserve1(dt.getId().trim());
							dt.setBeginDate(p.matcher(csmscalldt.getChildText("smscall_start_date")).replaceAll(""));
							dt.setEndDate(p.matcher(csmscalldt.getChildText("smscall_end_date")).replaceAll(""));
							String strState = p.matcher(csmscalldt.getChildText("smscall_state")).replaceAll("");
							dt.setState(getState(strState));
							dt.setReserve1(BIZTYPE_ZZYW);
							reList.add(dt);
						}
						
					}
				}
			}
		}
		
	}
	
	//自有业务
	private void cgetspuserinfo(List<RequestParameter> params, String accessId, BaseServiceInvocationResult res, List<GommonBusiness> reList) throws CommunicateException, JDOMException, Exception {
		String reqXml2 = this.bossTeletextUtil.mergeTeletext("cc_cgetspuserinfo_358", params);
		String reqXml3 = this.bossTeletextUtil.mergeTeletext("cc_cgetspbizdetail_528", params);
//		logger.debug(" ====== 查询自有业务发送报文 ======\n" + reqXml2);
//		logger.debug(" ====== 查询自有业务发送报文 ======\n" + reqXml3);
		if(null != reqXml2 && !"".equals(reqXml2) && null != reqXml3 && !"".equals(reqXml3)){
			String rspXml2 = (String)this.remote.callRemote(
					new StringTeletext(reqXml2, accessId, "cc_cgetspuserinfo_358", this.generateCity(params)));
			String rspXml3 = (String)this.remote.callRemote(
					new StringTeletext(reqXml3, accessId, "cc_cgetspbizdetail_528", this.generateCity(params)));
			
//			logger.debug(" ====== 返回自有业务报文 ======\n" + rspXml2);
//			logger.debug(" ====== 返回自有业务报文 ======\n" + rspXml3);
			if (null != rspXml2 && !"".equals(rspXml2)
					&& null != rspXml3 && !"".equals(rspXml3))
			{
				
				// 查询此用户开通的所有自有业务
				Element root2 = this.getElement(rspXml2.getBytes());
				String resp_code2 = root2.getChild("response").getChildText("resp_code");
				// 查询自有业务配置信息
				Element root3 = this.getElement(rspXml3.getBytes());
				String resp_code3 = root3.getChild("response").getChildText("resp_code");
				String resp_desc3 = root3.getChild("response").getChildText("resp_desc");

				res.setResultCode(isScuccess(resp_code2)?"0":"1");
				if (!isScuccess(resp_code2))
				{
					res.setErrorCode(resp_code3);
					res.setErrorMessage(resp_desc3);
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY020001", "cc_cgetspuserinfo_358", resp_code2);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				if (isScuccess(resp_code2))
				{
					//自有业务配置信息
					Map<String, String> spDicMap = new HashMap<String, String>();
					Map<String, String> spDic2Map = new HashMap<String, String>();

					if( isScuccess(resp_code3) ){
						String retContent = root3.getChild("content").getChildText("XTABLE_SPPRD");
						List<Map<String,String>> respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList){
								spDicMap.put(m.get("产品编码"), m.get("产品名称"));	
								spDic2Map.put(m.get("产品编码").substring(4),m.get("产品名称"));
							}
						}
					}
					//自有业务
					XPath xpath = XPath.newInstance("/operation_out/content/selfplatuserreg_reg_id/cselfplatuserregdt");
					List<Element> list = (List<Element>) xpath.selectNodes(root2);
					if(list != null && list.size() >0)
					{
						String prdName = "";
						for (Element element : list) 
						{
							GommonBusiness dt = new GommonBusiness();
							String prdCode = element.getChildTextTrim("selfplatuserreg_prd_code");
							if(spDicMap.get(prdCode) == null || "".equals(spDicMap.get(prdCode)))
							{
								if(spDic2Map.get(prdCode) == null || "".equals(spDic2Map.get(prdCode)))
								{
									prdName = (String) this.zyywMap.get(prdCode);
								}
								else
								{
									prdName = spDic2Map.get(prdCode);
								}
							}
							else
							{
								prdName = spDicMap.get(prdCode) ;
							}
							dt.setName(prdName);
							dt.setBeginDate(element.getChildTextTrim("selfplatuserreg_begin_date"));
							dt.setEndDate(element.getChildTextTrim("selfplatuserreg_end_date"));
							dt.setState(getState(element.getChildTextTrim("selfplatuserreg_status")));
							String domain_code = element.getChildTextTrim("selfplatuserreg_domain_code");
							String biz_code = element.getChildTextTrim("selfplatuserreg_biz_code");
							String prd_code = element.getChildTextTrim("selfplatuserreg_prd_code");
							dt.setId(domain_code+","+biz_code+","+prd_code);
							dt.setReserve1(BIZTYPE_ZYYW);
							reList.add(dt);
						}
					}
				}
			}
		}
	}
	
	//无线音乐俱乐部
	private void cgetmusicinfo(List<RequestParameter> params, String accessId, BaseServiceInvocationResult res, List<GommonBusiness> reList) throws CommunicateException, Exception {
		String reqXml4 = this.bossTeletextUtil.mergeTeletext("cc_cgetmusicinfo_391", params);
//		logger.debug(" ====== 查询无线音乐俱乐部发送报文 ======\n" + reqXml4);
		if(null != reqXml4 && !"".equals(reqXml4)){
			String rspXml4 = (String)this.remote.callRemote(
					 new StringTeletext(reqXml4, accessId, "cc_cgetmusicinfo_391", this.generateCity(params)));
//			logger.debug(" ====== 返回无线音乐俱乐部报文 ======\n" + rspXml4);
			Element root4 = this.getElement(rspXml4.getBytes());
			String resp_code4 = root4.getChild("response").getChildText("resp_code");
			String resp_desc4 = root4.getChild("response").getChildText("resp_desc");
			String resultCode = BOSS_SUCCESS.equals(resp_code4) ? LOGIC_SUCESS : LOGIC_ERROR;
			if (!isScuccess(resp_code4))
			{
				res.setErrorCode(resp_code4);
				res.setErrorMessage(resp_desc4);
				ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY020001", "cc_cgetmusicinfo_391", resp_code4);
				if (null != errDt)
				{
					res.setErrorCode(errDt.getLiErrCode());
					res.setErrorMessage(errDt.getLiErrMsg());
				}
			}
			if (isScuccess(resp_code4)){
				List srlList = root4.getChild("content").getChildren("musicclubmember_operating_srl");
				if (null != srlList && srlList.size() > 0)
				{
					for (int i = 0; i < srlList.size(); i++)
					{
						Element musicDt = ((Element)srlList.get(i)).getChild("cmusicclubmemberdt");
						GommonBusiness dt = new GommonBusiness();
						dt.setBeginDate(p.matcher(musicDt.getChildText("musicclubmember_start_date")).replaceAll(""));
						dt.setEndDate(p.matcher(musicDt.getChildText("musicclubmember_end_date")).replaceAll(""));
						String level = musicDt.getChildText("musicclubmember_member_level");
						if("1".equals(level)){
							dt.setName("无线音乐俱乐部(普通会员)");
							dt.setId("WXYYJLB_PTHY");
						}else{
							dt.setName("无线音乐俱乐部(高级会员)");
							dt.setId("WXYYJLB_GJHY");
						}
						dt.setState(2);
						dt.setReserve1(BIZTYPE_WXYYJLB);
						reList.add(dt);
					}
				}
			}
			
		}

	}
	


	

	
	
	
	
	

	private int getState(String strState) {
		int state;
//		if (null != strState && !"".equals(strState))
//		{
//			if ("1".equals(strState))
//			{
//				state = 2;
//			}
//			else if ("2".equals(strState))
//			{
//				state = 1;
//			}
//			else
//			{
//				state = Integer.parseInt(strState);
//			}
//		}
//		else
		//根据现网代码全部显示开通
		{
			state = 2;
		}
		return state;
	}

	private boolean isScuccess(String resp_code){
		if("0000".equals(resp_code) || "-10000".equals(resp_code) || "-13124".equals(resp_code)  
				|| "-2237".equals(resp_code) //用户没有开通IP直通车
				)
			return true;
		else
			return false;
		
	}

	private Boolean isSpecilPrdCode(String pckCode, String[] arr){
		boolean retCode = false;
		if(arr != null  && arr.length>0)
		for(int i = 0; i<arr.length; i++){
			if(pckCode.equals(arr[i])){
				retCode = true;
				break;
			}
		}
		return retCode;
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