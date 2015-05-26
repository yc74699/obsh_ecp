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
 * 产品增值业务列表查询
 * cc_cgetincinfo
 * @author yuantao
 *
 */
public class OtherBusinessInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(OtherBusinessInvocation.class);
	
	private String BIZTYPE_ZZYW = "1";//增值业务 
	private String BIZTYPE_ZYYW = "2";//自有业务
	private String BIZTYPE_WXYYJLB = "3";//无线音乐俱乐部
	private String BIZTYPE_TC = "4";//套餐
	private String BIZTYPE_JTMS = "5";//交通秘书
	private String BIZTYPE_IPZTC = "6";//IP直通车
	private String BIZTYPE_SNYKDH = "7";//省内一卡多号
	private String BIZTYPE_SJYX = "8";//查询手机游戏
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Map map;

	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");

	public OtherBusinessInvocation()
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
		}
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY020001Result res = new QRY020001Result();
		List<BossParmDT> parList = null;
		List<GommonBusiness> reList = null;
		String reqXml = "";
		String reqXml2 = "";
		String reqXml3 = "";
		String reqXml4 = "";
		String rspXml = "";
		String rspXml2 = "";
		String rspXml3 = "";
		String rspXml4 = "";
		GommonBusiness dt = null;
		String bizId = "";
		ErrorMapping errDt = null;
		
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
					/*if (!"TCJYWCX_QTYW".equals(bizId))
					{
						
					}*/
				}
			}
			
			if (!"TCJYWCX_QTYW".equals(bizId) && null != parList && parList.size() > 0)
			{
				RequestParameter par = new RequestParameter();
				par.setParameterName("codeCount");
				par.setParameterValue(parList);
				params.add(par);
			}
			
			reList = new ArrayList<GommonBusiness>();
			
			// 查询增值业务列表
			//cgetincinfo(params, accessId, res, reList);
//			logger.debug(" ====== 查询增值业务列表 ====== " + res.getResultCode());
			// 查询自有业务
//			if ("0".equals(res.getResultCode())) {
//				cgetspuserinfo(params, accessId, res, reList);
//			}
//			logger.debug(" ====== 查询自有业务 ====== " + res.getResultCode());

			// 查询交通秘书
			//if ("0".equals(res.getResultCode())) {
				cgettrffinfo(params, accessId, res, reList);
			//}
//			logger.debug(" ====== 查询交通秘书 ====== " + res.getResultCode());

			// 查询无线音乐俱乐部
//			if ("0".equals(res.getResultCode())) {
//				cgetmusicinfo(params, accessId, res, reList);
//			}
//			logger.debug(" ====== 查询无线音乐俱乐部 ====== " + res.getResultCode());

			// 查询IP直通车
			if ("0".equals(res.getResultCode())) {
				cqueryiplinear(params, accessId, res, reList);
			}
//			logger.debug(" ====== 查询IP直通车 ====== " + res.getResultCode());

			// 查询省内一卡多号
			if ("0".equals(res.getResultCode())) {
				cqrymultiinfo(params, accessId, res, reList);
			}
//			logger.debug(" ====== 查询省内一卡多号 ====== " + res.getResultCode());
			
			// 手机游戏
