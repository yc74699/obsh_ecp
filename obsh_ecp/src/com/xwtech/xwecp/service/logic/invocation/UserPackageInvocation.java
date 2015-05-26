package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.DAOException;
import com.xwtech.xwecp.dao.IPackageChangeDAO;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.pojo.PackageBean;
import com.xwtech.xwecp.pojo.PkgExgSelectBean;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY050027Result;
import com.xwtech.xwecp.service.logic.pojo.UserPackage;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 查询用户的套餐
 * 
 * @author 邵琪
 * 
 */
public class UserPackageInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(UserPackageInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");

	private IPackageChangeDAO packageChangeDAO;

	public UserPackageInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.packageChangeDAO = (IPackageChangeDAO) (springCtx.getBean("packageChangeDAO"));

	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY050027Result res = new QRY050027Result();
		try {
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			List<String> usingPckCodeList = new ArrayList<String>();
			String userCity = (String) getParameters(params, "context_ddr_city");
			params.add(new RequestParameter("cityId", userCity));
			
			String city = (String) getParameters(params, "city");
			params.add(new RequestParameter("city", city));
			// 查询用户产品信息
			BaseResult proInfoResult = this.getProInfo(accessId, config, params, res);
			if (LOGIC_SUCESS.equals(proInfoResult.getResultCode())) {
				// 获取用户产品ID
				String productId = (String) proInfoResult.getReObj();
				//在配置表中获取该产品下套餐是否可以变更
				//boolean changFlag  = true;
				boolean changFlag = packageChangeDAO.allowChangePackage(productId);
				//是否可以套餐变更，返回changFlag 0:不可以；1：可以
				if(!changFlag) {
					res.setChangeFlag("0");
					//如果以下线的产品，只查出当前套餐
					List<UserPackage> userPackageList = new ArrayList();
					userPackageList = this.getCurPackageInfo(accessId, config, params, res);
					res.setUserPackageList(userPackageList);
					return res;
				} else {
					res.setChangeFlag("1");
				}
				// 设置用户产品ID参数
				params.add(new RequestParameter("proId", productId));

				// 查询用户的套餐
				BaseResult proPackCfgResult = this.getProPackCfg(accessId, config, params, res);

				if (LOGIC_SUCESS.equals(proPackCfgResult.getResultCode())) {
					// 获取套餐列表
					List<UserPackage> userPackageList = (List<UserPackage>) proPackCfgResult.getReObj();
					if (userPackageList != null && userPackageList.size() > 0) {
						// 查询个人开通套餐，与必选套餐比对，找到主必选开通套餐
						this.queryPersonalPackage(accessId, config, params, res, userPackageList);
						// TODO 根据内存过滤
						// 设置套餐名称、描述
						List list = new ArrayList();
						for (UserPackage package1 : userPackageList) {
							list.add(new BusinessBoss(package1.getPackageCode()));
						}
						params.add(new RequestParameter("codeCount", list));
						BaseResult pkgCfgResult = this.getPkgCfgList(accessId, config, params);
						if (LOGIC_SUCESS.equals(pkgCfgResult.getResultCode())) {

							List<ProductBusinessPackageBean> pkgCfgList = (List<ProductBusinessPackageBean>) pkgCfgResult.getReObj();
							for (UserPackage package1 : userPackageList) {
								for (ProductBusinessPackageBean objPackage : pkgCfgList) {
									if (objPackage.getPackageCode().equals(package1.getPackageCode())) {
											package1.setPackageName(objPackage.getPackageName());
											package1.setPackageDesc(objPackage.getPackageDesc());
									}
								}
							}
							
							//以下用于返回页面的备选套餐过滤等逻辑处理。
							//95系套餐
							String[] arr95Package = {"4957","4958","4959","4960"};
//							老奥运88系套餐
							String[] arrOld88Package = {"1340","1342","1344","1346","1347","1348","1349","1350"};
							//新旧88套餐
							String[] arrAll88Package = {"1340","1342","1344","1346","1347","1348","1349","1350","1740" ,
									"1741","1742","1743","1744","1745","1746","1747","1748","2377"};
							Iterator incIt = userPackageList.iterator();
							UserPackage pckBean = new UserPackage();
							while (incIt.hasNext()){
								pckBean = (UserPackage) incIt.next();
								//备选套餐有老88套餐，则全部不显示
								if(isSpecilPackage(pckBean.getPackageCode(),arrOld88Package) 
										&& pckBean.getCurOpen() != "1"){
									incIt.remove();
								}
								
								//如果客户原来的套餐是“新旧奥运88系列套餐”，则屏蔽“95系列套餐”，即客户不能看到“95系列套餐”。
								//反之，如果客户原来不是“奥运88系列套餐”，则客户仍然是可以看到“95系列套餐”
								for(int i= 0;i < usingPckCodeList.size();i++){
									if(isSpecilPackage(usingPckCodeList.get(i),arrAll88Package)
											 && isSpecilPackage(pckBean.getPackageCode(),arr95Package)){
										incIt.remove();
										break;
									}
								}
							}
							
						} else {
							res.setResultCode(LOGIC_ERROR);
							res.setErrorCode(pkgCfgResult.getErrorCode());
							res.setErrorMessage(pkgCfgResult.getErrorMessage());
						}
						
						//查询数据库中所有套餐列表,转换boss_packageCode为ECP_packageCode
						userPackageList = this.getPackageCodeToChange(userPackageList);
						
						//过滤后台配置的套餐数据：todo第二个参数地市信息
						userPackageList = filterPackage(userPackageList, city, productId);
						
						res.setUserPackageList(userPackageList);
					}
					
				} else {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(proPackCfgResult.getErrorCode());
					res.setErrorMessage(proPackCfgResult.getErrorMessage());
				}
			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(proInfoResult.getErrorCode());
				res.setErrorMessage(proInfoResult.getErrorMessage());
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	protected BaseResult getBizName(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetbizname_713", params);

			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetbizname_713",super.generateCity(params)));
			logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050020", "cc_cgetbizname_713", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);

				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath.newInstance("/operation_out/content/biz_name");
					Element element = (Element) xpath.selectSingleNode(root);
					res.setReObj(element.getTextTrim());

				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 查询用户产品信息
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getProInfo(final String accessId, final ServiceConfig config, final List<RequestParameter> params, final QRY050027Result result) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetproinfo_345", params);

			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetproinfo_345", super.generateCity(params)));
			logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050020", "cc_cgetproinfo_345", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);

				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath.newInstance("/operation_out/content/userproductinfo_product_info_id/cuserproductinfodt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					String now = DateTimeUtil.getTodayChar14();
					for (Element element : list) {
						String product_id = element.getChildText("userproductinfo_product_id").trim();
						String start_date = element.getChildText("userproductinfo_start_date").trim();
						String distance = DateTimeUtil.getDistanceDT(now, start_date, "s");
						if (Long.parseLong(distance) < 0) {
							res.setReObj(product_id);
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 查询用户当前套餐
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<UserPackage> getCurPackageInfo(final String accessId, final ServiceConfig config, final List<RequestParameter> params, final QRY050027Result result) {
		BaseResult res = new BaseResult();
		 List<UserPackage> retList = new ArrayList<UserPackage>();
		String reqXml = "";
		String rspXml = "";
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_find_package_62_TC", params);

			logger.debug(" ====== 创建订单请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_find_package_62_TC", super.generateCity(params)));
			logger.debug(" ====== 创建订单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050027", "cc_find_package_62_TC", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);

				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath.newInstance("/operation_out/content/package_code/cplanpackagedt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					String[] oldPackage ={"2000001081","2000001082","2000001083","2000001084","2000001085",
										  "2000001086","2000001087","2000001088","2000001089","2000001141",
										  "2000001142","2000001143","2000001880","2000001881","2000001882"};
					for (Element element : list) {
							
							String pkgCode = element.getChildTextTrim("package_code");
							String pkgName = element.getChildTextTrim("package_name");
							String pkgState = element.getChildTextTrim("package_state");
							if (isSpecilPackage(pkgCode,oldPackage) && "0".equals(pkgState)) {
								String pkgDesc ="";
								// 设置套餐名称、描述
								List list2 = new ArrayList();
								list2.add(new BusinessBoss(pkgCode));
								
								params.add(new RequestParameter("codeCount", list2));
								BaseResult pkgCfgResult = this.getPkgCfgList(accessId, config, params);
								if (LOGIC_SUCESS.equals(pkgCfgResult.getResultCode())) {
									List<ProductBusinessPackageBean> pkgCfgList = (List<ProductBusinessPackageBean>) pkgCfgResult.getReObj();
										for (ProductBusinessPackageBean objPackage : pkgCfgList) {
											if (objPackage.getPackageCode().equals(pkgCode)) {
												pkgDesc = objPackage.getPackageDesc();
												break;
											}
										}
								}
								UserPackage bean = new UserPackage();
								bean.setPackageCode(pkgCode);
								bean.setPackageName(pkgName);
								bean.setPackageDesc(pkgDesc);
								bean.setCurOpen("1");
								retList.add(bean);
								break;
							}

					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return retList;
	}
	
	/**
	 * 
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param userPackageList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult queryPersonalPackage(final String accessId, final ServiceConfig config, final List<RequestParameter> params, final QRY050027Result result,
			List<UserPackage> userPackageList) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_find_package_62_TC", params);

			logger.debug(" ====== 创建订单请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_find_package_62_TC", super.generateCity(params)));
			logger.debug(" ====== 创建订单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050027", "cc_find_package_62_TC", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);

				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath.newInstance("/operation_out/content/package_code/cplanpackagedt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					for (Element element : list) {
						for (UserPackage package1 : userPackageList) {
							String pkgCode = element.getChildTextTrim("package_code");
							if (package1.getPackageCode().equals(pkgCode)) {
								package1.setStartDate(element.getChildTextTrim("package_use_date"));
								package1.setCurOpen("1");
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 查询用户的套餐
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getProPackCfg(final String accessId, final ServiceConfig config, final List<RequestParameter> params, final QRY050027Result result) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetpropackcfg_377", params);

			logger.debug(" ====== 创建订单请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetpropackcfg_377", super.generateCity(params)));
			logger.debug(" ====== 创建订单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050027", "cc_cgetpropackcfg_377", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);

				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
				
					XPath xpath = XPath.newInstance("/operation_out/content/productbasepackagecfg_product_id/productbasepackagecfgdt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					// 开通时间、关闭时间，次月初
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.MONTH, 1);
					cal.set(Calendar.DAY_OF_MONTH, 1);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					String nextDate = sdf.format(cal.getTime());
					cal.add(Calendar.DAY_OF_MONTH, -1);
					String endDate = sdf.format(cal.getTime());
					List<UserPackage> rtnList = new ArrayList<UserPackage>();
					for (Element element : list) {
						UserPackage userPackage = new UserPackage();
						userPackage.setProId(element.getChildTextTrim("productbasepackagecfg_product_id"));
						userPackage.setPkgId(element.getChildTextTrim("productbasepackagecfg_business_id"));
						userPackage.setPackageCode(element.getChildTextTrim("productbasepackagecfg_package_code"));
						userPackage.setPackageName(element.getChildTextTrim("productbasepackagecfg_pack_name"));
						userPackage.setStartDate(element.getChildTextTrim("productbasepackagecfg_create_date"));
						userPackage.setNextDate(nextDate);
						userPackage.setEndDate(endDate);
						userPackage.setNextDate(nextDate);
						userPackage.setCurOpen("0");
						rtnList.add(userPackage);
					}
					res.setReObj(rtnList);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 套餐配置信息
	 * 
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
		List<ProductBusinessPackageBean> pkgCfgList = null;
		try {
			
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetpackbycode_605", params);

			logger.debug(" ====== 查询套餐配置请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetpackbycode_605", super.generateCity(params)));
			logger.debug(" ====== 查询套餐配置返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY050017", "cc_cgetpackbycode_605", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);

				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					List productbusinesspackage_package_code = null;
					try {
						productbusinesspackage_package_code = root.getChild("content").getChildren("productbusinesspackage_package_code");
					} catch (Exception e) {
						productbusinesspackage_package_code = null;
					}
					if (null != productbusinesspackage_package_code && productbusinesspackage_package_code.size() > 0) {
						pkgCfgList = new ArrayList<ProductBusinessPackageBean>(productbusinesspackage_package_code.size());
						ProductBusinessPackageBean bean = null;
						for (int i = 0; i < productbusinesspackage_package_code.size(); i++) {
							bean = new ProductBusinessPackageBean();
							Element cproductbusinesspackagedt = ((Element) productbusinesspackage_package_code.get(i)).getChild("cproductbusinesspackagedt");
							if (null != cproductbusinesspackagedt) {
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
						// this.wellFormedDAO.getCache().add(key, proCfgList);
						res.setReObj(pkgCfgList);
					}
				}
			}

		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 解析报文
	 * 
	 * @param tmp
	 * @return
	 */
	public Element getElement(byte[] tmp) {
		Element root = null;
		try {
			ByteArrayInputStream ins = new ByteArrayInputStream(tmp);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(ins);
			root = doc.getRootElement();
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return root;
	}

	private Boolean isSpecilPackage(String pckCode, String[] arr){

		boolean retCode = false;
		if(arr != null  && arr.length>0)
		for(int i = 0; i < arr.length; i++){
			if(pckCode.equals(arr[i])){
				retCode = true;
				break;
			}
		}
		return retCode;
	}
	
	public class ProductBusinessPackageBean {
		private String bossToolFlag;

		private String webFlag;

		private String bossViewFlag;

		// 地市代码
		private String cityId;

		// 套餐名称
		private String packageName;

		// 套餐结束时间
		private String endDate;

		// 套餐描述
		private String packageDesc;

		// 套餐大类
		private String packageClass;

		private String flag1860;

		private String cuiFlag;

		private String isNowclose;

		private String isNowopen;

		private String openChoice;

		private String preChoice;

		// 套餐开始时间
		private String startDate;

		// 套餐级别
		private String packageLevel;

		// 套餐代码
		private String packageCode;

		// 套餐大类
		private String businessId;

		// 套餐分组
		private String groupId;

		// 分组名称
		private String groupName;

		// 分类名称
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
	 * 业务boss实现类
	 * 
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
	
	private List<UserPackage> getPackageCodeToChange(List<UserPackage> userPackageList){
		//查询数据库中所有套餐列表,转换boss_packageCode为ECP_packageCode
		List<PackageBean> packageList = null;
		List<UserPackage> resultList = new ArrayList<UserPackage>();
		try {
			packageList = packageChangeDAO.getPackageInfo();
			if(packageList != null && packageList.size() > 0) {
				for(PackageBean bean : packageList) {
					String bossPackageCode = bean.getPkgNumBoss();
					if(userPackageList != null && userPackageList.size() > 0) {
						for(UserPackage uBean : userPackageList) {
							String bossCode = uBean.getPackageCode();
							if(bossCode.equals(bossPackageCode)) {
								uBean.setPackageCode(bean.getPkgNum());
								uBean.setPkgNumBoss(bean.getPkgNumBoss());
								if(uBean.getPackageDesc()!=null){
								uBean.setPackageDesc(uBean.getPackageDesc().replaceAll("\"", ""));
								}
								resultList.add(uBean);
								break;
							}
						}
					}
				}
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	private List<UserPackage> filterPackage(List<UserPackage> userPackageList, String area, String productId) {
		//过滤后台配置可配置的套餐
		int count = 0;
		List<PkgExgSelectBean> packageList = null;
		List<UserPackage> resultList = new ArrayList<UserPackage>();
		try {
			packageList = packageChangeDAO.getPkgExgSelect(area, productId);
			if(packageList != null && packageList.size() > 0) {
				for(PkgExgSelectBean bean : packageList) {
					String PackageCode = bean.getImpPkgNumEcp();
					if(userPackageList != null && userPackageList.size() > 0) {
						for(UserPackage uBean : userPackageList) {
							String code = uBean.getPackageCode();
							if(code.equals(PackageCode) && !uBean.getCurOpen().equals("1")) {
								resultList.add(uBean);
								break;
							}
						}
					} 
				}
			}
			//过滤出当前正使用的套餐
			for(UserPackage uBean : userPackageList) {
				if(uBean.getCurOpen().equals("1") && count == 0) {
					resultList.add(uBean);
					count ++;
					break;
				}
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	
}