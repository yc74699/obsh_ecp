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
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.ExchangeBizInfo;
import com.xwtech.xwecp.service.logic.pojo.ExchangeType;
import com.xwtech.xwecp.service.logic.pojo.PackageType;
import com.xwtech.xwecp.service.logic.pojo.QRY030008Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * M值兑换新业务可兑换列表查询
 * 
 * @author 吴宗德
 *
 */
public class QueryExchangeBizListInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(QueryExchangeBizListInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public QueryExchangeBizListInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY030008Result res = new QRY030008Result();
		try
		{
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			BaseResult ruleList = this.queryMExchangeRuleList(accessId, config, params);
			if (LOGIC_SUCESS.equals(ruleList.getResultCode())) {
				List<RequestParameter> paramNew = copyParam(params);
				paramNew.add(new RequestParameter("dict_type", 839));
				BaseResult pkgType = this.getDictList(accessId, config, paramNew);
				
				if (LOGIC_SUCESS.equals(pkgType.getResultCode())) {
					List<RequestParameter> paramNew2 = copyParam(params);
					paramNew2.add(new RequestParameter("dict_type", 246));
					BaseResult exchangeType = this.getDictList(accessId, config, paramNew2);
					
					if (LOGIC_SUCESS.equals(exchangeType.getResultCode())) {
						List<MZoneExchangeRulerBean> ruleLists 	= (List<MZoneExchangeRulerBean>)ruleList.getReObj();
						List<DictBean> pkgTypes 				= (List<DictBean>)pkgType.getReObj();
						List<DictBean> exchangeTypes 			= (List<DictBean>)exchangeType.getReObj();
						
						if (ruleLists != null && ruleLists.size() > 0) {
							// 过滤无效的规则
							int size = ruleLists.size();
							MZoneExchangeRulerBean bean = null;
							for (int i = size - 1; i >= 0; i--) {
								bean = ruleLists.get(i);
								//过滤掉不是M值兑换规则的信息
								if(Integer.parseInt(bean.getConfigType()) > 100){
									ruleLists.remove(i);
								} else {	//过滤掉无效的规则，尚未开始的或者已经过期的
									
									String sysdate = DateTimeUtil.getTodayChar12();
									//当前时间早于开始时间的过滤掉
									if(sysdate.compareTo(bean.getStartDate()) < 0){
										ruleLists.remove(i);
									} else if (bean.getEndDate() != null	
											&& !"".equals(bean.getEndDate())
											&& sysdate.compareTo(bean.getEndDate()) > 0) {//当前时间晚于结束时间的过滤掉
										ruleLists.remove(i);
									}
								}
								
								//屏蔽5元免200条网内短信(4648) 和 动感地带情侣短信套餐10包600（4817）
								// 屏蔽WAP5元套餐（4870）和 20元GPRS套餐（4871）  2008/12/31 yangg add
								String subCode = bean.getSubCode();
								if("4648".equals(subCode) || "4817".equals(subCode)
										|| "4870".equals(subCode) || "4871".equals(subCode)){
									ruleLists.remove(i);
								}
							}
							
							Map<String, String> sortMap = new HashMap<String, String>();
							size = ruleLists.size();
							for (int i = 0; i < ruleLists.size(); i++) {
								if(!sortMap.containsKey(ruleLists.get(i).getConfigType())){
									sortMap.put(ruleLists.get(i).getConfigType(), "");
								}
							}
							//过滤M值兑换新业务类型
							if(exchangeTypes != null && exchangeTypes.size() > 0){
								size = exchangeTypes.size();
								for (int i = size - 1; i >= 0; i--) {
									if(!sortMap.containsKey(exchangeTypes.get(i).getDictCode())){
										exchangeTypes.remove(i);
									}
								}
							}
						}
						
						if (pkgTypes != null && pkgTypes.size() > 0) {
							//屏蔽定向漫游套餐   2008/12/31 yangg add
							for (int i = pkgTypes.size()-1; i >=0; i--){
								if("101302".equals(pkgTypes.get(i).getDictCode())){
									pkgTypes.remove(i);
								}
								
							}
						}
						
						List<ExchangeType> exchangeTypesRet = null;
						List<PackageType> packageTypes = null;
						List<ExchangeBizInfo> exchangeBizInfos = null;
						
						if (exchangeTypes != null && exchangeTypes.size() > 0) {
							exchangeTypesRet = new ArrayList<ExchangeType>(exchangeTypes.size());
							ExchangeType bean = null;
							for (DictBean obj:exchangeTypes) {
								bean = new ExchangeType();
								
								bean.setTypeId(obj.getDictCode());
								bean.setTypeName(obj.getDictCodeDesc());
								
								exchangeTypesRet.add(bean);
							}
							res.setExchangeTypes(exchangeTypesRet);
						}
						
						if (pkgTypes != null && pkgTypes.size() > 0) {
							packageTypes = new ArrayList<PackageType>(pkgTypes.size());
							PackageType bean = null;
							for (DictBean obj:pkgTypes) {
								bean = new PackageType();
								
								bean.setTypeId(obj.getDictCode());
								bean.setTypeName(obj.getDictCodeDesc());
								
								packageTypes.add(bean);
							}
							res.setPackageTypes(packageTypes);
						}
						
						if (ruleLists != null && ruleLists.size() > 0) {
							exchangeBizInfos = new ArrayList<ExchangeBizInfo>(ruleLists.size());
							ExchangeBizInfo bean = null;
							for (MZoneExchangeRulerBean obj:ruleLists) {
								bean = new ExchangeBizInfo();
								
								bean.setExchangeTypeId(obj.getConfigType());
								bean.setPackageTypeId(obj.getPackageKind());
								if("5000".equals(obj.getSubCode())){ //短信呼长短编码转换
									bean.setExchangeCode("220000" + obj.getSubCode());	
								}else{
									bean.setExchangeCode(obj.getSubCode());
								}
								bean.setExchangeName(obj.getDealName());
								bean.setScores(Integer.parseInt(obj.getDeduceNumber()));
								
								exchangeBizInfos.add(bean);
							}
							res.setExchangeBizInfos(exchangeBizInfos);
						}
						
					} else {
						res.setResultCode(LOGIC_ERROR);
						res.setErrorCode(exchangeType.getErrorCode());
						res.setErrorMessage(exchangeType.getErrorMessage());
					}
				} else {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(pkgType.getErrorCode());
					res.setErrorMessage(pkgType.getErrorMessage());
				}
			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(ruleList.getErrorCode());
				res.setErrorMessage(ruleList.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * M值兑换规则查询
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult queryMExchangeRuleList(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
//		String key = "PRO_LIST_";
		List<MZoneExchangeRulerBean> ruleList = null;
		
		try {
//			for (RequestParameter param:params) {
//				if ("city".equals(param.getParameterName())) {
//					key += param.getParameterValue();
//				}
//			}
//			
//			Object obj = this.wellFormedDAO.getCache().get(key);
//			if(obj != null && obj instanceof List)
//			{
//				proList = (List<CWebProductOpenCfgDtBean>)obj;
//			} else {
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqryzonem_qry_534", params);
				
				logger.debug(" ====== M值兑换规则查询请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cqryzonem_qry_534", this.generateCity(params)));
				logger.debug(" ====== M值兑换规则查询返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY030008", "cc_cqryzonem_qry_534", errCode);
						if (null != errDt)
						{
							errCode = errDt.getLiErrCode();
							errDesc = errDt.getLiErrMsg();
						}
						res.setErrorCode(errCode);
						res.setErrorMessage(errDesc);
						
					}
					if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
						Element content = root.getChild("content");
						String tableList = p.matcher(content.getChildText("XTABLE_ZONE_CFG")).replaceAll("");
						
						String [] rows = tableList.split(";");
						ruleList = new ArrayList<MZoneExchangeRulerBean>(rows.length - 1);
						MZoneExchangeRulerBean bean = null;
						for (int i = 1; i < rows.length; i ++) {
							String [] cells = rows[i].split("~");
							
//							rulerBean.setCityId(getString(map, "地市"));
//	            			rulerBean.setConfigData(getString(map, "新业务类型配置信息"));
//	            			rulerBean.setConfigType(getString(map, "新业务类型"));
//	            			rulerBean.setDealName(getString(map, "名称"));
//	            			rulerBean.setDeduceNumber(getString(map, "每月扣减数"));
//	            			rulerBean.setEndDate(getString(map, "结束时间"));
//	            			rulerBean.setStartDate(getString(map, "开始时间"));
//	            			rulerBean.setSubCode(getString(map, "小类"));
//	            			rulerBean.setPackageKind(getString(map, "兑换套餐大类"));
							
							
							//新业务类型~小类~名称~每月扣减数~地市~开始时间~结束时间~兑换套餐大类~到期处理方式~;
							
							
							bean = new MZoneExchangeRulerBean();
							
							bean.setCityId(cells[4]);
//							bean.setConfigData(cells[1]);
							bean.setConfigType(cells[0]);
							bean.setDealName(cells[2]);
							bean.setDeduceNumber(cells[3]);
							bean.setEndDate(cells[6]);
							bean.setStartDate(cells[5]);
							bean.setSubCode(cells[1]);
							bean.setPackageKind(cells[7]);
							
							ruleList.add(bean);
						}
					}
				}
				res.setReObj(ruleList);
//			}
//			res.setReObj(proList);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 查询字典信息
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult getDictList(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
//		String key = "PRO_CFG_LIST_";
		List<DictBean> dictList = null;
		try {
//			for (RequestParameter param:params) {
//				if ("city".equals(param.getParameterName())) {
//					key += param.getParameterValue();
//				}
//			}
//			
//			Object obj = this.wellFormedDAO.getCache().get(key);
//			if(obj != null && obj instanceof List)
//			{
//				proCfgList = (List<CcCGetProByCityBean>)obj;
//			} else {
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_get_dictlist_78", params);
				
				logger.debug(" ====== M值兑换新业务可兑换列表查询 字典查询请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_get_dictlist_78", this.generateCity(params)));
				logger.debug(" ====== M值兑换新业务可兑换列表查询 字典查询返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY030008", "cc_get_dictlist_78", errCode);
						if (null != errDt)
						{
							errCode = errDt.getLiErrCode();
							errDesc = errDt.getLiErrMsg();
						}
						res.setErrorCode(errCode);
						res.setErrorMessage(errDesc);
						
					}
					if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
						List list_size = null;
						try
						{
							list_size = root.getChild("content").getChildren("list_size");
						}
						catch (Exception e)
						{
							list_size = null;
						}
						if (null != list_size && list_size.size() > 0) {
							dictList = new ArrayList<DictBean>(list_size.size());
							DictBean bean = null;
							for (int i = 0; i < list_size.size(); i++)
							{
								Element ele = (Element)list_size.get(i);
								if (null != ele)
								{
									bean = new DictBean();
									
				    				bean.setDictCode(p.matcher(ele.getChildText("dict_code")).replaceAll(""));
				    				bean.setDictCodeDesc(p.matcher(ele.getChildText("dict_code_desc")).replaceAll(""));
				    				
				    				dictList.add(bean);
								}
							}
//							this.wellFormedDAO.getCache().add(key, proCfgList);
							res.setReObj(dictList);
						}
					}
				}
