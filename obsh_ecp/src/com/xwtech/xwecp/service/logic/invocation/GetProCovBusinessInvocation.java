package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.xwtech.xwecp.service.ServiceExecutor;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.ProIncrement;
import com.xwtech.xwecp.service.logic.pojo.ProPackage;
import com.xwtech.xwecp.service.logic.pojo.ProSelf;
import com.xwtech.xwecp.service.logic.pojo.ProService;
import com.xwtech.xwecp.service.logic.pojo.QRY050034Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.TeletextParseUtils;

/**
 * 产品互转 产品业务选择  
 * 
 * @author 吴宗德
 *
 */
public class GetProCovBusinessInvocation extends BaseInvocation implements ILogicalService 
{
	private static final Logger logger = Logger.getLogger(GetProCovBusinessInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	/**
		全球通98套餐		1741
		全球通138套餐	1742
		全球通188套餐B	1743
		全球通288套餐B	1744
		全球通388套餐B	1745
		全球通588套餐B	1746
		全球通888套餐B	1747
		全球通1688套餐B	1748
		95元包200元		4957
		165元包400元		4958
		310元包800元		4959
		530元包1500元	4960
		全球通商旅卡80	4908
		全球通商旅卡150	4909
		全球通商旅卡298	4910
		全球通商旅卡550	4911
		全球通商旅卡120	5551
		20元包400条		1146
		30元包600条		1147
		40元包800条		1148
		50元包1000条		1149
		动感音乐套餐		4991

	 */
	private static final String[] pakCodes = {"1741","1742","1743","1744","1745","1746","1747","1748",
		"4957","4958","4959","4960","4908","4909","4910","4911","5551","1146","1147","1148","1149","4991" };
	
	public GetProCovBusinessInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	/**
	 * 对新套餐全球通(预付费)100063,全球通商旅卡(预付费)100050,动感任我行2  100070,动感任我行3   100089等4种产品的必选套餐进行过滤
	 * @param canChoose
	 * @return
	 */
	private List<ProPackage> filterPakCode(List<ProPackage> canChoose,String proId)
	{
		
		List<ProPackage> filterList = new ArrayList<ProPackage>();
		
		if(canChoose!=null && canChoose.size()>0)
		{
			if("100063".equals(proId) || "100050".equals(proId)|| "100070".equals(proId)|| "100089".equals(proId)){
				Iterator it = canChoose.iterator();
				while(it.hasNext())
				{
					ProPackage packageBean = (ProPackage)it.next();
					for(int i=0;i<pakCodes.length;i++)
					{
						if(pakCodes[i].equals(packageBean.getPkgId()))
						{
							filterList.add(packageBean);
							continue;
						}
					}
				}
			}else{
				return canChoose;
			}
		}
		
		return filterList;
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY050034Result res = new QRY050034Result();
		try
		{
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			//获取产品编码
			RequestParameter proIdParam = this.getParameter(params, "proId");
			String proId = proIdParam != null ? proIdParam.getParameterValue().toString() : "";
			
			BaseResult proBusinessList = this.getProBusinessList(accessId, config, params);
			if (LOGIC_SUCESS.equals(proBusinessList.getResultCode())) {
				Map map = (Map)proBusinessList.getReObj();
				
				if (map.get("package") != null) {
					//对必选套餐进行过滤
					res.setProPackages(filterPakCode((List<ProPackage>)map.get("package"),proId));
					
				}
			
				if (map.get("otherPackage") != null) {
					res.setOtherPackages((List<ProPackage>)map.get("otherPackage"));
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
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		try {
//			String city = "";
//			for (RequestParameter param:params) {
//				if ("context_ddr_city".equals(param.getParameterName())) {
//					city = (String)param.getParameterValue();
//				}
//			}
//			
//			List<RequestParameter> paramsNew = copyParam(params);
//			paramsNew.add(new RequestParameter("city", city));
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetprodetail_344_CPHZ", params);

			logger.debug(" ====== 查询产品详细信息请求报文 ======\n" + reqXml);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetprodetail_344_CPHZ", this.generateCity(params)));
//			logger.debug(" ====== 查询产品详细信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY050034", "cc_cgetprodetail_344_CPHZ", errCode);
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
						for (int i = 0; i < productbasepackagecfg_product_id.size(); i++)
						{
							Element productbasepackagecfgdt = ((Element)productbasepackagecfg_product_id.get(i)).getChild("productbasepackagecfgdt");
							if (null != productbasepackagecfgdt)
							{
								String pkgCode = p.matcher(productbasepackagecfgdt.getChildText("productbasepackagecfg_package_code")).replaceAll("");
								String freezePeriod = p.matcher(productbasepackagecfgdt.getChildText("productbasepackagecfg_freeze_period")).replaceAll("");
								String packId = p.matcher(productbasepackagecfgdt.getChildText("productbasepackagecfg_pack_id")).replaceAll("");
								
								List<RequestParameter> paramNew = copyParam(params);
								List list = new ArrayList();
								list.add(new BusinessBoss(pkgCode));
								paramNew.add(new RequestParameter("codeCount", list));

								BaseResult pkgCfgList = this.getPkgCfgList(accessId, config, paramNew);

								if (LOGIC_SUCESS.equals(pkgCfgList.getResultCode())) {
									List<ProductBusinessPackageBean> pkgCfg = (List<ProductBusinessPackageBean>)pkgCfgList.getReObj();

									if (pkgCfg != null) {
										for (ProductBusinessPackageBean cfg:pkgCfg) {
											if (pkgCode.equals("200000" + cfg.getPackageCode()) && !pkgCode.equals("2735")) { //屏蔽 2735 150条30M流量的套餐，暂时，需改成可配置化
												bean = new ProPackage();

												bean.setTypeId(cfg.getBusinessId());
												bean.setPkgId(cfg.getPackageCode());
												bean.setPkgName(cfg.getPackageName());
												bean.setPkgLevel(cfg.getPackageLevel());
												bean.setPkgDesc(cfg.getPackageDesc());
												bean.setPackId(packId);
												//bean.setFreezePeriod(Integer.parseInt(freezePeriod));

												pkgList.add(bean);
												break;
											}
										}
									}
								} else {
									res.setResultCode(LOGIC_ERROR);
									res.setErrorCode(pkgCfgList.getErrorCode());
									res.setErrorMessage(pkgCfgList.getErrorMessage());
								}
							}
						}

						//构造套餐大类名称
						if (pkgList != null && pkgList.size() > 0) {
							for (ProPackage pkg:pkgList) {
								List<RequestParameter> paramNew = copyParam(params);
								paramNew.add(new RequestParameter("biz_id", pkg.getTypeId()));

								BaseResult typeName = this.getPkgTypeName(accessId, config, paramNew);

								if (LOGIC_SUCESS.equals(typeName.getResultCode())) {
									pkg.setTypeName((String)typeName.getReObj());
								} else {
									res.setResultCode(LOGIC_ERROR);
									res.setErrorCode(typeName.getErrorCode());
									res.setErrorMessage(typeName.getErrorMessage());
								}
							}
						}
						
						//88系列套餐过滤
						BaseResult pkg88ListRet = this.getPkg88List(accessId, config, params);
						if (LOGIC_SUCESS.equals(pkg88ListRet.getResultCode())) {
							String usingPckOldCode = "";
							List<PackageInfoBean> pkg88List = (List<PackageInfoBean>)pkg88ListRet.getReObj();
							if (pkg88List != null && pkg88List.size() > 0) {
								usingPckOldCode = pkg88List.get(0).getPackageCode();
							}
							
							//95系套餐
							String[] arr95Package = {"4957","4958","4959","4960"};
							//老奥运88系套餐
							String[] arrOld88Package = {"1340","1342","1344","1346","1347","1348","1349","1350","1740"};
							//新88套餐
							String[] arrNew88Package = {"1741","1742","1743","1744","1745","1746","1747","1748"};
							
							if (pkgList != null && pkgList.size() > 0) {
								Iterator it = pkgList.iterator();
								ProPackage pkg = null;
								while (it.hasNext()) {
									pkg = (ProPackage) it.next();
									
									//新产品套餐有老88套餐，全部不显示
									if(isSpecilPackage(pkg.getPkgId(), arrOld88Package)){
										it.remove();
									}
									//如果客户原来的套餐是“奥运88系列套餐”，客户选择的新产品包含“95系列套餐”和“奥运88系列套餐”，则屏蔽“95系列套餐”
									if(isSpecilPackage(usingPckOldCode,arrOld88Package)){
										if(isSpecilPackage(pkg.getPkgId(),arr95Package) 
												&& (isSpecilPackage(pkg.getPkgId(), arrOld88Package) 
														|| isSpecilPackage(pkg.getPkgId(), arrNew88Package)))
											it.remove();
									}
								}
							}
						} else {
							res.setResultCode(LOGIC_ERROR);
							res.setErrorCode(pkg88ListRet.getErrorCode());
							res.setErrorMessage(pkg88ListRet.getErrorMessage());
						}

						retMap.put("package", pkgList);
					}
					
					//其它套餐
					List productotherpackagecfg_product_id = null;
					try
					{
						productotherpackagecfg_product_id = root.getChild("content").getChildren("productotherpackagecfg_product_id");
					}
					catch (Exception e)
					{
						productotherpackagecfg_product_id = null;
					}
					
					if (null != productotherpackagecfg_product_id && productotherpackagecfg_product_id.size() > 0) {
						List<ProPackage> otherPkgList = new ArrayList<ProPackage>(productotherpackagecfg_product_id.size());
						ProPackage bean = null;
						for (int i = 0; i < productotherpackagecfg_product_id.size(); i++)
						{
							Element productotherpackagecfgdt = ((Element)productotherpackagecfg_product_id.get(i)).getChild("productotherpackagecfgdt");
							if (null != productotherpackagecfgdt)
							{
								bean = new ProPackage();
								
								bean.setTypeId(p.matcher(productotherpackagecfgdt.getChildText("productotherpackagecfg_business_id")).replaceAll(""));
								bean.setPkgId(p.matcher(productotherpackagecfgdt.getChildText("productotherpackagecfg_package_code")).replaceAll(""));
								
								otherPkgList.add(bean);
							}
						}
						retMap.put("otherPackage", otherPkgList);
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
						List<ProService> serviceList = new ArrayList<ProService>(productserviceoptcfg_product_id.size());
						ProService bean = null;
						for (int i = 0; i < productserviceoptcfg_product_id.size(); i++)
						{
							Element productserviceoptcfgdt = ((Element)productserviceoptcfg_product_id.get(i)).getChild("productserviceoptcfgdt");
							if (null != productserviceoptcfgdt)
							{
								String isShow = p.matcher(productserviceoptcfgdt.getChildText("productserviceoptcfg_is_show")).replaceAll("");
								String serviceId = p.matcher(productserviceoptcfgdt.getChildText("productserviceoptcfg_service_code")).replaceAll("");

								if ("1".equals(isShow)) {
									bean = new ProService();

									bean.setServiceId(serviceId);
									bean.setServiceName(getAdjFunDic().get(serviceId));
									
									String isDefaultOpen = p.matcher(productserviceoptcfgdt.getChildText("productserviceoptcfg_is_default_open")).replaceAll("");
									String relationType = p.matcher(productserviceoptcfgdt.getChildText("productserviceoptcfg_relation_type")).replaceAll("");
									String experiencePeriod = p.matcher(productserviceoptcfgdt.getChildText("productserviceoptcfg_experience_period")).replaceAll("");
									String isExperience = p.matcher(productserviceoptcfgdt.getChildText("productserviceoptcfg_is_experience")).replaceAll("");
									String freezePeriod = p.matcher(productserviceoptcfgdt.getChildText("productserviceoptcfg_freeze_period")).replaceAll("");
									
									//bean.setIsDefaultOpen(Integer.parseInt(isDefaultOpen));
//									bean.setRelationType(Integer.parseInt(relationType));
//									bean.setExperiencePeriod(Integer.parseInt(experiencePeriod));
//									bean.setIsExperience(Integer.parseInt(isExperience));
//									bean.setFreezePeriod(Integer.parseInt(freezePeriod));

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

								//if ("1".equals(isShow)) {
									bean = new ProIncrement();

									String incrementId = p.matcher(productincrementcfgdt.getChildText("productincrementcfg_increment_code")).replaceAll("");

									bean.setIncrementId(incrementId);
									bean.setIncrementName(getAddIncDic().get(incrementId));
									
									String isDefaultOpen = p.matcher(productincrementcfgdt.getChildText("productincrementcfg_is_default_open")).replaceAll("");
									String relationType = p.matcher(productincrementcfgdt.getChildText("productincrementcfg_relation_type")).replaceAll("");
									String experiencePeriod = p.matcher(productincrementcfgdt.getChildText("productincrementcfg_experience_period")).replaceAll("");
									String isExperience = p.matcher(productincrementcfgdt.getChildText("productincrementcfg_is_experience")).replaceAll("");
									String freezePeriod = p.matcher(productincrementcfgdt.getChildText("productincrementcfg_freeze_period")).replaceAll("");
									
//									bean.setIsDefaultOpen(Integer.parseInt(isDefaultOpen));
//									bean.setRelationType(Integer.parseInt(relationType));
									//bean.setExperiencePeriod(Integer.parseInt(experiencePeriod));
//									bean.setIsExperience(Integer.parseInt(isExperience));
//									bean.setFreezePeriod(Integer.parseInt(freezePeriod));

									incrementList.add(bean);
							//	}
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

								if ("1".equals(isShow)) {
									bean = new ProSelf();

									String selfId1 = p.matcher(cproductselfplatcfgdt.getChildText("productselfplatcfg_domain_code")).replaceAll("");
									String selfId2 = p.matcher(cproductselfplatcfgdt.getChildText("productselfplatcfg_biz_code")).replaceAll("");
									String selfId3 = p.matcher(cproductselfplatcfgdt.getChildText("productselfplatcfg_prd_code")).replaceAll("");
									String isDefaultOpen = p.matcher(cproductselfplatcfgdt.getChildText("productselfplatcfg_is_default_open")).replaceAll("");
									String relationType = p.matcher(cproductselfplatcfgdt.getChildText("productselfplatcfg_relation_type")).replaceAll("");

									bean.setSelfId1(selfId1);
									bean.setSelfId2(selfId2);
									bean.setSelfId3(selfId3);
									bean.setIsDefaultOpen(Integer.parseInt(isDefaultOpen));
									bean.setRelationType(Integer.parseInt(relationType));

									selfList.add(bean);
								}
							}
						}
						
						//查询自有业务配置填补自有业务名称
						BaseResult selfCfgListRet = this.getSelfCfgList(accessId, config, params);
						if (LOGIC_SUCESS.equals(selfCfgListRet.getResultCode())) {
							Map<String, Object> reMap = (Map<String, Object>)selfCfgListRet.getReObj();
							Object prdListObj = reMap.get("prdList");
							Object bizListObj = reMap.get("bizList");
							List<CcCGetSpBizDetailBean> spprdList = null;
							List<CSelfPlatBizCfgDtBean> spbizList = null;
							if (prdListObj != null && prdListObj instanceof List) {
								spprdList = (List<CcCGetSpBizDetailBean>)prdListObj;
							}
							if (bizListObj != null && bizListObj instanceof List) {
								spbizList = (List<CSelfPlatBizCfgDtBean>)bizListObj;
							}
							
							if (spprdList != null && spprdList.size() > 0 && spbizList != null && spbizList.size() > 0) {
								for (ProSelf objSelf:selfList) {
									for (CSelfPlatBizCfgDtBean objFun:spbizList) {
										if (objSelf.getSelfId1().equalsIgnoreCase(objFun.getDomain_code()) &&
											objSelf.getSelfId2().equals(objFun.getBiz_code())) {
											if (objFun.getUnification_search().equals("1")) {
												for (CcCGetSpBizDetailBean objBiz:spprdList) {
													if (objSelf.getSelfId3().equals(objBiz.getPrdCode()) &&
														objSelf.getSelfId2().equals(objBiz.getBizCode())) {
														//写入自有业务名称
														objSelf.setSelfName(objBiz.getPrdName());
													}
												}
											}
										}
									}
								}
							}
						} else {
							res.setResultCode(LOGIC_ERROR);
							res.setErrorCode(selfCfgListRet.getErrorCode());
							res.setErrorMessage(selfCfgListRet.getErrorMessage());
						}
						
						retMap.put("self", selfList);
					}

					res.setReObj(retMap);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 查询奥运88套餐系列
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult getPkg88List(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		List<PackageInfoBean> pkg88List = null;
		try {
			List<RequestParameter> paramsNew = copyParam(params);
			paramsNew.add(new RequestParameter("biz_pkg_qry_scope", "1"));
			paramsNew.add(new RequestParameter("package_type", "1031"));
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_find_package_62_YYTC", paramsNew);

			logger.debug(" ====== 查询88系列套餐请求报文 ======\n" + reqXml);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_find_package_62_YYTC", this.generateCity(paramsNew)));
			logger.debug(" ====== 查询88系列套餐返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY050034", "cc_find_package_62_YYTC", errCode);
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
					List package_code = null;
					try
					{
						package_code = root.getChild("content").getChildren("package_code");
					}
					catch (Exception e)
					{
						package_code = null;
					}
					if (null != package_code && package_code.size() > 0) {
						pkg88List = new ArrayList<PackageInfoBean>(package_code.size());
						PackageInfoBean bean = null;
						for (int i = 0; i < package_code.size(); i++)
						{
							Element cplanpackagedt = ((Element)package_code.get(i)).getChild("cplanpackagedt");
							if (null != cplanpackagedt)
							{
								bean = new PackageInfoBean();
								bean.setPackageCode(p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll(""));

								bean.setPackageUserId(p.matcher(cplanpackagedt.getChildText("package_user_id")).replaceAll(""));
								bean.setPackageType(p.matcher(cplanpackagedt.getChildText("package_type")).replaceAll(""));
								bean.setPackageLevel(p.matcher(cplanpackagedt.getChildText("package_level")).replaceAll(""));
								bean.setPackageApplyDate(p.matcher(cplanpackagedt.getChildText("package_apply_date")).replaceAll(""));
								bean.setPackageUseDate(p.matcher(cplanpackagedt.getChildText("package_use_date")).replaceAll(""));
								bean.setPackageHistorySrl(p.matcher(cplanpackagedt.getChildText("package_history_srl")).replaceAll(""));
			                    bean.setPackageEndDate(p.matcher(cplanpackagedt.getChildText("package_end_date")).replaceAll(""));
			                    bean.setPackageState(p.matcher(cplanpackagedt.getChildText("package_state")).replaceAll(""));
			                    bean.setPackageName(p.matcher(cplanpackagedt.getChildText("package_name")).replaceAll(""));
								
								pkg88List.add(bean);
							}
						}
						res.setReObj(pkg88List);
					}
				}
			}
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
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetpackbycode_605", params);
				
				logger.debug(" ====== 查询套餐配置请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetpackbycode_605", this.generateCity(params)));
				logger.debug(" ====== 查询套餐配置返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY050034", "cc_cgetpackbycode_605", errCode);
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
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetbizname_713", params);
				
				logger.debug(" ====== 查询套餐大类名称请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetbizname_713", this.generateCity(params)));
				logger.debug(" ====== 查询套餐大类名称返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY050034", "cc_cgetbizname_713", errCode);
						if (null != errDt)
						{
							errCode = errDt.getLiErrCode();
							errDesc = errDt.getLiErrMsg();
						}
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
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
	 * 查询自有业务配置
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult getSelfCfgList(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetspbizdetail_528", params);

			logger.debug(" ====== 查询自有业务配置请求报文 ======\n" + reqXml);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetspbizdetail_528", this.generateCity(params)));
			logger.debug(" ====== 查询自有业务配置返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY050034", "cc_cgetspbizdetail_528", errCode);
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
					
					String prdContent = root.getChild("content").getChildText("XTABLE_SPPRD");
					List<Map<String,String>> prdList = TeletextParseUtils.parseXTABLE(prdContent);
					if (prdList != null) {
						List<CcCGetSpBizDetailBean> spprdList = new ArrayList<CcCGetSpBizDetailBean>(prdList.size());
						CcCGetSpBizDetailBean bean = null;
						for(Map<String,String> m : prdList){
							bean = new CcCGetSpBizDetailBean();
							
							bean.setBizCode(getString(m, "业务编码"));
							bean.setPrdCode(getString(m, "产品编码"));
							bean.setRelationType(getString(m, "产品关系类型"));
							bean.setPrdCanOrder(getString(m, "产品是否可订购"));
							bean.setChooseFlag(getString(m, "是否必选标志"));
							bean.setPrdDiscription(getString(m, "产品描述"));
							bean.setPrdName(getString(m, "产品名称"));
							bean.setFixFeeMonth(getString(m, "月固定收费金额"));
							
							spprdList.add(bean);
						}
						retMap.put("prdList", spprdList);
					}
					
					String bizContent = root.getChild("content").getChildText("XTABLE_SPBIZ");
					List<Map<String,String>> bizList = TeletextParseUtils.parseXTABLE(bizContent);
					if (bizList != null) {
						List<CSelfPlatBizCfgDtBean> spbizList = new ArrayList<CSelfPlatBizCfgDtBean>(bizList.size());
						CSelfPlatBizCfgDtBean bean = null;
						for(Map<String,String> m : bizList){
							bean = new CSelfPlatBizCfgDtBean();
							
							bean.setMore_choose_flag(getString(m, "基本产品是可多选标志"));
							bean.setCan_order(getString(m, "业务是否可订购"));
							bean.setUnification_search(getString(m, "梦网统一查询退订界面是否展现标志"));
							bean.setCan_donate(getString(m, "业务是否可赠送"));
							bean.setCan_booking(getString(m, "业务是否可预约"));
							bean.setDomain_code(getString(m, "平台编码"));
							bean.setBiz_code(getString(m, "业务编码"));
							bean.setBiz_name(getString(m, "业务名称"));
							bean.setBiz_description(getString(m, "业务说明"));
							bean.setTest_flag(getString(m, "测试标记"));
							
							spbizList.add(bean);
						}
						retMap.put("bizList", spbizList);
					}
					
					res.setReObj(retMap);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	private Boolean isSpecilPackage(String pckCode, String[] arr){

		boolean blRet = false;
		//String[] arrPackageCode = ["4957","","","","",];
		if(arr != null  && arr.length>0)
		for(int i = 0; i<arr.length; i++){
			if(pckCode.equals(arr[i])){
				blRet = true;
				break;
			}
		}
		return blRet;
	}
	
	private static Map<String, String> mapAdjFunDic = null;
	
	/**
	 * 附加功能字典
	 * @return
	 */
	private Map<String, String> getAdjFunDic() {
		if (mapAdjFunDic == null) {
			mapAdjFunDic = new HashMap<String, String>();
			mapAdjFunDic.put("1", "台港澳国际漫游");
			mapAdjFunDic.put("2", "省际漫游");
			mapAdjFunDic.put("3", "省内漫游");
			mapAdjFunDic.put("4", "短信");
			mapAdjFunDic.put("5", "语音信箱");
			mapAdjFunDic.put("6", "传真");
			mapAdjFunDic.put("7", "数据");
			mapAdjFunDic.put("8", "台港澳国际长权");
			mapAdjFunDic.put("9", "呼叫转移");
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
	
	public class PackageInfoBean {
		private String packageCode;
		private String packageUserId;
		private String packageType;
		private String packageLevel;
		private String packageApplyDate;
		private String packageUseDate;
		private String packageHistorySrl;
		private String packageEndDate;
		private String packageState;
		private String packageName;
		private String packageChangeDate;
		private String packageTypeName;
		private String packageDesc;
		
		public String getPackageChangeDate() {
			return packageChangeDate;
		}
		public void setPackageChangeDate(String packageChangeDate) {
			this.packageChangeDate = packageChangeDate;
		}
		public String getPackageTypeName() {
			return packageTypeName;
		}
		public void setPackageTypeName(String packageTypeName) {
			this.packageTypeName = packageTypeName;
		}
		/**
		 * @return the packageCode
		 */
		public String getPackageCode() {
			return packageCode;
		}
		/**
		 * @param packageCode the packageCode to set
		 */
		public void setPackageCode(String packageCode) {
			this.packageCode = packageCode;
		}
		/**
		 * @return the packageUserId
		 */
		public String getPackageUserId() {
			return packageUserId;
		}
		/**
		 * @param packageUserId the packageUserId to set
		 */
		public void setPackageUserId(String packageUserId) {
			this.packageUserId = packageUserId;
		}
		/**
		 * @return the packageType
		 */
		public String getPackageType() {
			return packageType;
		}
		/**
		 * @param packageType the packageType to set
		 */
		public void setPackageType(String packageType) {
			this.packageType = packageType;
		}
		/**
		 * @return the packageLevel
		 */
		public String getPackageLevel() {
			return packageLevel;
		}
		/**
		 * @param packageLevel the packageLevel to set
		 */
		public void setPackageLevel(String packageLevel) {
			this.packageLevel = packageLevel;
		}
		/**
		 * @return the packageApplyDate
		 */
		public String getPackageApplyDate() {
			return packageApplyDate;
		}
		/**
		 * @param packageApplyDate the packageApplyDate to set
		 */
		public void setPackageApplyDate(String packageApplyDate) {
			this.packageApplyDate = packageApplyDate;
		}
		/**
		 * @return the packageUseDate
		 */
		public String getPackageUseDate() {
			return packageUseDate;
		}
		/**
		 * @param packageUseDate the packageUseDate to set
		 */
		public void setPackageUseDate(String packageUseDate) {
			this.packageUseDate = packageUseDate;
		}
		/**
		 * @return the packageHistorySrl
		 */
		public String getPackageHistorySrl() {
			return packageHistorySrl;
		}
		/**
		 * @param packageHistorySrl the packageHistorySrl to set
		 */
		public void setPackageHistorySrl(String packageHistorySrl) {
			this.packageHistorySrl = packageHistorySrl;
		}
		/**
		 * @return the packageEndDate
		 */
		public String getPackageEndDate() {
			return packageEndDate;
		}
		/**
		 * @param packageEndDate the packageEndDate to set
		 */
		public void setPackageEndDate(String packageEndDate) {
			this.packageEndDate = packageEndDate;
		}
		/**
		 * @return the packageState
		 */
		public String getPackageState() {
			return packageState;
		}
		/**
		 * @param packageState the packageState to set
		 */
		public void setPackageState(String packageState) {
			this.packageState = packageState;
		}
		/**
		 * @return the packageName
		 */
		public String getPackageName() {
			return packageName;
		}
		/**
		 * @param packageName the packageName to set
		 */
		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}
		public String getPackageDesc() {
			return packageDesc;
		}
		public void setPackageDesc(String packageDesc) {
			this.packageDesc = packageDesc;
		}
	}
	
	public class CcCGetSpBizDetailBean
	{
		//业务编码
		private String bizCode;
		//产品编码
		private String prdCode;
		//产品关系类型
		private String relationType;
		
		private String prdCanOrder;
		private String chooseFlag;
		private String prdDiscription;
		private String prdName;
		private String fixFeeMonth;
		private String status;
		private String timeEnded;
		private String timeOpened;
		/**
		 * @return the bizCode
		 */
		public String getBizCode()
		{
			return bizCode;
		}
		/**
		 * @param bizCode the bizCode to set
		 */
		public void setBizCode(String bizCode)
		{
			this.bizCode = bizCode;
		}
		/**
		 * @return the chooseFlag
		 */
		public String getChooseFlag()
		{
			return chooseFlag;
		}
		/**
		 * @param chooseFlag the chooseFlag to set
		 */
		public void setChooseFlag(String chooseFlag)
		{
			this.chooseFlag = chooseFlag;
		}
		/**
		 * @return the fixFeeMonth
		 */
		public String getFixFeeMonth()
		{
			return fixFeeMonth;
		}
		/**
		 * @param fixFeeMonth the fixFeeMonth to set
		 */
		public void setFixFeeMonth(String fixFeeMonth)
		{
			this.fixFeeMonth = fixFeeMonth;
		}
		/**
		 * @return the prdCanOrder
		 */
		public String getPrdCanOrder()
		{
			return prdCanOrder;
		}
		/**
		 * @param prdCanOrder the prdCanOrder to set
		 */
		public void setPrdCanOrder(String prdCanOrder)
		{
			this.prdCanOrder = prdCanOrder;
		}
		/**
		 * @return the prdCode
		 */
		public String getPrdCode()
		{
			return prdCode;
		}
		/**
		 * @param prdCode the prdCode to set
		 */
		public void setPrdCode(String prdCode)
		{
			this.prdCode = prdCode;
		}
		/**
		 * @return the prdDiscription
		 */
		public String getPrdDiscription()
		{
			return prdDiscription;
		}
		/**
		 * @param prdDiscription the prdDiscription to set
		 */
		public void setPrdDiscription(String prdDiscription)
		{
			this.prdDiscription = prdDiscription;
		}
		/**
		 * @return the prdName
		 */
		public String getPrdName()
		{
			return prdName;
		}
		/**
		 * @param prdName the prdName to set
		 */
		public void setPrdName(String prdName)
		{
			this.prdName = prdName;
		}
		/**
		 * @return the relationType
		 */
		public String getRelationType()
		{
			return relationType;
		}
		/**
		 * @param relationType the relationType to set
		 */
		public void setRelationType(String relationType)
		{
			this.relationType = relationType;
		}
		/**
		 * @return the status
		 */
		public String getStatus()
		{
			return status;
		}
		/**
		 * @param status the status to set
		 */
		public void setStatus(String status)
		{
			this.status = status;
		}
		/**
		 * @return the timeEnded
		 */
		public String getTimeEnded()
		{
			return timeEnded;
		}
		/**
		 * @param timeEnded the timeEnded to set
		 */
		public void setTimeEnded(String timeEnded)
		{
			this.timeEnded = timeEnded;
		}
		/**
		 * @return the timeOpened
		 */
		public String getTimeOpened()
		{
			return timeOpened;
		}
		/**
		 * @param timeOpened the timeOpened to set
		 */
		public void setTimeOpened(String timeOpened)
		{
			this.timeOpened = timeOpened;
		}
	}
	
	public class CSelfPlatBizCfgDtBean {

		/**
		 * 基本产品是可多选标志0：单选，只能选择一个基本产品1
		 */
		private String more_choose_flag;

		/**
		 * 
		 */
		private String can_order;

		/**
		 * 
		 */
		private String unification_search;

		/**
		 * 
		 */
		private String can_donate;

		/**
		 * 
		 */
		private String can_booking;

		/**
		 * 平台编码
		 */
		private String domain_code;

		/**
		 * 业务编码
		 */
		private String biz_code;

		/**
		 * 业务名称
		 */
		private String biz_name;

		/**
		 * 业务说明
		 */
		private String biz_description;

		/**
		 * 测试标记0：非测试交易1：测试交易
		 */
		private String test_flag;

		public String getBiz_code() {
			return biz_code;
		}

		public void setBiz_code(String biz_code) {
			this.biz_code = biz_code;
		}

		public String getBiz_description() {
			return biz_description;
		}

		public void setBiz_description(String biz_description) {
			this.biz_description = biz_description;
		}

		public String getBiz_name() {
			return biz_name;
		}

		public void setBiz_name(String biz_name) {
			this.biz_name = biz_name;
		}

		public String getCan_booking() {
			return can_booking;
		}

		public void setCan_booking(String can_booking) {
			this.can_booking = can_booking;
		}

		public String getCan_donate() {
			return can_donate;
		}

		public void setCan_donate(String can_donate) {
			this.can_donate = can_donate;
		}

		public String getCan_order() {
			return can_order;
		}

		public void setCan_order(String can_order) {
			this.can_order = can_order;
		}

		public String getDomain_code() {
			return domain_code;
		}

		public void setDomain_code(String domain_code) {
			this.domain_code = domain_code;
		}

		public String getMore_choose_flag() {
			return more_choose_flag;
		}

		public void setMore_choose_flag(String more_choose_flag) {
			this.more_choose_flag = more_choose_flag;
		}

		public String getTest_flag() {
			return test_flag;
		}

		public void setTest_flag(String test_flag) {
			this.test_flag = test_flag;
		}

		public String getUnification_search() {
			return unification_search;
		}

		public void setUnification_search(String unification_search) {
			this.unification_search = unification_search;
		}

	}
}