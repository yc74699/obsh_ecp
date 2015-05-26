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
import org.jdom.input.SAXBuilder;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.IPackageChangeDAO;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.ProIncrement;
import com.xwtech.xwecp.service.logic.pojo.ProPackage;
import com.xwtech.xwecp.service.logic.pojo.ProSelf;
import com.xwtech.xwecp.service.logic.pojo.ProService;
import com.xwtech.xwecp.service.logic.pojo.QRY050017Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 在线入网 产品业务选择
 * 
 * @author 吴宗德
 *
 */
public class GetProBusinessInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(GetProBusinessInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private IPackageChangeDAO packageChangeDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	//private static final String[] ZXRW_PKG_ID = {"1591","1267","1146","1146","1147","1148","1149","1431","4991","2000001124"};//2000001124是神州行轻松卡Q套餐（月功能费13元）编码
	
	public GetProBusinessInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.packageChangeDAO = (IPackageChangeDAO) (springCtx.getBean("packageChangeDAO"));
		
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY050017Result res = new QRY050017Result();
		try
		{
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			BaseResult proBusinessList = this.getProBusinessList(accessId, config, params);
			if (LOGIC_SUCESS.equals(proBusinessList.getResultCode())) {
				Map map = (Map)proBusinessList.getReObj();
				
				if (map.get("package") != null) {
					res.setProPackages((List<ProPackage>)map.get("package"));
				}
				
				if (map.get("service") != null) {
					res.setProServices((List<ProService>)map.get("service"));
				}
				
				if (map.get("increment") != null) {
					res.setProIncrements((List<ProIncrement>)map.get("increment"));
				}
				
				if (map.get("self") != null) {
					res.setProSelfs((List<ProSelf>)map.get("self"));
				}
				
			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(proBusinessList.getErrorCode());
				res.setErrorMessage(proBusinessList.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 网营全品牌查询可用产品
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getProBusinessList(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
//		String key = "PRO_LIST_";
		Map<String, Object> retMap = new HashMap<String, Object>();
		String city  = (String) getParameters(params, "city");
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
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetprodetail_344", params);
				
				logger.debug(" ====== 查询产品详细信息请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetprodetail_344", this.generateCity(params)));
//				logger.debug(" ====== 查询产品详细信息返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY050017", "cc_cgetprodetail_344", errCode);
						if (null != errDt)
						{
							errCode = errDt.getLiErrCode();
							errDesc = errDt.getLiErrMsg();
						}
					}
					res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
						
						
						//套餐
						List productbasepackagecfg_product_id = null;
						try
						{
							productbasepackagecfg_product_id = root.getChild("content").getChildren("productbasepackagecfg_product_id");
						}
						catch (Exception e)
						{
							productbasepackagecfg_product_id = null;
						}
						if (null != productbasepackagecfg_product_id && productbasepackagecfg_product_id.size() > 0) {
							List<ProPackage> pkgList = new ArrayList<ProPackage>(productbasepackagecfg_product_id.size());
							ProPackage bean = null;
							List<RequestParameter> paramNew = copyParam(params);
							List paramlist = new ArrayList();
							List templist = new ArrayList();
							BaseResult pkgCfgList = null;
							for (int i = 0; i < productbasepackagecfg_product_id.size(); i++)
							{
								Element productbasepackagecfgdt = ((Element)productbasepackagecfg_product_id.get(i)).getChild("productbasepackagecfgdt");
								if (null != productbasepackagecfgdt)
								{
									String pkgCode = p.matcher(productbasepackagecfgdt.getChildText("productbasepackagecfg_package_code")).replaceAll("");
									//产品接口数据list
									paramlist.add(new BusinessBoss(pkgCode));
									templist.add(pkgCode);
									
								}
							}
							paramNew.add(new RequestParameter("codeCount", paramlist));
							//配置接口的套餐详细信息数据
							pkgCfgList = this.getPkgCfgList(accessId, config, paramNew);
							if (LOGIC_SUCESS.equals(pkgCfgList.getResultCode())) {
								
								List<ProductBusinessPackageBean> pkgCfg = (List<ProductBusinessPackageBean>)pkgCfgList.getReObj();
								for(int i = 0;i< templist.size();i++){
									String codeStr = ""; 
									for(ProductBusinessPackageBean cfg:pkgCfg){
										
										if(!cfg.getPackageCode().startsWith("200000")){ //配置表数据以200000开头的长编码
											codeStr = "200000";
										}else{
											codeStr = ""; 
										}
										if (templist.get(i).equals(codeStr+ cfg.getPackageCode())) {
											bean = new ProPackage();
											
											bean.setTypeId(cfg.getBusinessId());
											bean.setPkgId(cfg.getPackageCode());
											bean.setPkgName(cfg.getPackageName());
											bean.setPkgLevel(cfg.getPackageLevel());
											bean.setPkgDesc(cfg.getPackageDesc());
											bean.setTypeName("必选套餐");
											pkgList.add(bean);
										}
									}
									
								}
								
							}
							
							else {
								res.setResultCode(LOGIC_ERROR);
								res.setErrorCode(pkgCfgList.getErrorCode());
								res.setErrorMessage(pkgCfgList.getErrorMessage());
							}
							List<ProPackage> retpkgList = new ArrayList<ProPackage>();
							List<String> cityPckList = new ArrayList();
							cityPckList = packageChangeDAO.getPkgIdByCity(city);
							
							for(int i=0;i<pkgList.size();i++){
								if(this.getZXRWPkgId(pkgList.get(i).getPkgId(),cityPckList)){
									retpkgList.add(pkgList.get(i));
								}
							}
							retMap.put("package", retpkgList);
							
						}
						
						//附加功能
						List productserviceoptcfg_product_id = null;
						try
						{
							productserviceoptcfg_product_id = root.getChild("content").getChildren("productserviceoptcfg_product_id");
						}
						catch (Exception e)
						{
							productserviceoptcfg_product_id = null;
						}
						if (null != productserviceoptcfg_product_id && productserviceoptcfg_product_id.size() > 0) {
							String service_needToOpen = "/02/03/04/07/09/10/12/13/15/";	//需要开通的附加功能
							List<ProService> serviceList = new ArrayList<ProService>(productserviceoptcfg_product_id.size());
							ProService bean = null;
							for (int i = 0; i < productserviceoptcfg_product_id.size(); i++)
							{
								Element productserviceoptcfgdt = ((Element)productserviceoptcfg_product_id.get(i)).getChild("productserviceoptcfgdt");
								if (null != productserviceoptcfgdt)
								{
									String serviceId = p.matcher(productserviceoptcfgdt.getChildText("productserviceoptcfg_service_code")).replaceAll("");
				    				
									if (service_needToOpen.contains("/" + serviceId.substring(8) + "/")) {
										bean = new ProService();
										
										bean.setServiceId("21000000" + serviceId.substring(8));
										bean.setServiceName(getAdjFunDic().get(serviceId.substring(8)));
										
										serviceList.add(bean);
									}
								}
							}
							retMap.put("service", serviceList);
						}
						
						//增值业务
						List productincrementcfg_product_id = null;
						try
						{
							productincrementcfg_product_id = root.getChild("content").getChildren("productincrementcfg_product_id");
						}
						catch (Exception e)
						{
							productincrementcfg_product_id = null;
						}
						if (null != productincrementcfg_product_id && productincrementcfg_product_id.size() > 0) {
							List<ProIncrement> incrementList = new ArrayList<ProIncrement>(productincrementcfg_product_id.size());
							ProIncrement bean = null;
							for (int i = 0; i < productincrementcfg_product_id.size(); i++)
							{
								Element productincrementcfgdt = ((Element)productincrementcfg_product_id.get(i)).getChild("productincrementcfgdt");
								if (null != productincrementcfgdt)
								{
									String isShow = p.matcher(productincrementcfgdt.getChildText("productincrementcfg_is_show")).replaceAll("");
									String relationType = p.matcher(productincrementcfgdt.getChildText("productincrementcfg_relation_type")).replaceAll("");
				    				
									if ("1".equals(isShow) && "1".equals(relationType)) {
										bean = new ProIncrement();
										
										String incrementId = p.matcher(productincrementcfgdt.getChildText("productincrementcfg_increment_code")).replaceAll("");
										
										bean.setIncrementId(incrementId);
										bean.setIncrementName(getAddIncDic().get(incrementId));
										
										incrementList.add(bean);
									}
								}
							}
							retMap.put("increment", incrementList);
						}
						
						//自有业务
						List productselfplatcfg_biz_code = null;
						try
						{
							productselfplatcfg_biz_code = root.getChild("content").getChildren("productselfplatcfg_biz_code");
						}
						catch (Exception e)
						{
							productselfplatcfg_biz_code = null;
						}
						if (null != productselfplatcfg_biz_code && productselfplatcfg_biz_code.size() > 0) {
							List<ProSelf> selfList = new ArrayList<ProSelf>(productselfplatcfg_biz_code.size());
							ProSelf bean = null;
							for (int i = 0; i < productselfplatcfg_biz_code.size(); i++)
							{
								Element cproductselfplatcfgdt = ((Element)productselfplatcfg_biz_code.get(i)).getChild("cproductselfplatcfgdt");
								if (null != cproductselfplatcfgdt)
								{
									String isShow = p.matcher(cproductselfplatcfgdt.getChildText("productselfplatcfg_is_show")).replaceAll("");
									String relationType = p.matcher(cproductselfplatcfgdt.getChildText("productselfplatcfg_relation_type")).replaceAll("");
				    				
									if ("1".equals(isShow) && "1".equals(relationType)) {
										bean = new ProSelf();
										
										String selfId1 = p.matcher(cproductselfplatcfgdt.getChildText("productselfplatcfg_domain_code")).replaceAll("");
										String selfId2 = p.matcher(cproductselfplatcfgdt.getChildText("productselfplatcfg_biz_code")).replaceAll("");
										String selfId3 = p.matcher(cproductselfplatcfgdt.getChildText("productselfplatcfg_prd_code")).replaceAll("");
										
										bean.setSelfId1(selfId1);
										bean.setSelfId2(selfId2);
										bean.setSelfId3(selfId3);
										
										selfList.add(bean);
									}
								}
							}
							retMap.put("self", selfList);
						}
						
						res.setReObj(retMap);
					}
				}
//			}
//			res.setReObj(proList);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 套餐配置信息
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult getPkgCfgList(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
//		String key = "PRO_CFG_LIST_";
		List<ProductBusinessPackageBean> pkgCfgList = null;
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
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetpackbycode_605_ZXRW", params);
				
				logger.debug(" ====== 查询套餐配置请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetpackbycode_605_ZXRW", this.generateCity(params)));
				logger.debug(" ====== 查询套餐配置返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY050017", "cc_cgetpackbycode_605_ZXRW", errCode);
						if (null != errDt)
						{
							errCode = errDt.getLiErrCode();
							errDesc = errDt.getLiErrMsg();
						}
					}
					res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
						List productbusinesspackage_package_code = null;
						try
						{
							productbusinesspackage_package_code = root.getChild("content").getChildren("productbusinesspackage_package_code");
						}
						catch (Exception e)
						{
							productbusinesspackage_package_code = null;
						}
						if (null != productbusinesspackage_package_code && productbusinesspackage_package_code.size() > 0) {
							pkgCfgList = new ArrayList<ProductBusinessPackageBean>(productbusinesspackage_package_code.size());
							ProductBusinessPackageBean bean = null;
							for (int i = 0; i < productbusinesspackage_package_code.size(); i++)
							{
								bean = new ProductBusinessPackageBean();
								Element cproductbusinesspackagedt = ((Element)productbusinesspackage_package_code.get(i)).getChild("cproductbusinesspackagedt");
								if (null != cproductbusinesspackagedt)
								{
				                	bean.setBossToolFlag(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_bosstool_flag")).replaceAll(""));
				                	bean.setWebFlag(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_web_flag")).replaceAll(""));
				                	bean.setBossViewFlag(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_bossview_flag")).replaceAll(""));
				                	bean.setCityId(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_city_id")).replaceAll(""));
				                	bean.setPackageName(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_package_name")).replaceAll(""));
				                	bean.setEndDate(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_end_date")).replaceAll(""));
				                	bean.setPackageDesc(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_package_desc")).replaceAll(""));
				                	bean.setPackageClass(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_package_class")).replaceAll(""));
				                	bean.setFlag1860(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_flag_1860")).replaceAll(""));
				                	bean.setCuiFlag(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_cui_flag")).replaceAll(""));
				                	bean.setIsNowclose(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_is_nowclose")).replaceAll(""));
				                	bean.setIsNowopen(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_is_nowopen")).replaceAll(""));
				                	bean.setOpenChoice(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_open_choice")).replaceAll(""));
				                	bean.setPreChoice(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_pre_choice")).replaceAll(""));
				                	bean.setStartDate(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_start_date")).replaceAll(""));
				                	bean.setPackageLevel(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_package_level")).replaceAll(""));
				                	bean.setPackageCode(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_package_code")).replaceAll(""));
				                	bean.setBusinessId(p.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_business_id")).replaceAll(""));
				                	
				                	
				                	pkgCfgList.add(bean);
								}
							}
//							this.wellFormedDAO.getCache().add(key, proCfgList);
							res.setReObj(pkgCfgList);
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
	
	/**
	 * 查询套餐大类名称
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult getPkgTypeName(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
//		String key = "PRO_CFG_LIST_";
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
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetbizname_713_ZXRW", params);
				
				logger.debug(" ====== 查询套餐大类名称请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetbizname_713_ZXRW", this.generateCity(params)));
				logger.debug(" ====== 查询套餐大类名称返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY050017", "cc_cgetbizname_713_ZXRW", errCode);
						if (null != errDt)
						{
							errCode = errDt.getLiErrCode();
							errDesc = errDt.getLiErrMsg();
						}
					}
					res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
						Element content = root.getChild("content");
						String typeName = p.matcher(content.getChildText("biz_name")).replaceAll("");
						res.setReObj(typeName);
					}
				}
//			}
//			res.setReObj(proCfgList);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 取得可在网营入网的套餐编码
	 * @param proid
	 * @param arr
	 * @return
	 */
	private Boolean getZXRWPkgId(String pkgId, List list){

		boolean retCode = false;
		if(list != null  && list.size()>0)
		for(int i = 0; i < list.size(); i++){
			if(pkgId.equals(list.get(i))){
				retCode = true;
				break;
			}
		}
		return retCode;
	}
	
	
	/**
	 * 业务boss实现类
	 * @author 吴宗德
	 *
	 */
	public class BusinessBoss {
		private String parm1;

		public BusinessBoss(String parm1) {
			super();
			this.parm1 = parm1;
		}

		public String getParm1() {
			return parm1;
		}

		public void setParm1(String parm1) {
			this.parm1 = parm1;
		}
	}
	
	public class ProductBusinessPackageBean {
		private String bossToolFlag;
		private String webFlag;
		private String bossViewFlag;
		//地市代码
		private String cityId;
		//套餐名称
		private String packageName;
		//套餐结束时间
		private String endDate;
		//套餐描述
		private String packageDesc;
		//套餐大类
		private String packageClass;
		private String flag1860;
		private String cuiFlag;
		private String isNowclose;
		private String isNowopen;
		private String openChoice;
		private String preChoice;
		//套餐开始时间
		private String startDate;
		//套餐级别
		private String packageLevel;
		//套餐代码
		private String packageCode;
		//套餐大类
		private String businessId;
		
		//套餐分组
		private String groupId;
		
		//分组名称
		private String groupName;
		
		//分类名称
		private String businessName;

		public String getBossToolFlag() {
			return bossToolFlag;
		}

		public void setBossToolFlag(String bossToolFlag) {
			this.bossToolFlag = bossToolFlag;
		}

		public String getBossViewFlag() {
			return bossViewFlag;
		}

		public void setBossViewFlag(String bossViewFlag) {
			this.bossViewFlag = bossViewFlag;
		}

		public String getBusinessId() {
			return businessId;
		}

		public void setBusinessId(String businessId) {
			this.businessId = businessId;
		}

		public String getBusinessName() {
			return businessName;
		}

		public void setBusinessName(String businessName) {
			this.businessName = businessName;
		}

		public String getCityId() {
			return cityId;
		}

		public void setCityId(String cityId) {
			this.cityId = cityId;
		}

		public String getCuiFlag() {
			return cuiFlag;
		}

		public void setCuiFlag(String cuiFlag) {
			this.cuiFlag = cuiFlag;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public String getFlag1860() {
			return flag1860;
		}

		public void setFlag1860(String flag1860) {
			this.flag1860 = flag1860;
		}

		public String getGroupId() {
			return groupId;
		}

		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		public String getGroupName() {
			return groupName;
		}

		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}

		public String getIsNowclose() {
			return isNowclose;
		}

		public void setIsNowclose(String isNowclose) {
			this.isNowclose = isNowclose;
		}

		public String getIsNowopen() {
			return isNowopen;
		}

		public void setIsNowopen(String isNowopen) {
			this.isNowopen = isNowopen;
		}

		public String getOpenChoice() {
			return openChoice;
		}

		public void setOpenChoice(String openChoice) {
			this.openChoice = openChoice;
		}

		public String getPackageClass() {
			return packageClass;
		}

		public void setPackageClass(String packageClass) {
			this.packageClass = packageClass;
		}

		public String getPackageCode() {
			return packageCode;
		}

		public void setPackageCode(String packageCode) {
			this.packageCode = packageCode;
		}

		public String getPackageDesc() {
			return packageDesc;
		}

		public void setPackageDesc(String packageDesc) {
			this.packageDesc = packageDesc;
		}

		public String getPackageLevel() {
			return packageLevel;
		}

		public void setPackageLevel(String packageLevel) {
			this.packageLevel = packageLevel;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public String getPreChoice() {
			return preChoice;
		}

		public void setPreChoice(String preChoice) {
			this.preChoice = preChoice;
		}

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public String getWebFlag() {
			return webFlag;
		}

		public void setWebFlag(String webFlag) {
			this.webFlag = webFlag;
		}
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
	
	/** 语音套餐字典 */
	private static Map<String, String> soundPackageFilter = null;
	
	private Map<String, String> getSoundPackageMap() {
		if (soundPackageFilter == null) {
			soundPackageFilter = new HashMap<String, String>();
			//南京
			soundPackageFilter.put("14", "/1264/");
			//镇江
			soundPackageFilter.put("18", "/1207/");
			//扬州
			soundPackageFilter.put("23", "/1268/");
			//盐城
			soundPackageFilter.put("22", "/1268/");	
			//徐州
			soundPackageFilter.put("16", "/1207/");
			//无锡
			soundPackageFilter.put("19", "/1267/");
			//泰州
			soundPackageFilter.put("21", "/1268/");
			//宿迁
			soundPackageFilter.put("13", "/1268/");
			//南通
			soundPackageFilter.put("20", "/1267/");
			//连云港
			soundPackageFilter.put("15", "/1207/");
			//常州
			soundPackageFilter.put("17", "/1268/");
			//淮安
			soundPackageFilter.put("12", "/1267/");
			//苏州
			soundPackageFilter.put("11", "/1207/");
		}
		return soundPackageFilter;
	}
	
	/** 必选套餐字典 */
	private static Map<String, String> needPackageFilter = null;
	
	private Map<String, String> getNeedPackageMap() {
		if (needPackageFilter == null) {
			needPackageFilter = new HashMap<String, String>();
			//南京
			needPackageFilter.put("14", "/1431/");
			//镇江
			needPackageFilter.put("18", "/1431/");
			//扬州
			needPackageFilter.put("23", "/1146/1431/");
			//盐城
			needPackageFilter.put("22", "/1431/1146/4991/");	
			//徐州
			needPackageFilter.put("16", "/1431/4991/1146/");
			//无锡
			needPackageFilter.put("19", "/1146/");
			//泰州
			needPackageFilter.put("21", "/1147/1146/1148/1149/4991/1431/");
			//宿迁
			needPackageFilter.put("13", "/1431/");
			//南通
			needPackageFilter.put("20", "/1146/1147/1148/1149/1431/4991/");
			//连云港
			needPackageFilter.put("15", "/1146/4991/");
			//常州
			needPackageFilter.put("17", "/1146/1431/");
			//淮安
			needPackageFilter.put("12", "/4991/");
			//苏州
			needPackageFilter.put("11", "/1146/");
		}
		return needPackageFilter;
	}
	
	private static Map<String, String> mapAdjFunDic = null;
	
	/**
	 * 附加功能字典
	 * @return
	 */
	private Map<String, String> getAdjFunDic() {
		if (mapAdjFunDic == null) {
			mapAdjFunDic = new HashMap<String, String>();
			mapAdjFunDic.put("01", "台港澳国际漫游");
			mapAdjFunDic.put("02", "省际漫游");
			mapAdjFunDic.put("03", "省内漫游");
			mapAdjFunDic.put("04", "短信");
			mapAdjFunDic.put("05", "语音信箱");
			mapAdjFunDic.put("06", "传真");
			mapAdjFunDic.put("07", "数据");
			mapAdjFunDic.put("08", "台港澳国际长权");
			mapAdjFunDic.put("09", "呼叫转移");
			mapAdjFunDic.put("10", "呼叫等待");
			mapAdjFunDic.put("12", "多方通话");
			mapAdjFunDic.put("13", "来电显示");
			mapAdjFunDic.put("14", "大众卡本地通");
			mapAdjFunDic.put("15", "呼叫保持");
			mapAdjFunDic.put("18", "主叫隐藏");
		}
		return mapAdjFunDic;
	}

	private static Map<String, String> mapAddIncDic = null;
	
	/**
	 * 新业务字典
	 * @return
	 */
	private Map<String, String> getAddIncDic() {
		if (mapAddIncDic == null) {
			mapAddIncDic = new HashMap<String, String>();
			mapAddIncDic.put("5000", "短信呼");
			mapAddIncDic.put("5001", "短信话费提醒(原)");
			mapAddIncDic.put("5002", "无线终端");
			mapAddIncDic.put("5003", "盐城特预卡");
			mapAddIncDic.put("5004", "手机证券");
			mapAddIncDic.put("5005", "集团GPRS");
			mapAddIncDic.put("5006", "彩铃");
			mapAddIncDic.put("5007", "双号传真");
			mapAddIncDic.put("5008", "双号数据");
			mapAddIncDic.put("5010", "短信帐单");
			mapAddIncDic.put("5011", "纸帐单");
			mapAddIncDic.put("5012", "话费易");
			mapAddIncDic.put("5013", "动感易");
			mapAddIncDic.put("5014", "积分短信申请");
			mapAddIncDic.put("5015", "套餐易");
			mapAddIncDic.put("5016", "呼转特定号码");
			mapAddIncDic.put("5017", "生日祝福");
			mapAddIncDic.put("5018", "积分申请");
			mapAddIncDic.put("5019", "动感M值计划");
			mapAddIncDic.put("5020", "彩音");
			mapAddIncDic.put("5021", "亲情易");
			mapAddIncDic.put("5022", "被叫易");
			mapAddIncDic.put("5023", "短信回呼");
			mapAddIncDic.put("5024", "忙时通");
			mapAddIncDic.put("5025", "CMNET业务");
			mapAddIncDic.put("5026", "CMWAP业务");
			mapAddIncDic.put("5027", "梦网易");
			mapAddIncDic.put("5028", "终端管理CMDM");
			mapAddIncDic.put("5029", "彩信帐单");
			mapAddIncDic.put("5030", "来电提醒");
		}

		return mapAddIncDic;
	}
}