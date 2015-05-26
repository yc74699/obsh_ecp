package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.Monternet;
import com.xwtech.xwecp.service.logic.pojo.QRY050002Result;
import com.xwtech.xwecp.service.logic.pojo.SelfBusiness;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.TeletextParseUtils;

public class QrySpuserRegMoInvocation extends BaseInvocation implements ILogicalService
{
private static final Logger logger = Logger.getLogger(PayByCardInvocation.class);

	/** 号簿管家对应的业务类型 */
	private static final String PIM_BIZ_TYPE = "10";

	/** 139邮箱业务对应的业务类型 */
	private static final String EMAIL_BIZ_TYPE = "16";
	
	/** 139邮箱业务对应的企业代码 */
	private static final String EMAIL_SP_ID = "910194";

	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	public QrySpuserRegMoInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId, 
			                ServiceConfig config, List<RequestParameter> params)
	{
		QRY050002Result ret = new QRY050002Result();
		String rsp = "";
		String resp_desc = "";
		String resp_code = "";
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Monternet monternet = null;
		List<RequestParameter> dictList = null;
		RequestParameter req = null;
		List<Monternet> reList = new ArrayList();
		ErrorMapping errDt = null;
		
		try
		{
			//用户订购关系查询
			rsp = (String)this.remote.callRemote(
				     new StringTeletext(this.bossTeletextUtil.
				     mergeTeletext("cc_cqryspuserreg_65_SJB", params), 
				     accessId, "cc_cqryspuserreg_65_SJB", this.generateCity(params)));
			if (null != rsp && !"".equals(rsp))
			{
				Element root = this.getElement(rsp.getBytes());
				if (null != root)
				{
					resp_code = p.matcher(root.getChild("response").getChildText("resp_code")).replaceAll("");
					resp_desc = root.getChild("response").getChildText("resp_desc");
					ret.setResultCode("0000".equals(resp_code)?"0":"1");
					if (!"0000".equals(resp_code))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY050002", "cc_cqryspuserreg_65_SJB", resp_code);
						if (null != errDt)
						{
							ret.setErrorCode(errDt.getLiErrCode());
							ret.setErrorMessage(errDt.getLiErrMsg());
						}
					}
					/*ret.setResultCode(resp_code);
					ret.setErrorCode(resp_code);
					ret.setErrorMessage(resp_desc);*/
				}
				if (null != resp_code && ( "0".equals(resp_code) || "0000".equals(resp_code)) )
				{
					//组装发送报文  字典表查询 关键字：221
					RequestParameter par = new RequestParameter();
					par.setParameterName("dict_type");
					par.setParameterValue("221");
					params.add(par);
					String reqXml = this.bossTeletextUtil.mergeTeletext("cc_get_dictlist_78", params);
					String rspXml = (String)this.remote.callRemote(
							 new StringTeletext(reqXml, accessId, "cc_get_dictlist_78", this.generateCity(params)));
					
					Element dictRoot = this.getElement(rspXml.getBytes());
					if (null != dictRoot)
					{
						String dict_resp_code = p.matcher(dictRoot.getChild("response").getChildText("resp_code")).replaceAll("");
						ret.setResultCode("0000".equals(dict_resp_code)?"0":"1");
						if (!"0000".equals(dict_resp_code))
						{
							errDt = this.wellFormedDAO.transBossErrCode("QRY050002", "cc_get_dictlist_78", dict_resp_code);
							if (null != errDt)
							{
								ret.setErrorCode(errDt.getLiErrCode());
								ret.setErrorMessage(errDt.getLiErrMsg());
							}
						}
						
						if (null != dict_resp_code && "0000".equals(dict_resp_code))
						{
							dictList = new ArrayList();
							List sizeList = dictRoot.getChild("content").getChildren("list_size");
							
							if (null != sizeList && sizeList.size() > 0)
							{
								for (int i = 0; i < sizeList.size(); i++)
								{
									req = new RequestParameter();
									req.setParameterName(((Element)sizeList.get(i)).getChildText("dict_code").trim());
									req.setParameterValue(((Element)sizeList.get(i)).getChildText("dict_code_desc").trim());
									dictList.add(req);
								}
							}
						}
					}
					
					List gsmList = root.getChild("content").getChildren("spbizreg_gsm_user_id");
					if (null != gsmList && gsmList.size() > 0)
					{
						for (int i = 0; i < gsmList.size(); i++)
						{
							Element cspbizregdt = ((Element)gsmList.get(i)).getChild("cspbizregdt");
							monternet = new Monternet();
							monternet.setBizCode(p.matcher(cspbizregdt.getChildText("spbizreg_sub_biz_val")).replaceAll(""));
							monternet.setBizName(p.matcher(cspbizregdt.getChildText("spbizreg_busi_name")).replaceAll(""));
							monternet.setBizType(p.matcher(cspbizregdt.getChildText("spbizreg_biz_type")).replaceAll(""));
							monternet.setBizDesc(p.matcher(cspbizregdt.getChildText("spbizreg_biz_desc")).replaceAll(""));
							monternet.setSpId(p.matcher(cspbizregdt.getChildText("spbizreg_sub_biz_type")).replaceAll(""));
							monternet.setSpServiceId(p.matcher(cspbizregdt.getChildText("spbizreg_sp_svc_id")).replaceAll(""));
							monternet.setSpShortName(p.matcher(cspbizregdt.getChildText("spbizreg_sp_name")).replaceAll(""));
							if(monternet.getSpShortName().indexOf("中国移动") != -1){
								monternet.setSpShortName(monternet.getSpShortName().substring(0,4));
							}
							monternet.setPrice(
									String.valueOf(Integer.parseInt(
											p.matcher(cspbizregdt.getChildText("spbizreg_price")).replaceAll("").trim())/10));  //单位：分
							monternet.setTimeOpened(p.matcher(cspbizregdt.getChildText("spbizreg_effect_time")).replaceAll(""));
							monternet.setStatus(p.matcher(cspbizregdt.getChildText("spbizreg_status")).replaceAll(""));
							monternet.setBillType(p.matcher(cspbizregdt.getChildText("spbizreg_fee_type")).replaceAll(""));
							monternet.setBizTypeName("暂无");
							if (null != dictList && dictList.size() > 0)
							{
								for (RequestParameter dt : dictList)
								{
									if (dt.getParameterName().equals(monternet.getBizType()))
									{
										monternet.setBizTypeName(String.valueOf(dt.getParameterValue()));
									}
								}
							}
							reList.add(monternet);
						}
					}
					
					String emailInfo = "";
					List emailList = root.getChild("content").getChildren("emailuserattach_user_id");
					if (null != emailList && emailList.size() > 0)
					{
//						for (int i = 0; i < emailList.size(); i++)
//						{
							Element cemailuserattachdt = ((Element)emailList.get(emailList.size()-1)).getChild("cemailuserattachdt");
							emailInfo = (p.matcher(cemailuserattachdt.getChildText("emailuserattach_info_value")).replaceAll(""));
//							break;
//						}
					}
					
					List regList = root.getChild("content").getChildren("spuserreg_gsm_user_id");
					if (null != regList && regList.size() > 0)
					{
						List<Monternet> tList =  new ArrayList<Monternet>();
						for (int i = 0; i < regList.size(); i++)
						{
							Element cspuserregdt = ((Element)regList.get(i)).getChild("cspuserregdt");
							monternet = new Monternet();
							monternet.setTimeOpened(p.matcher(cspuserregdt.getChildText("spuserreg_reg_time")).replaceAll(""));
							String bizType = p.matcher(cspuserregdt.getChildText("spuserreg_biz_type")).replaceAll("");
							// 号簿管家
							if (PIM_BIZ_TYPE.equals(bizType)) {
								monternet.setBizCode("HBGJ");
								monternet.setPrice("0");
							} else {
								monternet.setPrice("0");
							}
							//139邮箱
							if(EMAIL_BIZ_TYPE.equals(bizType)){
								if (!emailInfo.equals("")) {
									if ("+MAILBZ".equals(emailInfo)) {
										monternet.setPrice("500");
										monternet.setBizCode("139YX_5Y");
									} else if ("+MAILVIP".equals(emailInfo)) {
										monternet.setPrice("2000");
										monternet.setBizCode("139YX_20Y");
									} else {
										monternet.setPrice("0");
										monternet.setBizCode("139YX_MFSJYX");
									}
									
								}
							}
							monternet.setSpShortName("中国移动");
							monternet.setBizDesc("中国移动");
							monternet.setBizType(bizType);
							if (null != dictList && dictList.size() > 0)
							{
								for (RequestParameter dt : dictList)
								{
									if (dt.getParameterName().equals(monternet.getBizType()))
									{
										monternet.setBizName(String.valueOf(dt.getParameterValue()));
									}
								}
							}
							//TODO ddd
							tList.add(monternet);
						}
						reList.addAll(0, tList);
					}
					
				}
			}
			ret.setMonternet(reList);
			
			//自有业务配置信息
			Map<String, String> spDicMapName = new HashMap<String, String>();
			Map<String, String> spDicMapPrice = new HashMap<String, String>();
			Map<String, String> spDicMapFun = new HashMap<String, String>();
			Map<String, String> spDicMapTag = new HashMap<String, String>();
			rsp = (String)this.remote.callRemote(
				     new StringTeletext(this.bossTeletextUtil.
				     mergeTeletext("cc_cgetspbizdetail_528", params), 
				     accessId, "cc_cgetspbizdetail_528", this.generateCity(params)));
//			rsp = FileUtils.readFileToString(new File("e:/Noname1.txt"));
			if(null != rsp && !"".equals(rsp)){
				Element root = this.getElement(rsp.getBytes());
				String resp_code2 = root.getChild("response").getChildText("resp_code");
				if(isScuccess(resp_code2)){
					String retContent = root.getChild("content").getChildText("XTABLE_SPPRD");
					List<Map<String,String>> respList = TeletextParseUtils.parseXTABLE(retContent);
					if(respList != null)
					{
						for(Map<String,String> m : respList){
							spDicMapName.put(m.get("业务编码")+"_"+m.get("产品编码"), m.get("产品名称"));	
							spDicMapPrice.put(m.get("业务编码")+"_"+m.get("产品编码"), m.get("月固定收费金额"));	
						}
					}
					String retContent2 = root.getChild("content").getChildText("XTABLE_SPBIZ");
					List<Map<String,String>> respList2 = TeletextParseUtils.parseXTABLE(retContent2);
					if(respList2 != null)
					{
						for(Map<String,String> m : respList2){
							spDicMapFun.put(m.get("平台编码")+"_"+m.get("业务编码"), m.get("业务名称"));	
							spDicMapTag.put(m.get("平台编码")+"_"+m.get("业务编码"), m.get("梦网统一查询退订界面是否展现标志"));	
						}
					}
				}
			}
			
			
			List<SelfBusiness> reSelfList = new ArrayList<SelfBusiness>();
			//查询自有业务 
			rsp = (String)this.remote.callRemote(
				     new StringTeletext(this.bossTeletextUtil.
				     mergeTeletext("cc_cgetspuserinfo_358", params), 
				     accessId, "cc_cgetspuserinfo_358", this.generateCity(params)));
			if (null != rsp && !"".equals(rsp))
			{
				Element root = this.getElement(rsp.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY050002", "cc_cgetspuserinfo_358", errCode);
					if (null != errDt)
					{
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				ret.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				ret.setErrorCode(errCode);
				ret.setErrorMessage(errDesc);
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					List selfList = root.getChild("content").getChildren("selfplatuserreg_reg_id");
					if (null != selfList && selfList.size() > 0)
					{
						for (int i = 0; i < selfList.size(); i++)
						{
							Element cselfplatuserregdt = ((Element)selfList.get(i)).getChild("cselfplatuserregdt");
							String prdCode = p.matcher(cselfplatuserregdt.getChildText("selfplatuserreg_prd_code")).replaceAll("");
							String bizCode = p.matcher(cselfplatuserregdt.getChildText("selfplatuserreg_biz_code")).replaceAll("");
							String domainCode = p.matcher(cselfplatuserregdt.getChildText("selfplatuserreg_domain_code")).replaceAll("");
							if("1".equals(spDicMapTag.get(domainCode+"_"+prdCode))){
								SelfBusiness selfBusiness = new SelfBusiness();
								selfBusiness.setPrdCode(prdCode);
								selfBusiness.setBizCode(bizCode);
								selfBusiness.setDomailCode(domainCode);
								selfBusiness.setTimeOpened(p.matcher(cselfplatuserregdt.getChildText("selfplatuserreg_begin_date")).replaceAll(""));
								selfBusiness.setBizName(spDicMapName.get(prdCode+"_"+bizCode));
								selfBusiness.setPrice(spDicMapPrice.get(prdCode+"_"+bizCode));
//								selfBusiness.setBizType(spDicMapFun.get(domainCode+"_"+prdCode));
								selfBusiness.setSpShortName("中国移动");
								selfBusiness.setBizDesc("中国移动");
								reSelfList.add(selfBusiness);
							}
						}
					}
				}
			}
			ret.setSelfBusiness(reSelfList);
			
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		return ret;
	}
	
	private boolean isScuccess(String resp_code){
		if("0000".equals(resp_code) || "-10000".equals(resp_code))
			return true;
		else
			return false;
		
	}
	
}
