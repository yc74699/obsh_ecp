package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;
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
import com.xwtech.xwecp.service.logic.pojo.PackageInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY050036Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 产品互转 套餐冲突校验
 * 
 * @author 吴宗德
 *
 */
public class CheckPackageConflictInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(CheckPackageConflictInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public CheckPackageConflictInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY050036Result res = new QRY050036Result();
		try
		{
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			//套餐冲突校验
			BaseResult pkgListRet = this.checkPackageConflict(accessId, config, params);
			if (LOGIC_SUCESS.equals(pkgListRet.getResultCode())) {
				List<CPlanPackageDtBean> pkgList = (List<CPlanPackageDtBean>)pkgListRet.getReObj();
				if (pkgList != null && pkgList.size() > 0) {
					List<PackageInfo> closePackages = new ArrayList<PackageInfo>();
					PackageInfo bean = null;
					for (CPlanPackageDtBean pkgBean:pkgList) {
						
						String pkgCode = pkgBean.getCode();
						
						List<RequestParameter> paramNew = copyParam(params);
						List list = new ArrayList();
						list.add(new BusinessBoss(pkgCode));
						paramNew.add(new RequestParameter("codeCount", list));
						
						BaseResult pkgCfgList = this.getPkgCfgList(accessId, config, paramNew);
						
						if (LOGIC_SUCESS.equals(pkgCfgList.getResultCode())) {
							List<ProductBusinessPackageBean> pkgCfg = (List<ProductBusinessPackageBean>)pkgCfgList.getReObj();

							if (pkgCfg != null) {
								for (ProductBusinessPackageBean cfg:pkgCfg) {
									if (pkgCode.equals(cfg.getPackageCode())) {
										bean = new PackageInfo();

										bean.setPkgId(pkgBean.getCode());
										bean.setPkgState(pkgBean.getState());
										bean.setPkgType(pkgBean.getType());
										bean.setPkgName(cfg.getPackageName());
										bean.setPkgLevel(pkgBean.getLevel());
										bean.setBeginDate(pkgBean.getUse_date());
										bean.setEndDate(pkgBean.getEnd_date());
										
										closePackages.add(bean);
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
					res.setClosePackages(closePackages);
				}
			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(pkgListRet.getErrorCode());
				res.setErrorMessage(pkgListRet.getErrorMessage());
			}
		}
		catch (Exception e)
		{
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
	protected BaseResult checkPackageConflict(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		List<CPlanPackageDtBean> pkgList = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cchkconfpkgmem_351_CPHZ", params);

			logger.debug(" ====== 查询 产品互转 套餐冲突校验 请求报文 ======\n" + reqXml);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cchkconfpkgmem_351_CPHZ", this.generateCity(params)));
			logger.debug(" ====== 查询 产品互转 套餐冲突校验 返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY050036", "cc_cchkconfpkgmem_351_CPHZ", errCode);
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
						pkgList = new ArrayList<CPlanPackageDtBean>(package_code.size());
						CPlanPackageDtBean bean = null;
						for (int i = 0; i < package_code.size(); i++)
						{
							Element cplanpackagedt = ((Element)package_code.get(i)).getChild("cplanpackagedt");
							if (null != cplanpackagedt)
							{
								bean = new CPlanPackageDtBean();
								
								bean.setState(p.matcher(cplanpackagedt.getChildText("package_state")).replaceAll(""));
			                    bean.setChange_date(p.matcher(cplanpackagedt.getChildText("package_change_date")).replaceAll(""));
			                    bean.setEnd_date(p.matcher(cplanpackagedt.getChildText("package_end_date")).replaceAll(""));
			                    bean.setHistory_srl(p.matcher(cplanpackagedt.getChildText("package_history_srl")).replaceAll(""));
			                    bean.setUse_date(p.matcher(cplanpackagedt.getChildText("package_use_date")).replaceAll(""));
			                    bean.setApply_date(p.matcher(cplanpackagedt.getChildText("package_apply_date")).replaceAll(""));
			                    bean.setCode(p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll(""));
			                    bean.setLevel(p.matcher(cplanpackagedt.getChildText("package_level")).replaceAll(""));
			                    bean.setType(p.matcher(cplanpackagedt.getChildText("package_type")).replaceAll(""));
			                    bean.setUser_id(p.matcher(cplanpackagedt.getChildText("package_user_id")).replaceAll(""));

			                    pkgList.add(bean);
							}
						}
						res.setReObj(pkgList);
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
		List<ProductBusinessPackageBean> pkgCfgList = null;
		try {
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
					errDt = this.wellFormedDAO.transBossErrCode("QRY050036", "cc_cgetpackbycode_605", errCode);
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
		
		
		/**
		 * @return groupName
		 */
		public String getGroupName() {
			return groupName;
		}
		/**
		 * @param groupName 要设置的 groupName
		 */
		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}
		
		public String getBossToolFlag() {
			return bossToolFlag;
		}
		public void setBossToolFlag(String bossToolFlag) {
			this.bossToolFlag = bossToolFlag;
		}
		public String getWebFlag() {
			return webFlag;
		}
		public void setWebFlag(String webFlag) {
			this.webFlag = webFlag;
		}
		public String getBossViewFlag() {
			return bossViewFlag;
		}
		public void setBossViewFlag(String bossViewFlag) {
			this.bossViewFlag = bossViewFlag;
		}
		public String getCityId() {
			return cityId;
		}
		public void setCityId(String cityId) {
			this.cityId = cityId;
		}
		public String getPackageName() {
			return packageName;
		}
		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public String getPackageDesc() {
			return packageDesc;
		}
		public void setPackageDesc(String packageDesc) {
			this.packageDesc = packageDesc;
		}
		public String getPackageClass() {
			return packageClass;
		}
		public void setPackageClass(String packageClass) {
			this.packageClass = packageClass;
		}
		public String getFlag1860() {
			return flag1860;
		}
		public void setFlag1860(String flag1860) {
			this.flag1860 = flag1860;
		}
		public String getCuiFlag() {
			return cuiFlag;
		}
		public void setCuiFlag(String cuiFlag) {
			this.cuiFlag = cuiFlag;
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
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		public String getPackageLevel() {
			return packageLevel;
		}
		public void setPackageLevel(String packageLevel) {
			this.packageLevel = packageLevel;
		}
		public String getPackageCode() {
			return packageCode;
		}
		public void setPackageCode(String packageCode) {
			this.packageCode = packageCode;
		}
		public String getBusinessId() {
			return businessId;
		}
		public void setBusinessId(String businessId) {
			this.businessId = businessId;
		}
		public String getPreChoice() {
			return preChoice;
		}
		public void setPreChoice(String preChoice) {
			this.preChoice = preChoice;
		}
		/**
		 * @return groupId
		 */
		public String getGroupId() {
			return groupId;
		}
		/**
		 * @param groupId 要设置的 groupId
		 */
		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}
		/**
		 * @return businessName
		 */
		public String getBusinessName() {
			return businessName;
		}
		/**
		 * @param businessName 要设置的 businessName
		 */
		public void setBusinessName(String businessName) {
			this.businessName = businessName;
		}

	}
}