//			}
//			res.setReObj(proCfgList);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	public class DictBean {
		private String dictCode;

		private String dictCodeDesc;

		public String getDictCode() {
			return dictCode;
		}

		public void setDictCode(String dictCode) {
			this.dictCode = dictCode;
		}

		public String getDictCodeDesc() {
			return dictCodeDesc;
		}

		public void setDictCodeDesc(String dictCodeDesc) {
			this.dictCodeDesc = dictCodeDesc;
		}
	}
	
	public class MZoneExchangeRulerBean {
		// 新业务类型配置信息
		private String configData;
		// 新业务类型
		private String configType;
		// 新业务类型下的小类别
		private String subCode;
		// 名称
		private String dealName;
		// 每月扣减数
		private String deduceNumber;
		// 地市
		private String cityId;
		// 开始时间
		private String startDate;
		// 结束时间
		private String endDate;
		//套餐类别
		private String packageKind;

		public String getConfigData() {
			return configData;
		}

		public void setConfigData(String configData) {
			this.configData = configData;
		}

		public String getConfigType() {
			return configType;
		}

		public void setConfigType(String configType) {
			this.configType = configType;
		}

		public String getSubCode() {
			return subCode;
		}

		public void setSubCode(String subCode) {
			this.subCode = subCode;
		}

		public String getDealName() {
			return dealName;
		}

		public void setDealName(String dealName) {
			this.dealName = dealName;
		}

		public String getDeduceNumber() {
			return deduceNumber;
		}

		public void setDeduceNumber(String deduceNumber) {
			this.deduceNumber = deduceNumber;
		}

		public String getCityId() {
			return cityId;
		}

		public void setCityId(String cityId) {
			this.cityId = cityId;
		}

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public String getPackageKind() {
			return packageKind;
		}

		public void setPackageKind(String packageKind) {
			this.packageKind = packageKind;
		}
	}
}