//			if ("0".equals(res.getResultCode())) {
//				cgetusermobile(params, accessId, res, reList);
//			}
//			logger.debug(" ====== 查询手机游戏 ====== " + res.getResultCode());
			
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
				res.setResultCode(isScuccess(resp_code) ?"0":"1");
				if (!isScuccess(resp_code))
				{
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
							dt.setName(String.valueOf(this.map.get(dt.getId())==null?"":this.map.get(dt.getId())));
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
				res.setResultCode(isScuccess(resp_code2)?"0":"1");
				if (!isScuccess(resp_code2))
				{
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
					if( isScuccess(resp_code3) ){
						String retContent = root3.getChild("content").getChildText("XTABLE_SPPRD");
						List<Map<String,String>> respList = TeletextParseUtils.parseXTABLE(retContent);
						if(respList != null)
						{
							for(Map<String,String> m : respList){
								spDicMap.put(m.get("产品编码"), m.get("产品名称"));	
							}
						}
					}

					
					//自有业务
					//参照老网营代码queryOpenIncBizAndSelfBiz（）
					XPath xpath = XPath.newInstance("/operation_out/content/selfplatuserreg_reg_id/cselfplatuserregdt");
					List<Element> list = (List<Element>) xpath.selectNodes(root2);
					if(list != null && list.size() >0){
						//健康随行
						String[] arrHealthPrdCode = {"930101","930102","930103","930104","930105","930106","930107","930108"};
						String str_JKSX = "健康随行-";
						
						//生活小贴士
						String[] arrLifeTipPrdCode = {"930109","930110","930111","930112","930113","930114","930119","930122","930123","930124"};
						String str_SHXTS = "生活小贴士-";
						
						//家教超市
						String[] arrTeachMarketPrdCode = {"100001","100002","100003","110001","110002","110003","120001","130001","140001","200001","200002","200003","200004"};
						String str_JJCS = "家教超市-";
						
						//营养百科
						String[] arrNutritionPrdCode = {"250001","250002","250003"};
						String str_YYBK = "营养百科-";
						
						//英语天地产品代码
						String[] arrEngworldPrdCode = {"260001","270001","280001","290001","520002","530002"};
						String str_eng = "英语天地-";
						
						//移动商情产品代码
						String[] arrYDSQPrdCode = {"940001","940002","940003","940004","940005","940006","940007","940008","940009"};
						String str_YDSQ = "移动商情-";
						
						//体彩之家
						String[] arrTCZJPrdCode = {"970001","970002"};
						String str_TCZJ = "体彩之家-";
						
						String[] arrQZTPrdCode = {"510001","510002"};
						String str_QZT = "求职通-";

						for (Element element : list) {
							GommonBusiness dt = new GommonBusiness();
							
							String prdCode = element.getChildTextTrim("selfplatuserreg_prd_code");
							//boss返回3元版，5元版，需做修改
							if("540002".equals(prdCode))
							{
								dt.setName("12580联盟会员俱乐部3元版");
							}
							else if("540003".equals(prdCode))
							{
								dt.setName("12580联盟会员俱乐部5元版");
							}
							else if("540004".equals(prdCode))
							{
								dt.setName("12580联盟会员俱乐部10元版");
							}
							//健康随行
							else if (isSpecilPrdCode(prdCode, arrHealthPrdCode)){
								dt.setName(str_JKSX + spDicMap.get(prdCode));
							}
							//生活小贴士
							else if (isSpecilPrdCode(prdCode, arrLifeTipPrdCode)){
								dt.setName(str_SHXTS + spDicMap.get(prdCode));
							}
							//营养百科
							else if (isSpecilPrdCode(prdCode, arrNutritionPrdCode)){
								dt.setName(str_YYBK + spDicMap.get(prdCode));
							}
							//家教超市
							else if (isSpecilPrdCode(prdCode, arrTeachMarketPrdCode))
							{
								dt.setName(str_JJCS + spDicMap.get(prdCode));
							}
							//英语天地
							else if (isSpecilPrdCode(prdCode, arrEngworldPrdCode))
							{
								dt.setName(str_eng + spDicMap.get(prdCode));
							}
							//移动商情
							else if (isSpecilPrdCode(prdCode, arrYDSQPrdCode))
							{
								dt.setName(str_YDSQ + spDicMap.get(prdCode));
							}
							//体彩之家
							else if (isSpecilPrdCode(prdCode, arrTCZJPrdCode))
							{
								dt.setName(str_TCZJ + spDicMap.get(prdCode));
							}
							//求职通
							else if (isSpecilPrdCode(prdCode, arrQZTPrdCode))
							{
								dt.setName(str_QZT + spDicMap.get(prdCode));
							}
							else
							{
								dt.setName(spDicMap.get(prdCode));
							}
							dt.setBeginDate(element.getChildTextTrim("selfplatuserreg_begin_date"));
							dt.setEndDate(element.getChildTextTrim("selfplatuserreg_end_date"));
//							dt.setDomainCode(element.getChildTextTrim("selfplatuserreg_domain_code"));
//							dt.setBizCode(element.getChildTextTrim("selfplatuserreg_biz_code"));
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
			if (!isScuccess(resp_code4))
			{
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
	
	//查询交通秘书套餐
	private void cgettrffinfo(List<RequestParameter> params, String accessId, BaseServiceInvocationResult res, List<GommonBusiness> reList) throws CommunicateException, JDOMException, Exception {
		RequestParameter par;
		//新增参数
		par = new RequestParameter ();
		par.setParameterName("bossproduct_flag");  //标记
		par.setParameterValue("2");
		params.add(par);
		par = new RequestParameter ();
		par.setParameterName("trafficseccfg_biz_code");
		par.setParameterValue("18");
		params.add(par);

		String reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgettrffinfo_367", params);
//		logger.debug(" ====== 查询交通秘书套餐发送报文 ======\n" + reqXml);
		if (null != reqXml && !"".equals(reqXml))
		{
			//发送并接收报文
			String rspXml = (String)this.remote.callRemote(
					 new StringTeletext(reqXml, accessId, "cc_cgettrffinfo_367", this.generateCity(params)));
//			logger.debug(" ====== 返回交通秘书套餐报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml) )
			{
				
				// 查询用户在用增值业务列表
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				res.setResultCode(isScuccess(resp_code) ?"0":"1");
				if (!isScuccess(resp_code))
				{
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY020001", "cc_cgettrffinfo_367", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				if (isScuccess(resp_code) && root.getChild("content") != null)
				{
					
					if(root.getChild("content").getChild("trafficseccfg_domain_code") != null){
						String domainCode = root.getChild("content").getChild("trafficseccfg_domain_code").getChild("ctrafficseccfgdt").getChildText("trafficseccfg_domain_code");
						List spbizList = root.getChild("content").getChildren("trafficsecuser_operating_srl");
						if (null != spbizList && spbizList.size() > 0)
						{
							for (int i = 0; i < spbizList.size(); i++)
							{
								Element csmscalldt = ((Element)spbizList.get(i)).getChild("ctrafficsecuserdt");
								GommonBusiness dt = new GommonBusiness();
								String prdCode = p.matcher(csmscalldt.getChildText("trafficsecuser_prd_code")).replaceAll("").trim();
	//							String domainCode = p.matcher(csmscalldt.getChildText("trafficseccfg_domain_code")).replaceAll("").trim();
								String cardId = p.matcher(csmscalldt.getChildText("trafficsecuser_car_id")).replaceAll("").trim();
								dt.setId(prdCode+","+domainCode);
								String tName = "";
								if("180001".equals(prdCode)){
									tName = "5元版";
								}else if("180002".equals(prdCode)){
									tName = "10元版";
								}else if("180003".equals(prdCode)){
									tName = "20元版";
								}else if("180004".equals(prdCode)){
									tName = "30元版";
								}
								dt.setName("交通秘书-" + tName + "(" + cardId + ")");
								dt.setReserve1(dt.getId().trim());
								dt.setBeginDate(p.matcher(csmscalldt.getChildText("trafficsecuser_begin_date")).replaceAll(""));
								dt.setEndDate(p.matcher(csmscalldt.getChildText("trafficsecuser_end_date")).replaceAll(""));
								dt.setState(getState("2"));
								dt.setReserve1(BIZTYPE_JTMS);
								reList.add(dt);
							}
							
						}
					}
				}
			}
		}

	}

	//查询IP直通车
	private void cqueryiplinear(List<RequestParameter> params, String accessId, BaseServiceInvocationResult res, List<GommonBusiness> reList) throws CommunicateException, JDOMException, Exception {
		
		String reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqueryiplinear_318", params);
//		logger.debug(" ====== 查询IP直通车发送报文 ======\n" + reqXml);
		if (null != reqXml && !"".equals(reqXml))
		{
			//发送并接收报文
			String rspXml = (String)this.remote.callRemote(
					new StringTeletext(reqXml, accessId, "cc_cqueryiplinear_318", this.generateCity(params)));
//			logger.debug(" ====== 返回IP直通车报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml) )
			{
				
				// 查询用户在用增值业务列表
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				String resp_desc = root.getChild("response").getChildText("resp_desc");
				res.setResultCode(isScuccess(resp_code) ?"0":"1");
				if (!isScuccess(resp_code))
				{
					res.setResultCode(resp_code);
					res.setErrorMessage(resp_desc);
					
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY020001", "cc_cqueryiplinear_318", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				if (isScuccess(resp_code) && root.getChild("content") != null)
				{
					
					//增值业务
					List spbizList = root.getChild("content").getChildren("payment_pstn");
					if (null != spbizList && spbizList.size() > 0)
					{
						for (int i = 0; i < spbizList.size(); i++)
						{
							Element csmscalldt = ((Element)spbizList.get(i)).getChild("cuseriplineardt");
							GommonBusiness dt = new GommonBusiness();
							String pstn = p.matcher(csmscalldt.getChildText("payment_pstn")).replaceAll("").trim();
							dt.setId("83");
							dt.setName("IP直通车(" + pstn + ")");
							dt.setReserve1(dt.getId().trim());
							dt.setBeginDate(p.matcher(csmscalldt.getChildText("payment_create_date")).replaceAll(""));
							dt.setEndDate("");
							String strState = p.matcher(csmscalldt.getChildText("iplinear_state")).replaceAll("");
							dt.setState(getState(strState));
							dt.setReserve1(BIZTYPE_IPZTC);
							reList.add(dt);
						}
						
					}
				}
			}
		}
		
	}

	//查询省内一卡多号
	private void cqrymultiinfo(List<RequestParameter> params, String accessId, BaseServiceInvocationResult res, List<GommonBusiness> reList) throws CommunicateException, JDOMException, Exception {
		
		String reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqrymultiinfo_395", params);
//		logger.debug(" ====== 查询省内一卡多号发送报文 ======\n" + reqXml);
		if (null != reqXml && !"".equals(reqXml))
		{
			//发送并接收报文
			String rspXml = (String)this.remote.callRemote(
					new StringTeletext(reqXml, accessId, "cc_cqrymultiinfo_395", this.generateCity(params)));
//			logger.debug(" ====== 返回省内一卡多号报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml) )
			{
				
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				String resp_desc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(isScuccess(resp_code) ?"0":"1");
				if (!isScuccess(resp_code))
				{
					res.setResultCode(resp_code);
					res.setErrorMessage(resp_desc);
					
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY020001", "cc_cqrymultiinfo_395", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				if (isScuccess(resp_code))
				{
					
					List spbizList = root.getChild("content").getChildren("multinumberinfo_operating_srl");
					if (null != spbizList && spbizList.size() > 0)
					{
						for (int i = 0; i < spbizList.size(); i++)
						{
							Element csmscalldt = ((Element)spbizList.get(i)).getChild("cmultinumberinfodt");
							GommonBusiness dt = new GommonBusiness();
							String vmsisdn = p.matcher(csmscalldt.getChildText("multinumberinfo_vmsisdn")).replaceAll("").trim();
							dt.setId("389");
							dt.setName("省内一卡多号(" + vmsisdn + ")");
							dt.setReserve1(dt.getId().trim());
							dt.setBeginDate(p.matcher(csmscalldt.getChildText("multinumberinfo_start_date")).replaceAll(""));
							dt.setEndDate(p.matcher(csmscalldt.getChildText("multinumberinfo_end_date")).replaceAll(""));
							String strState = "2";
							dt.setState(getState(strState));
							dt.setReserve1(BIZTYPE_SNYKDH);
							reList.add(dt);
						}
						
					}
				}
			}
		}
		
	}
	
	//查询手机游戏
	private void cgetusermobile(List<RequestParameter> params, String accessId, BaseServiceInvocationResult res, List<GommonBusiness> reList) throws CommunicateException, JDOMException, Exception {
		
		String reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetusermobile_825", params);
//		logger.debug(" ====== 查询手机游戏发送报文 ======\n" + reqXml);
		if (null != reqXml && !"".equals(reqXml))
		{
			//发送并接收报文
			String rspXml = (String)this.remote.callRemote(
					new StringTeletext(reqXml, accessId, "cc_cgetusermobile_825", this.generateCity(params)));
//			logger.debug(" ====== 返回手机游戏报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml) )
			{
				
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				String resp_desc = root.getChild("response").getChildText("resp_desc");
				res.setResultCode(isScuccess(resp_code) ?"0":"1");
				if (!isScuccess(resp_code))
				{
					res.setResultCode(resp_code);
					res.setErrorMessage(resp_desc);
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY020001", "cc_cgetusermobile_825", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				if (isScuccess(resp_code))
				{
					
					List spbizList = root.getChild("content").getChildren("mobilegameorderinfo_user_id");
					if (null != spbizList && spbizList.size() > 0)
					{
						for (int i = 0; i < spbizList.size(); i++)
						{
							Element csmscalldt = ((Element)spbizList.get(i)).getChild("cmobilegameorderinfodt");
							GommonBusiness dt = new GommonBusiness();
							String bizCode = p.matcher(csmscalldt.getChildText("mobilegameorderinfo_bizcode")).replaceAll("").trim();
							String spId = p.matcher(csmscalldt.getChildText("mobilegameorderinfo_spid")).replaceAll("").trim();
							dt.setId(spId);
							dt.setName("手机游戏-" + getMobileGameName(bizCode));
							dt.setReserve1(dt.getId().trim());
							dt.setBeginDate(p.matcher(csmscalldt.getChildText("mobilegameorderinfo_start_date")).replaceAll(""));
							dt.setEndDate(p.matcher(csmscalldt.getChildText("mobilegameorderinfo_end_date")).replaceAll(""));
							String strState = "2";
							dt.setState(getState(strState));
							dt.setReserve1(BIZTYPE_SJYX);
							reList.add(dt);
						}
						
					}
				}
			}
		}
		
	}
	
	/**
	 * 由业务ID获得手机游戏子业务名称（从网营移植）
	 * 
	 * @param bizCode
	 * @return
	 */
	private static String getMobileGameName(String bizCode)
	{
		String mobileGameName = "";
		int tempCode = Integer.parseInt(bizCode.substring(5, 9));
		switch (tempCode) 
		{
		case 544:
			mobileGameName = "g+至尊游戏包(5元/月)";
			break;
		case 1876:
			mobileGameName = "g+好又多(10元/月)";
			break;
		case 1877:
			mobileGameName = "g+尽情玩吧(5元/月)";
			break;
		case 1873:
			mobileGameName = "g+旺旺包(5元/月)";
			break;
		case 1880:
			mobileGameName = "热门游戏(5元/月)";
			break;
		case 1870:
			mobileGameName = "g+经典游戏(5元/月)";
			break;
		case 1882:
			mobileGameName = "g+游戏狂人(5元/月)";
			break;
		case 1878:
			mobileGameName = "g+嘉年华(5元/月)";
			break;
		case 1881:
			mobileGameName = "g+游戏发烧包(15元/月)";
			break;
		case 1875:
			mobileGameName = "g+i游戏(5元/月)";
			break;
		case 1871:
			mobileGameName = "g+最佳游戏(15元/月)";
			break;
		case 1868:
			mobileGameName = "g+电影大片(10元/月)";
			break;
		case 1872:
			mobileGameName = "g+热门游戏(15元/月)";
			break;
		case 1869:
			mobileGameName = "g+私藏经典(5元/月)";
			break;
		case 1879:
			mobileGameName = "g+游戏区(15元/月)";
			break;
		case 1874:
			mobileGameName = "g+暴风城(5元/月)";
			break;
		}
		return mobileGameName;
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

	private GommonBusiness copyCom(GommonBusiness obj) {
		GommonBusiness copy = new GommonBusiness();
		
		copy.setId(obj.getId());
		copy.setName(obj.getName());
		copy.setBeginDate(obj.getBeginDate());
		copy.setEndDate(obj.getEndDate());
		copy.setState(obj.getState());
		copy.setReserve1(obj.getReserve1());
		copy.setReserve2(obj.getReserve2());
		
		return copy;
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