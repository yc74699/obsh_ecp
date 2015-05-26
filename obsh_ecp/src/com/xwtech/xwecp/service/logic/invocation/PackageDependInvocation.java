package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.IPackageChangeDAO;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.pojo.PkgExgNeedBusiInfo;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.PackageDepend;
import com.xwtech.xwecp.service.logic.pojo.QRY050028Result;
import com.xwtech.xwecp.service.logic.pojo.UserPackage;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 查询套餐依赖业务
 * 
 * @author 邵琪
 * 
 */
public class PackageDependInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(PackageDependInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;
	
	private IPackageChangeDAO packageChangeDAO;
	
	private Map<String, String> mapAdjFunDic;
	private Map<String, String> mapAddIncDic;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");

	public PackageDependInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.packageChangeDAO = (IPackageChangeDAO) (springCtx.getBean("packageChangeDAO"));
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY050028Result res = new QRY050028Result();
		try {
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			//返回的LIST
			List<PackageChgInfoBean> packageChgList = new ArrayList<PackageChgInfoBean>();
			// 传入参数
			
			String city = (String) getParameters(params, "city");
			String proId = (String) getParameters(params, "proId");
			//当前转出的套餐编码
			String paramOldPckCode = (String) getParameters(params, "oldPckCode");
			//当前转入的套餐编码
			String paramNewPckCode = (String) getParameters(params, "newPckCode");
			//套餐大类
			String paramPackageType = (String) getParameters(params, "packageType");
			String[] packageType = new String[]{paramPackageType,paramPackageType};
			
			String[] packageCode = new String[]{paramOldPckCode,paramNewPckCode};
			String[] optWay = new String[]{"0","1"};
			
//			params.add(new RequestParameter("packageType", packageType));
//			params.add(new RequestParameter("packageCode", packageCode));
//			params.add(new RequestParameter("optWay", optWay));
			
		    //开通时间次月初	
		    Calendar cal = Calendar.getInstance();  
		    cal.add(Calendar.MONTH,1);
		    cal.set(Calendar.DAY_OF_MONTH, 1);
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		    String nextDate = sdf.format(cal.getTime());
    	
		    //获取预约产品Id

			String userCity = (String) getParameters(params, "context_ddr_city");
			params.add(new RequestParameter("cityId", userCity));
			// 查询用户产品信息
			BaseResult proInfoResult = this.getUserBookingProId(accessId, config, params, res);
			// 获取用户产品ID
			String productId = (String) proInfoResult.getReObj();
			if(productId == null){
				productId = proId;
			}

			// 设置用户产品ID参数
			params.add(new RequestParameter("proId", productId));

			// 检查必选套餐变更冲突
			List<PackageChgInfoBean> pkgCloseList = new ArrayList<PackageChgInfoBean>();
			BaseResult res2 =  this.getconflictCheckPkgs(proId,productId,accessId, config, params, res);
			pkgCloseList = (List<PackageChgInfoBean>)res2.getReObj();
			if (pkgCloseList != null && pkgCloseList.size() > 0) {
				for (PackageChgInfoBean bean : pkgCloseList) {
					packageChgList.add(bean);
				}
				
			}
				
			for (int i = 0; i < packageCode.length; i++) {
        		//5元随意邮
        		if ("1151".equals(packageCode[i])) {
        			BaseResult spUserInfoResult = this.getSpUserInfo(accessId, config, params);
        			if (LOGIC_SUCESS.equals(spUserInfoResult.getResultCode())) {
//		            			将5元随意邮加入开通业务列表中
            			if(Boolean.TRUE.equals(spUserInfoResult.getReObj())){
    						PackageChgInfoBean bean = new PackageChgInfoBean();
    						bean.setPackageType("");
    						bean.setPackageCode("200501");
    						bean.setPackageName("五元手机邮箱");
    						bean.setOptWay("80");
    						bean.setNextDate(nextDate);	        						
    						packageChgList.add(bean);
            			}
        			}
        		}
        		//无限音乐俱乐部高级会员
        		if ("4991".equals(packageCode[i])) {
					PackageChgInfoBean bean = new PackageChgInfoBean();
					bean.setPackageType("");
					bean.setPackageCode("200501");
					bean.setPackageName("无限音乐俱乐部高级会员");
					bean.setOptWay("90");
					bean.setNextDate(nextDate);	        						
					packageChgList.add(bean);
        		}
        		// 配置参数
        		List<Map<String,String>> productdependcfgDependList = new ArrayList<Map<String,String>>();
        		Map<String,String> productdependcfgDependMap = new HashMap<String, String>();
        		productdependcfgDependMap.put("type", "4");
        		productdependcfgDependMap.put("code", packageType[i]);
        		productdependcfgDependMap.put("subCode", packageCode[i]);
        		productdependcfgDependList.add(productdependcfgDependMap);
        		params.add(new RequestParameter("productdependcfgDepend", productdependcfgDependList));
        		
        		BaseResult dependResult = this.getDepend(accessId, config, params);
        		if (LOGIC_SUCESS.equals(dependResult.getResultCode())) {
        			List<GetDependBean> rtnList = (List<GetDependBean>)dependResult.getReObj();
        			if (rtnList != null && rtnList.size() > 0) {
        				for (GetDependBean obj : rtnList) {
        					PackageChgInfoBean packageChgInfoBean = new PackageChgInfoBean();
    						//语音业务
    						if ("1".equals(obj.getDepend_type())) {
								//获取附加功能字典
								Map<String, String> map = getAdjFunDic();
    							//需要提示关闭的套餐
								BaseResult userAddonSingleResult = this.getUserAddonSingle(accessId, config, params);
								if (LOGIC_SUCESS.equals(userAddonSingleResult.getResultCode())) {
    								List<UserAddonFunctionBean> addonList = (List<UserAddonFunctionBean>)userAddonSingleResult.getReObj();
    								if (addonList != null && addonList.size()>0) {
    									for (UserAddonFunctionBean bean : addonList) {
    										if (obj.getDepended_code() != null && obj.getDepended_code().equals(bean.getSvcode())) {
    											if ("0".equals(optWay[i])) {
    												packageChgInfoBean.setPackageType("");
        											packageChgInfoBean.setPackageCode(bean.getSvcode());
        											packageChgInfoBean.setPackageName((String)map.get(obj.getDepended_code()));
        											packageChgInfoBean.setOptWay("0");
        											break;
    											} 
    										}
    									}
    								} else {//需要开通的套餐
    									if ("1".equals(optWay[i])) {
    										packageChgInfoBean.setPackageType("");
											packageChgInfoBean.setPackageCode(obj.getDepended_code());
											packageChgInfoBean.setPackageName((String)map.get(obj.getDepended_code()));
											packageChgInfoBean.setOptWay("1");
											packageChgInfoBean.setNextDate(nextDate);
    									}
    								}
    							}           								        							
    						}
    						
    						//增值业务
    						if ("2".equals(obj.getDepend_type())) {
    							//获取增值业务字典
								Map<String, String> map = getAddIncDic();
								BaseResult incInfResult = this.getIncInfo(accessId, config, params);
								if (LOGIC_SUCESS.equals(incInfResult.getResultCode())) {
									List<GetIncInfoBean> resultIncList = (List<GetIncInfoBean>)incInfResult.getReObj();
				                	if (resultIncList != null && resultIncList.size() > 0) {
				                		for (GetIncInfoBean bean : resultIncList) {
    										if (obj.getDepended_code() != null && obj.getDepended_code().equals(bean.getSmscall_deal_code())) {
    											if (obj.getDepended_code().equals(bean.getSmscall_deal_code())) {
    												if ("0".equals(optWay[i])) {
    													packageChgInfoBean.setPackageType("");
            											packageChgInfoBean.setPackageCode(bean.getSmscall_deal_code());
            											packageChgInfoBean.setPackageName((String)map.get(obj.getDepended_code()));
            											packageChgInfoBean.setOptWay("0");
            											break;
    												}
    											}
    										}
    									}
				                	} else {
    									if ("1".equals(optWay[i])) {   
    										packageChgInfoBean.setPackageType("");
											packageChgInfoBean.setPackageCode(obj.getDepended_code());
											packageChgInfoBean.setPackageName((String)map.get(obj.getDepended_code()));
											packageChgInfoBean.setOptWay("2");
											packageChgInfoBean.setNextDate(nextDate);
    									}
				                	}
    							}  										
    						}
    						BaseResult retMonternetResutlt = null, retMonternetIp = null;//减少重复调用接口
    						//梦网业务
    						if ("3".equals(obj.getDepend_type()) || "7".equals(obj.getDepend_type())) {
    							if (retMonternetResutlt == null) {
    								retMonternetResutlt = this.queryMonternetBusiness(accessId, config, params);
    							}
    							if (LOGIC_SUCESS.equals(retMonternetResutlt.getResultCode())) {
    								List<MonternetBusinessBean> listBiz = (List<MonternetBusinessBean>)retMonternetResutlt.getReObj();
    								
    								if (listBiz != null && listBiz.size() > 0) {
    									for (MonternetBusinessBean bean : listBiz) {
    										if (obj.getDepended_code() != null && obj.getDepended_sub_code() != null && obj.getDepended_code().equals(bean.getSpId()) && obj.getDepended_sub_code().equals(bean.getBizCode())) {
    											if ("0".equals(optWay[i])) {
													packageChgInfoBean.setPackageType("");
        											packageChgInfoBean.setPackageCode(obj.getDepended_sub_code());
        											packageChgInfoBean.setPackageName(bean.getBizName());
        											packageChgInfoBean.setOptWay("0");
        											break;
												} 
    										}
    									}
    									
    								}else{
    									if ("1".equals(optWay[i])) {
            								//查询所有梦网业务
            								if (retMonternetIp == null) {
                								//获取用户登录IP
                								//String operatorIp = (String) getParameter(params, "phoneNum");;                    									
            									retMonternetIp = this.queryMonternet(accessId, config, params);
            								}
            								if (LOGIC_SUCESS.equals(retMonternetIp.getResultCode())) {				
            									List<MonternetBusinessBean> allBizs = (List<MonternetBusinessBean>)retMonternetIp.getReObj();	
            				                	if (allBizs != null && allBizs.size() > 0) {
            				                		for (MonternetBusinessBean bean : allBizs) {
                										if (obj.getDepended_code() != null && obj.getDepended_sub_code() != null && obj.getDepended_code().equals(bean.getSpId()) && obj.getDepended_sub_code().equals(bean.getBizCode())) {
                											packageChgInfoBean.setPackageType(obj.getDepended_code());
                											packageChgInfoBean.setPackageCode(obj.getDepended_sub_code());
                											packageChgInfoBean.setPackageName(bean.getBizName());
                											packageChgInfoBean.setOptWay("3"); 
                											packageChgInfoBean.setNextDate(nextDate);
                										}
            				                		}	
            				                	}            									
            								}            										
    									}
    									
    								}
    							}
    						}
    				
    						//自有平台业务
    						BaseResult retSelfBizResult = null, retSpUserInfo = null;//减少重复调用接口
    						if ("6".equals(obj.getDepend_type())) { }
    						if (packageChgInfoBean.getPackageCode() != null){
    							packageChgList.add(packageChgInfoBean);
    						}
    						
    						
        			}
        		}
			}
			}
//            		//增值业务查询
            		List tempList = new ArrayList();
            		Map map1 = new HashMap();
            		map1.put("parm1", "5026");
            		Map map2 = new HashMap();
            		map2.put("parm1", "5006");
            		Map map3 = new HashMap();
            		map3.put("parm1", "5025");
            		tempList.add(map1);tempList.add(map2);tempList.add(map3);
            		params.add(new RequestParameter("codeCount", tempList));
            		BaseResult retIncDepanResult = this.getIncInfo(accessId, config, params);
					if (LOGIC_SUCESS.equals(retIncDepanResult.getResultCode())) {
						List<GetIncInfoBean> resultIncList = (List<GetIncInfoBean>)retIncDepanResult.getReObj();
						if (resultIncList != null && resultIncList.size() > 0) {
	                		for (GetIncInfoBean bean : resultIncList) {
								if ("5026".equals(bean.getSmscall_deal_code()) 
										|| "5025".equals(bean.getSmscall_deal_code())
										||"5006".equals(bean.getSmscall_deal_code())) {
	
		                			PackageChgInfoBean packageChgInfoBean = new PackageChgInfoBean();
									packageChgInfoBean.setPackageType("");
									packageChgInfoBean.setPackageCode(bean.getSmscall_deal_code());
									if("5006".equals(bean.getSmscall_deal_code())){
										packageChgInfoBean.setPackageName("彩铃");
									}else if("5026".equals(bean.getSmscall_deal_code())){
										packageChgInfoBean.setPackageName("CMWAP业务");
									}else if("5025".equals(bean.getSmscall_deal_code())){
										packageChgInfoBean.setPackageName("CMNET业务");
									}else {
									
									}
									
									packageChgInfoBean.setOptWay("3");
									packageChgList.add(packageChgInfoBean);
								}
							}
	                	} 
					}

					BaseResult retMontDepanResult = this.queryMonternetBusiness(accessId, config, params);
					List<MonternetBusinessBean> listDepanMonBiz = (List<MonternetBusinessBean>)retMontDepanResult.getReObj();
                	if (listDepanMonBiz != null && listDepanMonBiz.size() > 0) {
                		for (MonternetBusinessBean bean : listDepanMonBiz) {
							if ("IIC".equals(bean.getBizCode())) {
	                			PackageChgInfoBean packageChgInfoBean = new PackageChgInfoBean();

								packageChgInfoBean.setPackageType("");
								//飞信：901508 139免费：16
								packageChgInfoBean.setPackageCode(bean.getSpId());
								packageChgInfoBean.setPackageName(bean.getBizName());
								packageChgInfoBean.setOptWay("3");
								packageChgList.add(packageChgInfoBean);
							}
						}
                	} 
                	BaseResult retEmailDepanResult = this.queryEmailBusiness(accessId, config, params);
                	List<CEmailUserAttachDtBean> listEmailBiz = (List<CEmailUserAttachDtBean>)retEmailDepanResult.getReObj();
                	if (listEmailBiz != null && listEmailBiz.size() > 0) {
                		for (CEmailUserAttachDtBean bean : listEmailBiz) {
							if ("+MAILMF".equals(bean.getInfo_value())
									|| "+MAILBZ".equals(bean.getInfo_value())
									|| "+MAILVIP".equals(bean.getInfo_value())) {
	                			PackageChgInfoBean packageChgInfoBean = new PackageChgInfoBean();
								packageChgInfoBean.setPackageType("");
								//飞信：901508 139：16
								packageChgInfoBean.setPackageCode(bean.getBiz_type());
								packageChgInfoBean.setPackageName("139邮箱");
								packageChgInfoBean.setOptWay("3");
								packageChgList.add(packageChgInfoBean);
							}
						}
                	}
                	
                	//会员信息查询
            		BaseResult musicResult = this.queryMusicInfo(accessId, config, params);
    	    		List<CcCGetMusicInfoBean> musicInfoList = (List<CcCGetMusicInfoBean>)musicResult.getReObj();
    	    		if (musicInfoList != null && musicInfoList.size() > 0) {
    	    			for (CcCGetMusicInfoBean bean : musicInfoList) {
    	    				//已经开通无限高级会员
    	    				if ("2".equals(bean.getMember_level())) {
	                			PackageChgInfoBean packageChgInfoBean = new PackageChgInfoBean();
								packageChgInfoBean.setPackageType("");
								//wxgj：无限音乐俱乐部高级会员
								packageChgInfoBean.setPackageCode("122");
								packageChgInfoBean.setPackageName("无限音乐俱乐部高级会员");
								packageChgInfoBean.setOptWay("3");
								packageChgList.add(packageChgInfoBean);
							
    	    				}
    	    			}
    	    		}
        	    	
            
    	    		List<PackageDepend> retList = new ArrayList<PackageDepend>();
    	    		
    	    		List<PackageChgInfoBean> pretList = new ArrayList<PackageChgInfoBean>();
    	    		
    	    		for(PackageChgInfoBean bean : packageChgList) {
    	    			
    	    			bean.setPackageCode(wellFormedDAO.getSysBizCodeParm(bean.getPackageCode()).getBusiNum());
    	    			pretList.add(bean);
    	    		}
    	    		
    	    		//获取必须业务列表
    	    		List<PkgExgNeedBusiInfo> needBusiLst = packageChangeDAO.getPkgExgNeedBusiLst(city, paramNewPckCode, proId);
    	    		for(int j = 0; j < needBusiLst.size(); j++) {
    	    			PackageDepend bean = new PackageDepend();
    	    			for(int i = 0; i<pretList.size(); i++){
        	    			
        	    			if(needBusiLst.get(j).getBusiNumEcp().equals(pretList.get(i).getPackageCode())) {
            	    			bean.setOprateBizName(pretList.get(i).getPackageName());
            	    			bean.setOprateBizCode(pretList.get(i).getPackageCode());
            	    			bean.setOptWay(pretList.get(i).getOptWay());
            	    			bean.setStartDate(pretList.get(i).getStartDate());
            	    			bean.setEndDate(pretList.get(i).getEndDate());
            	    			bean.setNextDate(pretList.get(i).getNextDate());
            	    			//1:开通 0：未开通
            	    			bean.setCurStatus("1");
            	    			//0：可选 1：必选
            	    			bean.setCanChoose("1");
            	    			bean.setOptWay(needBusiLst.get(j).getBusiNumBoss1());
            	    			retList.add(bean);
            	    			break;
        	    			} 
    	    			}
    	    			
    	    		}
    	    		
    	    		for(int i = 0; i<needBusiLst.size(); i++){
		    			for(PackageDepend bean : retList) {
		    				
		    				if(needBusiLst.get(i).getBusiNumEcp().equals(bean.getOprateBizCode())) {
		    					needBusiLst.remove(needBusiLst.get(i));
		    					i=i-1;
		    					break;
		    				}
		    			}
	    		    }
    	    		
    	    		
    	    		List<PackageDepend> pList = new ArrayList<PackageDepend>();
    	    		for(int i = 0; i < needBusiLst.size(); i++) {
    	    			PackageDepend bean = new PackageDepend();
    	    			bean.setOprateBizName(needBusiLst.get(i).getBizName());
    	    			bean.setCurStatus("0");
    	    			bean.setCanChoose("1");
    	    			bean.setOprateBizCode(needBusiLst.get(i).getBusiNumEcp());
    	    			bean.setOptWay(needBusiLst.get(i).getBusiNumBoss1());
    	    			pList.add(bean);
    	    		}
    	    		retList.addAll(pList);
    	    		
    	    		for(int i = 0; i<pretList.size(); i++){
    	    			for(PackageDepend bean : retList) {
    	    				
    	    				if(pretList.get(i).getPackageCode().equals(bean.getOprateBizCode())) {
    	    					pretList.remove(pretList.get(i));
    	    					i=i-1;
    	    					break;
    	    				}
    	    			}
    	    		}
    	    		
    	    		List<PackageDepend> dList = new ArrayList<PackageDepend>();
    	    		
    	    		for(int i = 0; i<pretList.size(); i++){
    	    			PackageDepend b = new PackageDepend();
    	    			b.setOprateBizName(pretList.get(i).getPackageName());
    	    			b.setOprateBizCode(pretList.get(i).getPackageCode());
    	    			b.setCanChoose("0");
    	    			b.setCurStatus("1");
    	    			dList.add(b);
    	    		}
    	    		
    	    		retList.addAll(dList);
    	    		res.setPackageDependList(retList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 获取用户预约产品ID
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getUserBookingProId(final String accessId, final ServiceConfig config, final List<RequestParameter> params,
			final QRY050028Result result) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetproinfo_345", params);

			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetproinfo_345", this.generateCity(params)));
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
	 * 检查必选套餐变更冲突
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param userPackageList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getconflictCheckPkgs(String proId, String productId, final String accessId, final ServiceConfig config,
			final List<RequestParameter> params, final QRY050028Result result) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		
		List<CPlanPackageDtBean> packageCodes = new ArrayList<CPlanPackageDtBean>();
		//返回
		List<PackageChgInfoBean> packageCloseList = new ArrayList<PackageChgInfoBean>();
		
		String oldPckCode = (String)getParameters(params, "oldPckCode");
		String newPckCode = (String)getParameters(params, "newPckCode");
		String pckType = (String)getParameters(params, "packageType");
		
		String[] packageType = new String[]{pckType,pckType};
		String[] packageCode = new String[]{oldPckCode,newPckCode};
		String[] optWay = new String[]{"0","1"};

	    //开通时间次月初	
	    Calendar cal = Calendar.getInstance();  
	    cal.add(Calendar.MONTH,1);
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    cal.add(Calendar.DAY_OF_MONTH, -1);
	    String endDate = sdf.format(cal.getTime());

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_find_package_62_TC", params);

			logger.debug(" ====== 创建订单请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_find_package_62_TC", this.generateCity(params)));
			logger.debug(" ====== 创建订单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050028", "cc_find_package_62_TC", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);

				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
//					取出个人套餐列表
					XPath xpath = XPath.newInstance("/operation_out/content/package_code/cplanpackagedt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					if(list != null && list.size() >0){
						for (int i = 0; i < packageCode.length; i++) {
							if ("0".equals(optWay[i])) {
								for (Element element : list) {
									String pkgCode = element.getChildTextTrim("package_code");
									if (packageCode[i].equals(pkgCode)) {
										CPlanPackageDtBean bean = new CPlanPackageDtBean();
										bean.setCheck_conflict_flag("2");
										bean.setType(element.getChildTextTrim("package_type"));
										bean.setLevel(element.getChildTextTrim("package_level"));
										bean.setCode(element.getChildTextTrim("package_code"));
										bean.setApply_date(element.getChildTextTrim("package_apply_date"));
										bean.setUse_date(element.getChildTextTrim("package_use_date"));
										bean.setHistory_srl(element.getChildTextTrim("package_history_srl"));
										bean.setEnd_date(element.getChildTextTrim("package_end_date"));
										bean.setState(element.getChildTextTrim("package_state"));
										packageCodes.add(bean);									
									}
								}
							}
						}
					}
					//组织旧套餐
					for (int i = 0; i < packageCode.length; i++) {
						if ("1".equals(optWay[i])) {
							CPlanPackageDtBean bean = new CPlanPackageDtBean();
							bean.setCheck_conflict_flag("1");
							bean.setType(packageType[i]);
							bean.setLevel("0");
							bean.setCode(packageCode[i]);
							packageCodes.add(bean);	
						}	
					}	
					//调用验证接口
					params.add(new RequestParameter("oldProId", proId));
					params.add(new RequestParameter("newProId", productId));
					params.add(new RequestParameter("pkgList", packageCodes));
					BaseResult conflictCheckResult = this.doConflictCheck(accessId, config, params, result);
					if(LOGIC_SUCESS.equals(conflictCheckResult.getResultCode())){
						List<CPlanPackageDtBean> retValue = (List<CPlanPackageDtBean>) conflictCheckResult.getReObj();
						if (null != retValue && retValue.size() > 0) {
							params.add(new RequestParameter("codeCount", packageCodes));
							List<ProductBusinessPackageBean>  pList = null;
							for (CPlanPackageDtBean obj:retValue) {
								PackageChgInfoBean pkgBean = new PackageChgInfoBean(); 
								pkgBean.setPackageCode(obj.getCode());
								pkgBean.setPackageType(obj.getType());
								pkgBean.setStartDate(this.fromChar14toStandard(obj.getUse_date()));
								pkgBean.setEndDate(endDate);
								pkgBean.setOptWay("0");
								
								//调用套餐配置信息以查询套餐名称
								BaseResult retPackage =  this.getPkgCfgList(accessId, config, params);
								pList = (List<ProductBusinessPackageBean> )retPackage.getReObj();
								for (ProductBusinessPackageBean objPackage : pList) {
									if (objPackage.getPackageCode().equals(obj.getCode())) {
										//套餐名称
										pkgBean.setPackageName(objPackage.getPackageName());
									}
								}
								
								packageCloseList.add(pkgBean);
							}
						}
					}				
					res.setReObj(packageCloseList);
				}
			}
		}catch (Exception e) {
			logger.error(e, e);
		}
		return res;

	}	

	/**
	 * 套餐冲突校验
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult doConflictCheck(final String accessId, final ServiceConfig config, final List<RequestParameter> params,final QRY050028Result result) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;

		try {
			
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cchkconfpkgmem_351", params);
			// 设置用户产品ID参数
			//paramNew.add(new RequestParameter("chooseFlag", "3"));
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cchkconfpkgmem_351", this.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY050028", "cc_cchkconfpkgmem_351", errCode);
					if (null != errDt)
					{
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}else{
					//设置返回参数
					XPath xpath = XPath.newInstance("/operation_out/content/xxxxxxx/xxxxxxx");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					List<CPlanPackageDtBean> retValue = new ArrayList<CPlanPackageDtBean>();
					for (Element element : list) {
						CPlanPackageDtBean objCPlanPackageDtBean = new CPlanPackageDtBean();
						objCPlanPackageDtBean.setCheck_conflict_flag(element.getChildTextTrim("package_check_conflict_flag"));
						objCPlanPackageDtBean.setUser_id(element.getChildTextTrim("package_user_id"));
						objCPlanPackageDtBean.setLevel(element.getChildTextTrim("package_level"));
						objCPlanPackageDtBean.setCode(element.getChildTextTrim("package_code"));
						objCPlanPackageDtBean.setApply_date(element.getChildTextTrim("package_apply_date"));
						objCPlanPackageDtBean.setUse_date(element.getChildTextTrim("package_use_date"));
						objCPlanPackageDtBean.setHistory_srl(element.getChildTextTrim("package_history_srl"));
						objCPlanPackageDtBean.setEnd_date(element.getChildTextTrim("package_end_date"));
						objCPlanPackageDtBean.setChange_date(element.getChildTextTrim("package_change_date"));
						objCPlanPackageDtBean.setState(element.getChildTextTrim("package_state"));
						objCPlanPackageDtBean.setType(element.getChildTextTrim("package_type"));
            			retValue.add(objCPlanPackageDtBean);
					}
					res.setReObj(retValue);
					
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
			
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
			
////			取出个人套餐列表
//			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cchkconfpkgmem_351", params);
//
//			logger.debug(" ====== 创建订单请求报文 ======\n" + reqXml);
//
//			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cchkconfpkgmem_351", this.generateCity(params)));
//			logger.debug(" ====== 创建订单返回报文 ======\n" + rspXml);
//			if (null != rspXml && !"".equals(rspXml)) {
//				Element root = this.getElement(rspXml.getBytes());
//				String errCode = root.getChild("response").getChildText("resp_code");
//				String errDesc = root.getChild("response").getChildText("resp_desc");
//
//				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
//				if (!BOSS_SUCCESS.equals(errCode)) {
//					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050028", "cc_cchkconfpkgmem_351", errCode);
//					if (null != errDt) {
//						errCode = errDt.getLiErrCode();
//						errDesc = errDt.getLiErrMsg();
//					}
//					res.setErrorCode(errCode);
//					res.setErrorMessage(errDesc);
//
//				}
//				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
//					
//					
//					
////					取出个人套餐列表
//					XPath xpath = XPath.newInstance("/operation_out/content/package_code/cplanpackagedt");
//					List<Element> list = (List<Element>) xpath.selectNodes(root);
//					if(list != null && list.size() >0){
//						for (int i = 0; i < packageCode.length; i++) {
//							if ("0".equals(optWay[i])) {
//								for (Element element : list) {
//									String pkgCode = element.getChildTextTrim("package_code");
//									if (packageCode[i].equals(pkgCode)) {
////										CPlanPackageDtBean bean = new CPlanPackageDtBean();
////										
////										bean.setUser_id(element.getChildTextTrim("package_user_id"));
////										bean.setType(element.getChildTextTrim("package_type"));
////										bean.setLevel(element.getChildTextTrim("package_level"));
////										bean.setCode(element.getChildTextTrim("package_code"));
////										bean.setApply_date(element.getChildTextTrim("package_apply_date"));
////										bean.setUse_date(element.getChildTextTrim("package_use_date"));
////										bean.setHistory_srl(element.getChildTextTrim("package_history_srl"));
////										bean.setEnd_date(element.getChildTextTrim("package_end_date"));
////										bean.setChange_date(element.getChildTextTrim("package_change_date"));
////										bean.setState(element.getChildTextTrim("package_state"));
//										
//										
//										PackageChgInfoBean pkgBean = new PackageChgInfoBean(); 
//										pkgBean.setPackageCode(element.getChildTextTrim("package_code"));
//										pkgBean.setPackageType(element.getChildTextTrim("package_type"));
//										pkgBean.setStartDate(element.getChildTextTrim("package_use_date"));
//										pkgBean.setEndDate(endDate);
//										pkgBean.setOptWay("0");
//
//										packageCloseList.add(pkgBean);									
//									}
//								}
//							}
//						}
//						
//					}
//				}
//			}
//
//		} catch (Exception e) {
//			logger.error(e, e);
//		}
//		return res;
//	}

	/**
	 * 查询用户的套餐
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getProPackCfg(final String accessId, final ServiceConfig config, final List<RequestParameter> params,
			final QRY050028Result result) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetpropackcfg_377", params);

			logger.debug(" ====== 创建订单请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetpropackcfg_377", this.generateCity(params)));
			logger.debug(" ====== 创建订单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050028", "cc_cgetpropackcfg_377", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);

				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					// objBean.setProduct_id((String)map.get("productbasepackagecfg_product_id"));
					// objBean.setCity_id((String)map.get("productbasepackagecfg_city_id"));
					// objBean.setPack_id((String)map.get("productbasepackagecfg_pack_id"));
					// objBean.setBusiness_id((String)map.get("productbasepackagecfg_business_id"));
					// objBean.setStatus((String)map.get("productbasepackagecfg_status"));
					// objBean.setCreate_operator((String)map.get("productbasepackagecfg_create_operator"));
					// objBean.setChange_date((String)map.get("productbasepackagecfg_change_date"));
					// objBean.setChange_operator((String)map.get("productbasepackagecfg_change_operator"));
					// objBean.setCreate_date((String)map.get("productbasepackagecfg_create_date"));
					// objBean.setFreeze_period((String)map.get("productbasepackagecfg_freeze_period"));
					// objBean.setMain_type((String)map.get("productbasepackagecfg_main_type"));
					// objBean.setPack_name((String)map.get("productbasepackagecfg_pack_name"));
					// objBean.setPackage_code((String)map.get("productbasepackagecfg_package_code"));
					XPath xpath = XPath
							.newInstance("/operation_out/content/productbasepackagecfg_product_id/productbasepackagecfgdt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					// 开通时间、关闭时间，次月初
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.MONTH, 1);
					cal.set(Calendar.DAY_OF_MONTH, 1);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					String nextDate = sdf.format(cal.getTime());
					cal.add(Calendar.DAY_OF_MONTH, -1);
					String endDate = sdf.format(cal.getTime());
					List<UserPackage> rtnList = new ArrayList<UserPackage>();
					String userCity = (String) getParameters(params, "context_ddr_city");
					for (Element element : list) {
						String pkgCode = element.getChildTextTrim("productbasepackagecfg_package_code");

						if ("14".equals(userCity)) {
							// 去除动感装备套餐B
							if ("1740".equals(pkgCode)) {
								continue;
							}
						}
						// 去除动感装备套餐B
						if ("1151".equals(pkgCode)) {
							continue;
						}
						// 动感地带网聊
						if ("1431".equals(pkgCode)) {
							continue;
						}
						// 动感地带游戏套餐
						if ("2064".equals(pkgCode)) {
							continue;
						}
						// 去除动感地带音乐套餐
						if ("4991".equals(pkgCode)) {
							continue;
						}
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
		// String key = "PRO_CFG_LIST_";
		List<ProductBusinessPackageBean> pkgCfgList = null;
		try {
			// for (RequestParameter param:params) {
			// if ("city".equals(param.getParameterName())) {
			// key += param.getParameterValue();
			// }
			// }
			//			
			// Object obj = this.wellFormedDAO.getCache().get(key);
			// if(obj != null && obj instanceof List)
			// {
			// proCfgList = (List<CcCGetProByCityBean>)obj;
			// } else {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetpackbycode_605", params);

			logger.debug(" ====== 查询套餐配置请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetpackbycode_605", this.generateCity(params)));
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
						productbusinesspackage_package_code = root.getChild("content").getChildren(
								"productbusinesspackage_package_code");
					} catch (Exception e) {
						productbusinesspackage_package_code = null;
					}
					if (null != productbusinesspackage_package_code && productbusinesspackage_package_code.size() > 0) {
						pkgCfgList = new ArrayList<ProductBusinessPackageBean>(productbusinesspackage_package_code.size());
						ProductBusinessPackageBean bean = null;
						for (int i = 0; i < productbusinesspackage_package_code.size(); i++) {
							bean = new ProductBusinessPackageBean();
							Element cproductbusinesspackagedt = ((Element) productbusinesspackage_package_code.get(i))
									.getChild("cproductbusinesspackagedt");
							if (null != cproductbusinesspackagedt) {
								bean.setBossToolFlag(p.matcher(
										cproductbusinesspackagedt.getChildText("productbusinesspackage_bosstool_flag"))
										.replaceAll(""));
								bean
										.setWebFlag(p.matcher(
												cproductbusinesspackagedt.getChildText("productbusinesspackage_web_flag"))
												.replaceAll(""));
								bean.setBossViewFlag(p.matcher(
										cproductbusinesspackagedt.getChildText("productbusinesspackage_bossview_flag"))
										.replaceAll(""));
								bean.setCityId(p
										.matcher(cproductbusinesspackagedt.getChildText("productbusinesspackage_city_id"))
										.replaceAll(""));
								bean.setPackageName(p.matcher(
										cproductbusinesspackagedt.getChildText("productbusinesspackage_package_name"))
										.replaceAll(""));
								bean
										.setEndDate(p.matcher(
												cproductbusinesspackagedt.getChildText("productbusinesspackage_end_date"))
												.replaceAll(""));
								bean.setPackageDesc(p.matcher(
										cproductbusinesspackagedt.getChildText("productbusinesspackage_package_desc"))
										.replaceAll(""));
								bean.setPackageClass(p.matcher(
										cproductbusinesspackagedt.getChildText("productbusinesspackage_package_class"))
										.replaceAll(""));
								bean.setFlag1860(p.matcher(
										cproductbusinesspackagedt.getChildText("productbusinesspackage_flag_1860"))
										.replaceAll(""));
								bean
										.setCuiFlag(p.matcher(
												cproductbusinesspackagedt.getChildText("productbusinesspackage_cui_flag"))
												.replaceAll(""));
								bean.setIsNowclose(p.matcher(
										cproductbusinesspackagedt.getChildText("productbusinesspackage_is_nowclose")).replaceAll(
										""));
								bean.setIsNowopen(p.matcher(
										cproductbusinesspackagedt.getChildText("productbusinesspackage_is_nowopen")).replaceAll(
										""));
								bean.setOpenChoice(p.matcher(
										cproductbusinesspackagedt.getChildText("productbusinesspackage_open_choice")).replaceAll(
										""));
								bean.setPreChoice(p.matcher(
										cproductbusinesspackagedt.getChildText("productbusinesspackage_pre_choice")).replaceAll(
										""));
								bean.setStartDate(p.matcher(
										cproductbusinesspackagedt.getChildText("productbusinesspackage_start_date")).replaceAll(
										""));
								bean.setPackageLevel(p.matcher(
										cproductbusinesspackagedt.getChildText("productbusinesspackage_package_level"))
										.replaceAll(""));
								bean.setPackageCode(p.matcher(
										cproductbusinesspackagedt.getChildText("productbusinesspackage_package_code"))
										.replaceAll(""));
								bean.setBusinessId(p.matcher(
										cproductbusinesspackagedt.getChildText("productbusinesspackage_business_id")).replaceAll(
										""));

								pkgCfgList.add(bean);
							}
						}
						// this.wellFormedDAO.getCache().add(key, proCfgList);
						res.setReObj(pkgCfgList);
					}
				}
			}
			// }
			// res.setReObj(proCfgList);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 获取用户预约产品ID
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getSpUserInfo(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetspuserinfo_358", params);

			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetspuserinfo_358", this.generateCity(params)));
			logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050028", "cc_cgetspuserinfo_358", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);

				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath.newInstance("/operation_out/content/selfplatuserreg_reg_id/cselfplatuserregdt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					for (Element element : list) {
						if ("80".equals(element.getChildTextTrim("selfplatuserreg_biz_code")) && "200501".equals(element.getChildTextTrim("selfplatuserreg_prd_code"))) {
							res.setReObj(true);
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
	 * 根据套餐查询依赖业务
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getDepend(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		List<GetDependBean> rtnList = new ArrayList<GetDependBean>();
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetdepend_381", params);
			
			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);
			
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetdepend_381", this.generateCity(params)));
			logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050020", "cc_cgetdepend_381", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					
				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath.newInstance("/operation_out/content/productdependcfg_depend_type/cproductdependcfgdt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					for (Element element : list) {
						GetDependBean objBean = new GetDependBean();  
            			objBean.setDepend_type(element.getChildTextTrim("productdependcfg_depend_type"));
            			objBean.setDepend_code(element.getChildTextTrim("productdependcfg_depend_code"));
            			objBean.setDepend_sub_code(element.getChildTextTrim("productdependcfg_depend_sub_code"));
            			objBean.setDepend_type(element.getChildTextTrim("productdependcfg_depended_type"));
            			objBean.setDepend_code(element.getChildTextTrim("productdependcfg_depended_code"));
            			objBean.setDepended_sub_code(element.getChildTextTrim("productdependcfg_depended_sub_code"));
            			objBean.setDepend_relation(element.getChildTextTrim("productdependcfg_depend_relation"));
            			objBean.setDepend_opt_mode(element.getChildTextTrim("productdependcfg_depend_opt_mode"));
            			objBean.setOperator_id(element.getChildTextTrim("productdependcfg_operator_id"));
            			objBean.setOperator_date(element.getChildTextTrim("productdependcfg_operator_date"));
            			rtnList.add(objBean);
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
	 * 需要提示关闭的套餐
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getUserAddonSingle(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		List<UserAddonFunctionBean> rtnList = new ArrayList<UserAddonFunctionBean>();
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetsrvinfo_601", params);
			
			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);
			
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetsrvinfo_601", this.generateCity(params)));
			logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050020", "cc_cgetsrvinfo_601", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					
				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath.newInstance("/operation_out/content/sv_opt_applydate/cuserserviceoptdt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					for (Element element : list) {
	                    UserAddonFunctionBean addonBean = new UserAddonFunctionBean();
	                    addonBean.setUsrid(element.getChildTextTrim("sv_opt_usrid"));
	                    addonBean.setSvcode(element.getChildTextTrim("sv_opt_svcode"));
	                    addonBean.setApplydate(element.getChildTextTrim("sv_opt_applydate"));
	                    addonBean.setState(element.getChildTextTrim("sv_opt_state"));
	                    addonBean.setEnddate(element.getChildTextTrim("sv_opt_enddate"));
	                    addonBean.setChgdate(element.getChildTextTrim("sv_opt_chgdate"));
	                    addonBean.setStartdate(element.getChildTextTrim("sv_opt_startdate"));
	                    addonBean.setHissrl(element.getChildTextTrim("sv_opt_hissrl"));
	                    addonBean.setOperating(element.getChildTextTrim("sv_opt_operating"));
	                    
            			rtnList.add(addonBean);
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
	 * 需要提示关闭的套餐
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getIncInfo(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		List<GetIncInfoBean> rtnList = new ArrayList<GetIncInfoBean>();
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetincinfo_346", params);
			
			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);
			
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetincinfo_346", this.generateCity(params)));
			logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050020", "cc_cgetincinfo_346", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					
				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath.newInstance("/operation_out/content/smscall_deal_code/csmscalldt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					for (Element element : list) {
						GetIncInfoBean objCcCGetIncInfoBean = new GetIncInfoBean();
	                    objCcCGetIncInfoBean.setSmscall_state(element.getChildTextTrim("smscall_state"));
	                    objCcCGetIncInfoBean.setSmscall_start_date(element.getChildTextTrim("smscall_start_date"));
	                    objCcCGetIncInfoBean.setSmscall_operating_srl(element.getChildTextTrim("smscall_operating_srl"));
	                    objCcCGetIncInfoBean.setSmscall_reserved1(element.getChildTextTrim("smscall_reserved1"));
	                    objCcCGetIncInfoBean.setSmscall_reserved2(element.getChildTextTrim("smscall_reserved2"));
	                    objCcCGetIncInfoBean.setSmscall_remark(element.getChildTextTrim("smscall_remark"));
	                    objCcCGetIncInfoBean.setSmscall_deal_code(element.getChildTextTrim("smscall_deal_code"));
	                    objCcCGetIncInfoBean.setSmscall_end_date(element.getChildTextTrim("smscall_end_date"));
	                    objCcCGetIncInfoBean.setSmscall_gsm_user_id(element.getChildTextTrim("smscall_gsm_user_id"));
	                    objCcCGetIncInfoBean.setSmscall_apply_date(element.getChildTextTrim("smscall_apply_date"));
	                    
            			rtnList.add(objCcCGetIncInfoBean);
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
	 * 查询梦网业务
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult queryMonternetBusiness(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		List<MonternetBusinessBean> rtnList = new ArrayList<MonternetBusinessBean>();
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqryspuserreg_65", params);
			
			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);
			
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cqryspuserreg_65", this.generateCity(params)));
			logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050028", "cc_cqryspuserreg_65", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					
				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
 					XPath xpath = XPath.newInstance("/operation_out/content/spbizreg_gsm_user_id/cspbizregdt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					for (Element element : list) {
	
						MonternetBusinessBean monternetBean = new MonternetBusinessBean();
						monternetBean.setBizType(element.getChildTextTrim("spbizreg_biz_type"));

						monternetBean.setBizCode(element.getChildTextTrim("spbizreg_sub_biz_val"));
						monternetBean.setBizName(element.getChildTextTrim("spbizreg_busi_name"));
						monternetBean.setBizDesc(element.getChildTextTrim("spbizreg_biz_desc"));
						monternetBean.setSpId(element.getChildTextTrim("spbizreg_sub_biz_type"));
						monternetBean.setSpServiceId(element.getChildTextTrim("spbizreg_sp_svc_id"));
						monternetBean.setSpShortName(element.getChildTextTrim("spbizreg_sp_name"));
						monternetBean.setTimeOpened(element.getChildTextTrim("spbizreg_effect_time"));			
						
						monternetBean.setBillingType(element.getChildTextTrim("spbizreg_fee_type"));
						monternetBean.setPrice(element.getChildTextTrim("spbizreg_price"));
						monternetBean.setStatus(element.getChildTextTrim("spbizreg_status"));
						//add by liuhh 营养百科添加判断
						monternetBean.setTimeEnded(element.getChildTextTrim("spbizreg_end_time"));		
						rtnList.add(monternetBean);
	            
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
	 * 查询139邮箱
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult queryEmailBusiness(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		List<CEmailUserAttachDtBean> rtnList = new ArrayList<CEmailUserAttachDtBean>();
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqryspuserreg_65", params);
			
			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);
			
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cqryspuserreg_65", this.generateCity(params)));
			logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050028", "cc_cqryspuserreg_65", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					
				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
 					XPath xpath = XPath.newInstance("/operation_out/content/emailuserattach_user_id/cemailuserattachdt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					String emailuserattach_active_flag = "";
					for (Element element : list) {
						
						emailuserattach_active_flag = element.getChildTextTrim("emailuserattach_active_flag").trim();
						if ("1".equals(emailuserattach_active_flag)) {			
							CEmailUserAttachDtBean emailBean = new CEmailUserAttachDtBean();
							//monternetBean.setBizType(element.getChildTextTrim("spbizreg_biz_type"));
							
							emailBean.setCreate_date(element.getChildTextTrim( "emailuserattach_create_date"));
							emailBean.setCreate_operator(element.getChildTextTrim( "emailuserattach_create_operator"));
							emailBean.setChange_date(element.getChildTextTrim( "emailuserattach_change_date"));
							emailBean.setEnd_date(element.getChildTextTrim( "emailuserattach_end_date"));
							emailBean.setBegin_date(element.getChildTextTrim( "emailuserattach_begin_date"));
							emailBean.setInfo_value(element.getChildTextTrim( "emailuserattach_info_value"));
							emailBean.setInfo_code(element.getChildTextTrim( "emailuserattach_info_code"));
							emailBean.setBiz_type(element.getChildTextTrim( "emailuserattach_biz_type"));
							emailBean.setMsisdn(element.getChildTextTrim( "emailuserattach_msisdn"));
							emailBean.setUser_id(element.getChildTextTrim( "emailuserattach_user_id"));
							emailBean.setActive_flag(element.getChildTextTrim( "emailuserattach_active_flag"));
							emailBean.setChange_operator(element.getChildTextTrim( "emailuserattach_change_operator"));
							
							rtnList.add(emailBean);
						}
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
	 * 查询无线音乐俱乐部
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult queryMusicInfo(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		List<CEmailUserAttachDtBean> rtnList = new ArrayList<CEmailUserAttachDtBean>();
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetmusicinfo_391", params);
			
			logger.debug(" ====== 无线音乐请求报文 ======\n" + reqXml);
			
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetmusicinfo_391", this.generateCity(params)));
			logger.debug(" ====== 无线音乐返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050028", "cc_cgetmusicinfo_391", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					
				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					List<CcCGetMusicInfoBean> musicInfoList = new ArrayList<CcCGetMusicInfoBean>();
 					XPath xpath = XPath.newInstance("/operation_out/content/musicclubmember_operating_srl/cmusicclubmemberdt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					if(list != null){
						for (Element element : list) {	
							CcCGetMusicInfoBean bean = new CcCGetMusicInfoBean();
							bean.setOperating_srl(element.getChildTextTrim("musicclubmember_operating_srl"));
							bean.setGsm_user_id(element.getChildTextTrim("musicclubmember_gsm_user_id"));
							bean.setMsisdn(element.getChildTextTrim("musicclubmember_msisdn"));
							bean.setMember_level(element.getChildTextTrim("musicclubmember_member_level"));
							bean.setPackage_id(element.getChildTextTrim("musicclubmember_package_id"));
							bean.setStart_date(element.getChildTextTrim("musicclubmember_start_date"));
							bean.setEnd_date(element.getChildTextTrim("musicclubmember_end_date"));
							bean.setStatus(element.getChildTextTrim("musicclubmember_status"));
							bean.setOperating_src(element.getChildTextTrim("musicclubmember_operating_src"));
							bean.setLast_update_date(element.getChildTextTrim("musicclubmember_last_update_date"));
							bean.setCreate_operator(element.getChildTextTrim("musicclubmember_create_operator"));
							bean.setCreate_org_id(element.getChildTextTrim("musicclubmember_create_org_id"));
							bean.setChange_operator(element.getChildTextTrim("musicclubmember_change_operator"));
							bean.setChange_date(element.getChildTextTrim("musicclubmember_change_date"));
							bean.setChange_remark(element.getChildTextTrim("musicclubmember_change_remark"));
							bean.setActive_flag(element.getChildTextTrim("musicclubmember_active_flag"));
							musicInfoList.add(bean);
						}
					}
					res.setReObj(musicInfoList);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 查询梦网业务2
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult queryMonternet(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		List<MonternetBusinessBean> rtnList = new ArrayList<MonternetBusinessBean>();
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqrymonternet_73", params);
			
			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);
			
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cqrymonternet_73", this.generateCity(params)));
			logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050028", "cc_cqrymonternet_73", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					
				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath.newInstance("/operation_out/content/monternetcfg_spid/cmonternetcfgdt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					for (Element element : list) {
	
						MonternetBusinessBean monternetBean = new MonternetBusinessBean();
		
						monternetBean.setBizType(element.getChildTextTrim( "monternetcfg_biztypecode"));
						monternetBean.setBizTypeName(element.getChildTextTrim( "monternetcfg_biztype"));
						monternetBean.setBizCode(element.getChildTextTrim( "monternetcfg_bizcode"));
						monternetBean.setBizName(element.getChildTextTrim( "monternetcfg_bizname"));
						monternetBean.setBizDesc(element.getChildTextTrim( "monternetcfg_bizdesc"));
						monternetBean.setSpId(element.getChildTextTrim( "monternetcfg_spid"));
						monternetBean.setSpServiceId(element.getChildTextTrim( "monternetcfg_spsvcid"));
						monternetBean.setSpShortName(element.getChildTextTrim( "monternetcfg_spshortname"));			
						monternetBean.setAccessModel(element.getChildTextTrim( "monternetcfg_accessmodel"));
						monternetBean.setBillingType(element.getChildTextTrim( "monternetcfg_billingtype"));
						monternetBean.setPrice(element.getChildTextTrim( "monternetcfg_price"));
						monternetBean.setCityId(element.getChildTextTrim( "monternetcfg_city_id"));
						rtnList.add(monternetBean);
	            
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
	 * 查询自由业务配置信息
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult querySpChg(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		List<CSelfPlatBizCfgDtBean> rtnList_fun = new ArrayList<CSelfPlatBizCfgDtBean>();
		List<CcCGetSpBizDetailBean> rtnList_biz = new ArrayList<CcCGetSpBizDetailBean>();
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetspbizdetail_528", params);
			
			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);
			
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetspbizdetail_528", this.generateCity(params)));
			logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050028", "cc_cgetspbizdetail_528", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					
				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					Map retValue = new HashMap(2);
					XPath xpath_fun = XPath.newInstance("/operation_out/content/selfplatbizcfg_function_id/cselfplatbizcfgdt");
					XPath xpath_biz = XPath.newInstance("/operation_out/content/selfplatproductcfg_biz_code/cselfplatproductcfgdt");
					
					List<Element> list_fun = (List<Element>) xpath_fun.selectNodes(root);
					List<Element> list_biz = (List<Element>) xpath_biz.selectNodes(root);
					for (Element element : list_fun) {
	
						CSelfPlatBizCfgDtBean funBean = new CSelfPlatBizCfgDtBean();
							
						
						funBean.setMore_choose_flag(element.getChildTextTrim( "基本产品是可多选标志"));
						funBean.setCan_order(element.getChildTextTrim( "业务是否可订购"));
						funBean.setUnification_search(element.getChildTextTrim( "梦网统一查询退订界面是否展现标志"));
						funBean.setCan_donate(element.getChildTextTrim( "业务是否可赠送"));
						funBean.setCan_booking(element.getChildTextTrim( "业务是否可预约"));
						funBean.setDomain_code(element.getChildTextTrim( "平台编码"));
						funBean.setBiz_code(element.getChildTextTrim( "业务编码"));
						funBean.setBiz_name(element.getChildTextTrim( "业务名称"));
						funBean.setBiz_description(element.getChildTextTrim( "业务说明"));
						funBean.setTest_flag(element.getChildTextTrim( "测试标记"));
						rtnList_fun.add(funBean);
	            
					}
					for (Element element : list_biz) {
						
						CcCGetSpBizDetailBean bizBean = new CcCGetSpBizDetailBean();
							
						
						bizBean.setBizCode(element.getChildTextTrim( "业务编码"));
						bizBean.setPrdCode(element.getChildTextTrim( "产品编码"));
						bizBean.setRelationType(element.getChildTextTrim( "产品关系类型"));
						bizBean.setPrdCanOrder(element.getChildTextTrim( "产品是否可订购"));
						bizBean.setChooseFlag(element.getChildTextTrim( "是否必选标志"));
						bizBean.setPrdDiscription(element.getChildTextTrim( "产品描述"));
						bizBean.setPrdName(element.getChildTextTrim( "产品名称"));
						bizBean.setFixFeeMonth(element.getChildTextTrim( "月固定收费金额"));
						rtnList_biz.add(bizBean);
	            
					}
					
					
					
					
					retValue.put("selfplatbizcfg_function_id", rtnList_fun);
					retValue.put("selfplatproductcfg_biz_code", rtnList_biz);
					
					res.setReObj(retValue);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 查询梦网业务2
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getSpInfo(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		List<SelfPlatBean> rtnList = new ArrayList<SelfPlatBean>();
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetspuserinfo_358", params);
			
			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);
			
			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetspuserinfo_358", this.generateCity(params)));
			logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050028", "cc_cgetspuserinfo_358", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					
				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath.newInstance("/operation_out/content/selfplatuserreg_reg_id/cselfplatuserregdt");
					List<Element> list = (List<Element>) xpath.selectNodes(root);
					for (Element element : list) {

						SelfPlatBean objSelfPlatBean = new SelfPlatBean();
					    objSelfPlatBean.setDomainCode(element.getChildTextTrim( "selfplatuserreg_domain_code"));
                        objSelfPlatBean.setBizCode(element.getChildTextTrim( "selfplatuserreg_biz_code"));
                        objSelfPlatBean.setPrdCode(element.getChildTextTrim( "selfplatuserreg_prd_code"));
                        objSelfPlatBean.setBeginDate(element.getChildTextTrim( "selfplatuserreg_begin_date"));
                        objSelfPlatBean.setEndDate(element.getChildTextTrim( "selfplatuserreg_end_date"));
                        objSelfPlatBean.setStatus(element.getChildTextTrim( "selfplatuserreg_status"));
						
						
						rtnList.add(objSelfPlatBean);
	            
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
	
	/**
	 * 附加功能字典
	 * @return
	 */
	private Map<String, String> getAdjFunDic() {
		if (mapAdjFunDic == null) {
			mapAdjFunDic = new HashMap<String, String>();
			mapAdjFunDic.put("1", "台港澳 国际漫游");
			mapAdjFunDic.put("2", "省际漫游");
			mapAdjFunDic.put("3", "省内漫游");
			mapAdjFunDic.put("4", "短信");
			mapAdjFunDic.put("5", "语音信箱");
			mapAdjFunDic.put("6", "传真");
			mapAdjFunDic.put("7", "数据");
			mapAdjFunDic.put("8", "台港澳 国际长权");
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
	 * 把14字符的格式日期字符串转换成日期
	 * @param char14  14字符的格式日期字符串，如2008-01-08
	 * @return Date
	 */
	public static String fromChar14toStandard(String charDateTime)
	{
		
		String  strResult = "";
		if(null != charDateTime && !"".equals(charDateTime) && charDateTime.length() >= 8){
			strResult=charDateTime.substring(0, 4) + "-"
			+ charDateTime.substring(4, 6) + "-"
		    + charDateTime.substring(6, 8);
		}
		return strResult;
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

	
	/**
	 * 套餐冲突校验的请求参数Bean
	 * 
	 */
	public class CPlanPackageDtBean {
		private String check_conflict_flag;

		private String user_id;

		private String type;

		private String level;

		private String code;

		private String apply_date;

		private String use_date;

		private String history_srl;

		private String end_date;

		private String change_date;

		private String state;

		public CPlanPackageDtBean() {
			super();
			this.check_conflict_flag = "";
			this.user_id = "";
			this.type = "";
			this.level = "";
			this.code = "";
			this.apply_date = "";
			this.use_date = "";
			this.history_srl = "";
			this.end_date = "";
			this.change_date = "";
			this.state = "";
		}

		public CPlanPackageDtBean(String check_conflict_flag, String user_id, String type, String level, String code, String apply_date, String use_date, String history_srl, String end_date, String change_date, String state) {
			super();
			this.check_conflict_flag = check_conflict_flag;
			this.user_id = user_id;
			this.type = type;
			this.level = level;
			this.code = code;
			this.apply_date = apply_date;
			this.use_date = use_date;
			this.history_srl = history_srl;
			this.end_date = end_date;
			this.change_date = change_date;
			this.state = state;
		}

		public String getApply_date() {
			return apply_date;
		}

		public void setApply_date(String apply_date) {
			this.apply_date = apply_date;
		}

		public String getChange_date() {
			return change_date;
		}

		public void setChange_date(String change_date) {
			this.change_date = change_date;
		}

		public String getCheck_conflict_flag() {
			return check_conflict_flag;
		}

		public void setCheck_conflict_flag(String check_conflict_flag) {
			this.check_conflict_flag = check_conflict_flag;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getEnd_date() {
			return end_date;
		}

		public void setEnd_date(String end_date) {
			this.end_date = end_date;
		}

		public String getHistory_srl() {
			return history_srl;
		}

		public void setHistory_srl(String history_srl) {
			this.history_srl = history_srl;
		}

		public String getLevel() {
			return level;
		}

		public void setLevel(String level) {
			this.level = level;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getUse_date() {
			return use_date;
		}

		public void setUse_date(String use_date) {
			this.use_date = use_date;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

	}

	/**
	 * 套餐变更信息
	 * 
	 */
	public class PackageChgInfoBean {
		
		private String packageType;
		
		private String packageCode;
		
		private String packageName;
		
		private String optWay;
		
		private String startDate;
		
		private String endDate;
		
		private String nextDate;

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public String getNextDate() {
			return nextDate;
		}

		public String getPackageType() {
			return packageType;
		}

		public void setPackageType(String packageType) {
			this.packageType = packageType;
		}

		public void setNextDate(String nextDate) {
			this.nextDate = nextDate;
		}

		public String getOptWay() {
			return optWay;
		}

		public void setOptWay(String optWay) {
			this.optWay = optWay;
		}

		public String getPackageCode() {
			return packageCode;
		}

		public void setPackageCode(String packageCode) {
			this.packageCode = packageCode;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

	}
	/**
	 * 根据套餐查询依赖业务Bean
	 * 
	 * @author 孙林
	 * 
	 */
	public class GetDependBean {
		
		private String depend_type;
		
		private String depend_code;
		
		private String depend_sub_code;
		
		private String depended_type;
		
		private String depended_code;
		
		private String depended_sub_code;
		
		private String depend_relation;
		
		private String depend_opt_mode;
		
		private String operator_id;
		
		private String operator_date;

		public String getDepend_code() {
			return depend_code;
		}

		public void setDepend_code(String depend_code) {
			this.depend_code = depend_code;
		}

		public String getDepend_opt_mode() {
			return depend_opt_mode;
		}

		public void setDepend_opt_mode(String depend_opt_mode) {
			this.depend_opt_mode = depend_opt_mode;
		}

		public String getDepend_relation() {
			return depend_relation;
		}

		public void setDepend_relation(String depend_relation) {
			this.depend_relation = depend_relation;
		}

		public String getDepend_sub_code() {
			return depend_sub_code;
		}

		public void setDepend_sub_code(String depend_sub_code) {
			this.depend_sub_code = depend_sub_code;
		}

		public String getDepend_type() {
			return depend_type;
		}

		public void setDepend_type(String depend_type) {
			this.depend_type = depend_type;
		}

		public String getDepended_code() {
			return depended_code;
		}

		public void setDepended_code(String depended_code) {
			this.depended_code = depended_code;
		}

		public String getDepended_sub_code() {
			return depended_sub_code;
		}

		public void setDepended_sub_code(String depended_sub_code) {
			this.depended_sub_code = depended_sub_code;
		}

		public String getDepended_type() {
			return depended_type;
		}

		public void setDepended_type(String depended_type) {
			this.depended_type = depended_type;
		}

		public String getOperator_date() {
			return operator_date;
		}

		public void setOperator_date(String operator_date) {
			this.operator_date = operator_date;
		}

		public String getOperator_id() {
			return operator_id;
		}

		public void setOperator_id(String operator_id) {
			this.operator_id = operator_id;
		}

	}
	/**
	 * 用户附加功能的信息实体
	 */
	public class UserAddonFunctionBean {
		private String usrid;
		private String svcode;
		private String applydate;
		private String startdate;
		private String enddate;
		private String chgdate;
		private String hissrl;
		private String operating;
		private String state;
		
		public String getUsrid() {
			return usrid;
		}
		public void setUsrid(String usrid) {
			this.usrid = usrid;
		}
		public String getSvcode() {
			return svcode;
		}
		public void setSvcode(String svcode) {
			this.svcode = svcode;
		}
		public String getApplydate() {
			return applydate;
		}
		public void setApplydate(String applydate) {
			this.applydate = applydate;
		}
		public String getStartdate() {
			return startdate;
		}
		public void setStartdate(String startdate) {
			this.startdate = startdate;
		}
		public String getEnddate() {
			return enddate;
		}
		public void setEnddate(String enddate) {
			this.enddate = enddate;
		}
		public String getChgdate() {
			return chgdate;
		}
		public void setChgdate(String chgdate) {
			this.chgdate = chgdate;
		}
		public String getHissrl() {
			return hissrl;
		}
		public void setHissrl(String hissrl) {
			this.hissrl = hissrl;
		}
		public String getOperating() {
			return operating;
		}
		public void setOperating(String operating) {
			this.operating = operating;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
	}

	/**
	 * 产品增值业务列表查询业务Bean
	 *
	 */
	public class GetIncInfoBean {
		private String smscall_state;
		
		private String smscall_start_date;
		
		private String smscall_operating_srl;
		
		private String smscall_reserved1;
		
		private String smscall_reserved2;
		
		private String smscall_remark;
		
		private String smscall_deal_code;
		
		private String smscall_end_date;
		
		private String smscall_gsm_user_id;
		
		private String smscall_apply_date;

		public String getSmscall_apply_date() {
			return smscall_apply_date;
		}

		public void setSmscall_apply_date(String smscall_apply_date) {
			this.smscall_apply_date = smscall_apply_date;
		}

		public String getSmscall_deal_code() {
			return smscall_deal_code;
		}

		public void setSmscall_deal_code(String smscall_deal_code) {
			this.smscall_deal_code = smscall_deal_code;
		}

		public String getSmscall_end_date() {
			return smscall_end_date;
		}

		public void setSmscall_end_date(String smscall_end_date) {
			this.smscall_end_date = smscall_end_date;
		}

		public String getSmscall_gsm_user_id() {
			return smscall_gsm_user_id;
		}

		public void setSmscall_gsm_user_id(String smscall_gsm_user_id) {
			this.smscall_gsm_user_id = smscall_gsm_user_id;
		}

		public String getSmscall_operating_srl() {
			return smscall_operating_srl;
		}

		public void setSmscall_operating_srl(String smscall_operating_srl) {
			this.smscall_operating_srl = smscall_operating_srl;
		}

		public String getSmscall_remark() {
			return smscall_remark;
		}

		public void setSmscall_remark(String smscall_remark) {
			this.smscall_remark = smscall_remark;
		}

		public String getSmscall_reserved1() {
			return smscall_reserved1;
		}

		public void setSmscall_reserved1(String smscall_reserved1) {
			this.smscall_reserved1 = smscall_reserved1;
		}

		public String getSmscall_reserved2() {
			return smscall_reserved2;
		}

		public void setSmscall_reserved2(String smscall_reserved2) {
			this.smscall_reserved2 = smscall_reserved2;
		}

		public String getSmscall_start_date() {
			return smscall_start_date;
		}

		public void setSmscall_start_date(String smscall_start_date) {
			this.smscall_start_date = smscall_start_date;
		}

		public String getSmscall_state() {
			return smscall_state;
		}

		public void setSmscall_state(String smscall_state) {
			this.smscall_state = smscall_state;
		}


	}

	/**
	 * 梦网业务ean
	 *
	 */
	public class MonternetBusinessBean {
		
		/** 业务代码 */
		private String bizCode;
		
		/** 业务名称 */
		private String bizName;
		
		/** 业务类型 */
		private String bizType;
		
		/** 业务类型名称 */
		private String bizTypeName;
		
		/** 业务描述 */
		private String bizDesc;
		
		/** SP代码 */
		private String spId;
		
		/** SP服务代码 */
		private String spServiceId;
		
		/** SP简称 */
		private String spShortName;
		
		/** 费率（单位：厘） */
		private String price;
		
		/** 开通时间 */
		private String timeOpened;
		
		/** 关闭时间 */
		private String timeEnded;
		
		/** 状态 */
		private String status;
		
		/**
		 * 计费类型  0-免费 1-按条计费 2-包月计费 3-包时计费 4-包次计费 
		 */
		private String billingType;
		
		/** 地市 */
		private String cityId;
		
		/** 访问模式 */
		private String accessModel;

		/** 功能编码 */
		private String functionCode = "000000";
		/** 功能编码 */
		private String remark = "";
		
		public String getBillingType() {
			return billingType;
		}

		public void setBillingType(String billingType) {
			this.billingType = billingType;
		}

		public String getBizCode() {
			return bizCode;
		}

		public void setBizCode(String bizCode) {
			this.bizCode = bizCode;
		}

		public String getBizDesc() {
			return bizDesc;
		}

		public void setBizDesc(String bizDesc) {
			this.bizDesc = bizDesc;
		}

		public String getBizName() {
			return bizName;
		}

		public void setBizName(String bizName) {
			this.bizName = bizName;
		}

		public String getBizType() {
			return bizType;
		}

		public void setBizType(String bizType) {
			this.bizType = bizType;
		}

		public String getCityId() {
			return cityId;
		}

		public void setCityId(String cityId) {
			this.cityId = cityId;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getSpId() {
			return spId;
		}

		public void setSpId(String spId) {
			this.spId = spId;
		}

		public String getSpServiceId() {
			return spServiceId;
		}

		public void setSpServiceId(String spServiceId) {
			this.spServiceId = spServiceId;
		}

		public String getSpShortName() {
			return spShortName;
		}

		public void setSpShortName(String spShortName) {
			this.spShortName = spShortName;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getTimeOpened() {
			return timeOpened;
		}

		public String getTimeEnded() {
			return timeEnded;
		}

		public void setTimeEnded(String timeEnded) {
			this.timeEnded = timeEnded;
		}

		public void setTimeOpened(String timeOpened) {
			this.timeOpened = timeOpened;
		}

		public String getBizTypeName() {
			return bizTypeName;
		}

		public void setBizTypeName(String bizTypeName) {
			this.bizTypeName = bizTypeName;
		}

		public String getAccessModel() {
			return accessModel;
		}

		public void setAccessModel(String accessModel) {
			this.accessModel = accessModel;
		}

		public String getFunctionCode()
		{
			return functionCode;
		}

		public void setFunctionCode(String functionCode)
		{
			this.functionCode = functionCode;
		}

		public String getRemark()
		{
			return remark;
		}

		public void setRemark(String remark)
		{
			this.remark = remark;
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

	/**
	 * 查询自有业务配置信息下的cselfplatbizcfgdt业务Bean
	 * 
	 * @author 吴宗德
	 * 
	 */
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
	public class SelfPlatBean {

		/** 平台编码 */
		private String domainCode;
		
		/** 业务编码 */
		private String bizCode;
		
		/** 产品编码 */
		private String prdCode;
		
		/** 附加产品编码 */
		private String prdCode1;
		
		/** 生效时间 */
		private String beginDate;
		
		/** 失效时间 */
		private String endDate;
		
		/** 赠送号码 */
		private String donateMsisdn;
		
		/** 赠送标记 */
		private String donateFlag;	
		
		/** 状态0：预约1：正常2：暂停3：已退定 */
		private String status;
		
		/** 0:立即1：次月3:立即和次月 */
		private String chooseFlag;
		
		/** 0：关闭1：开通2：变更3:无 */
		private String operate;
		
		/** 出生日期（必填） + “TAB分隔符”+用户姓名 */
		private String addInfo1;
		
		/** 信息提供商+“TAB分隔符”+发展代理商信息 */
		private String addInfo2;
		/** 当前自有业务状态 */
		private String selfPlatState;
		/** 无参构造方法 */
		public SelfPlatBean() {
			super();
			this.domainCode = "";
			this.bizCode = "";
			this.prdCode = "";
			this.prdCode1 = "";
			this.beginDate = "";
			this.endDate = "";
			this.donateMsisdn = "";
			this.donateFlag = "";
			this.status = "";
			this.chooseFlag = "";
			this.operate = "";
			this.addInfo1 = "";
			this.addInfo2 = "";
			this.selfPlatState = "";
		}

		/** 构造方法 */
		public SelfPlatBean(String domainCode, String bizCode, String prdCode) {
			super();
			this.domainCode = domainCode;
			this.bizCode = bizCode;
			this.prdCode = prdCode;
			this.prdCode1 = "";
			this.beginDate = "";
			this.endDate = "";
			this.donateMsisdn = "";
			this.donateFlag = "";		
			this.status = "";
			this.chooseFlag = "";
			this.operate = "";
		}
		
		public String getBeginDate() {
			return beginDate;
		}

		public void setBeginDate(String beginDate) {
			this.beginDate = beginDate;
		}

		public String getBizCode() {
			return bizCode;
		}

		public void setBizCode(String bizCode) {
			this.bizCode = bizCode;
		}

		public String getDomainCode() {
			return domainCode;
		}

		public void setDomainCode(String domainCode) {
			this.domainCode = domainCode;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public String getPrdCode() {
			return prdCode;
		}

		public void setPrdCode(String prdCode) {
			this.prdCode = prdCode;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		/**
		 * @return the prdCode1
		 */
		public String getPrdCode1()
		{
			return prdCode1;
		}

		/**
		 * @param prdCode1 the prdCode1 to set
		 */
		public void setPrdCode1(String prdCode1)
		{
			this.prdCode1 = prdCode1;
		}

		public String getDonateMsisdn() {
			return donateMsisdn;
		}

		public void setDonateMsisdn(String donateMsisdn) {
			this.donateMsisdn = donateMsisdn;
		}
		
		public String getChooseFlag() {
			return chooseFlag;
		}

		public void setChooseFlag(String chooseFlag) {
			this.chooseFlag = chooseFlag;
		}
		
		public String getOperate() {
			return operate;
		}

		public void setOperate(String operate) {
			this.operate = operate;
		}

		public String getDonateFlag() {
			return donateFlag;
		}

		public void setDonateFlag(String donateFlag) {
			this.donateFlag = donateFlag;
		}

		public String getAddInfo1() {
			return addInfo1;
		}

		public void setAddInfo1(String addInfo1) {
			this.addInfo1 = addInfo1;
		}

		public String getAddInfo2() {
			return addInfo2;
		}

		public void setAddInfo2(String addInfo2) {
			this.addInfo2 = addInfo2;
		}

		public String getSelfPlatState() {
			return selfPlatState;
		}

		public void setSelfPlatState(String selfPlatState) {
			this.selfPlatState = selfPlatState;
		}
		
	}
	
	public class CEmailUserAttachDtBean {
		
		
		private String create_date;
		private String create_operator;
		private String change_date;
		private String end_date;
		private String begin_date;
		private String info_value;
		private String info_code;
		private String biz_type;
		private String msisdn;
		private String user_id;
		private String active_flag;
		private String change_operator;
		public String getActive_flag() {
			return active_flag;
		}
		public void setActive_flag(String active_flag) {
			this.active_flag = active_flag;
		}
		public String getBegin_date() {
			return begin_date;
		}
		public void setBegin_date(String begin_date) {
			this.begin_date = begin_date;
		}
		public String getBiz_type() {
			return biz_type;
		}
		public void setBiz_type(String biz_type) {
			this.biz_type = biz_type;
		}
		public String getChange_date() {
			return change_date;
		}
		public void setChange_date(String change_date) {
			this.change_date = change_date;
		}
		public String getChange_operator() {
			return change_operator;
		}
		public void setChange_operator(String change_operator) {
			this.change_operator = change_operator;
		}
		public String getCreate_date() {
			return create_date;
		}
		public void setCreate_date(String create_date) {
			this.create_date = create_date;
		}
		public String getCreate_operator() {
			return create_operator;
		}
		public void setCreate_operator(String create_operator) {
			this.create_operator = create_operator;
		}
		public String getEnd_date() {
			return end_date;
		}
		public void setEnd_date(String end_date) {
			this.end_date = end_date;
		}
		public String getInfo_code() {
			return info_code;
		}
		public void setInfo_code(String info_code) {
			this.info_code = info_code;
		}
		public String getInfo_value() {
			return info_value;
		}
		public void setInfo_value(String info_value) {
			this.info_value = info_value;
		}
		public String getMsisdn() {
			return msisdn;
		}
		public void setMsisdn(String msisdn) {
			this.msisdn = msisdn;
		}
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		
		
		
		
	}
	
	/**
	 * 会员信息查询
	 * @author 孙林
	 * @version 1.0
	 */
	public class CcCGetMusicInfoBean {
		
		private String operating_srl;
		
		private String gsm_user_id;
		
		private String msisdn;
		
		private String member_level;
		
		private String package_id;
		
		private String start_date;
		
		private String end_date;
		
		private String status;
		
		private String operating_src;
		
		private String last_update_date;
		
		private String create_operator;
		
		private String create_org_id;
		
		private String change_operator;
		
		private String 	change_date;
		
		private String 	change_remark;
		
		private String 	active_flag;

		public String getActive_flag() {
			return active_flag;
		}

		public void setActive_flag(String active_flag) {
			this.active_flag = active_flag;
		}

		public String getChange_date() {
			return change_date;
		}

		public void setChange_date(String change_date) {
			this.change_date = change_date;
		}

		public String getChange_operator() {
			return change_operator;
		}

		public void setChange_operator(String change_operator) {
			this.change_operator = change_operator;
		}

		public String getChange_remark() {
			return change_remark;
		}

		public void setChange_remark(String change_remark) {
			this.change_remark = change_remark;
		}

		public String getCreate_operator() {
			return create_operator;
		}

		public void setCreate_operator(String create_operator) {
			this.create_operator = create_operator;
		}

		public String getCreate_org_id() {
			return create_org_id;
		}

		public void setCreate_org_id(String create_org_id) {
			this.create_org_id = create_org_id;
		}

		public String getEnd_date() {
			return end_date;
		}

		public void setEnd_date(String end_date) {
			this.end_date = end_date;
		}

		public String getGsm_user_id() {
			return gsm_user_id;
		}

		public void setGsm_user_id(String gsm_user_id) {
			this.gsm_user_id = gsm_user_id;
		}

		public String getLast_update_date() {
			return last_update_date;
		}

		public void setLast_update_date(String last_update_date) {
			this.last_update_date = last_update_date;
		}

		public String getMember_level() {
			return member_level;
		}

		public void setMember_level(String member_level) {
			this.member_level = member_level;
		}

		public String getMsisdn() {
			return msisdn;
		}

		public void setMsisdn(String msisdn) {
			this.msisdn = msisdn;
		}

		public String getOperating_src() {
			return operating_src;
		}

		public void setOperating_src(String operating_src) {
			this.operating_src = operating_src;
		}

		public String getOperating_srl() {
			return operating_srl;
		}

		public void setOperating_srl(String operating_srl) {
			this.operating_srl = operating_srl;
		}

		public String getPackage_id() {
			return package_id;
		}

		public void setPackage_id(String package_id) {
			this.package_id = package_id;
		}

		public String getStart_date() {
			return start_date;
		}

		public void setStart_date(String start_date) {
			this.start_date = start_date;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}
}

