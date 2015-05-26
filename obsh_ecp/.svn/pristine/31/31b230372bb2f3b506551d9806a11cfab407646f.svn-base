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
import com.xwtech.xwecp.service.logic.pojo.QRY050045Result;
import com.xwtech.xwecp.service.logic.pojo.TransIncrementInfo;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class GetPkgBusinessInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(GetProBusinessInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	/**
	 * 解析公用的配置文件
	 */
	private ParseXmlConfig config;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public GetPkgBusinessInvocation()
	{ 
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}
	
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		QRY050045Result res = new QRY050045Result();
		try
		{
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			BaseResult proBusinessList = this.getPkgBusinessList(accessId, config, params);
			if (LOGIC_SUCESS.equals(proBusinessList.getResultCode())) {
				res.setIncrementInfos((List<TransIncrementInfo>)proBusinessList.getReObj());
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	protected BaseResult getPkgBusinessList(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		ErrorMapping errDt = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_getitembytemp", params);
			
			logger.debug(" ====== 查询产品详细信息请求报文 ======\n" + reqXml);
			
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_getitembytemp", this.generateCity(params)));
//			logger.debug(" ====== 查询产品详细信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				String resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode(BOSS_SUCCESS.equals(resp_code)?LOGIC_SUCESS:LOGIC_ERROR);
//				String errCode = root.getChild("response").getChildText("resp_code");
//				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY050045", "cc_getitembytemp", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				if (null != resp_code && (BOSS_SUCCESS.equals(resp_code))) {
					res.setResultCode(LOGIC_SUCESS);
					//获取查询的节点信息列表
					List package_info = this.config.getContentList(root, "item_info");
					List<TransIncrementInfo> list = new ArrayList<TransIncrementInfo>();
					List paramlist = new ArrayList();
					List<RequestParameter> paramNew = copyParam(params);
					BaseResult pkgCfgList = null;
					for (int i = 0; i < package_info.size(); i++) {
						TransIncrementInfo transIncrementInfo = new TransIncrementInfo();
						String pkgCode = this.config.getChildText((Element) package_info.get(i), "sub_prodid");
						paramlist.add(new BusinessBoss(pkgCode));
						list.add(transIncrementInfo);
					}
					paramNew.add(new RequestParameter("codeCount", paramlist));
					pkgCfgList = this.getPkgCfgList(accessId, config, paramNew);
					if (LOGIC_SUCESS.equals(pkgCfgList.getResultCode())) {
						List<ProductBusinessPackageBean> pkgCfg = (List<ProductBusinessPackageBean>)pkgCfgList.getReObj();
							for (int i = 0; i < package_info.size(); i++) {
								TransIncrementInfo transIncrementInfo = new TransIncrementInfo();
								transIncrementInfo.setPkgId(this.config.getChildText((Element) package_info.get(i), "prodid"));
								transIncrementInfo.setPkgName(this.config.getChildText((Element) package_info.get(i), "prodname"));
								transIncrementInfo.setSubPkgId(this.config.getChildText((Element) package_info.get(i), "sub_prodid"));
								transIncrementInfo.setSubPkgName(this.config.getChildText((Element) package_info.get(i), "subs_prodname"));
								transIncrementInfo.setSelectType(Integer.parseInt(this.config.getChildText((Element) package_info.get(i), "select_type")));
								transIncrementInfo.setIsIncluedSp(Integer.parseInt(this.config.getChildText((Element) package_info.get(i), "sp_include")));
								transIncrementInfo.setIsIncludAttrFlag(Integer.parseInt(this.config.getChildText((Element) package_info.get(i), "attr_flag")));
								list.add(transIncrementInfo);
							}
							for(ProductBusinessPackageBean cfg:pkgCfg){
								for (TransIncrementInfo transInfo : list) {
									String pkgCode = transInfo.getSubPkgId();
									if (cfg.getPackageCode().equals(pkgCode)) {
										transInfo.setPkgDesc(cfg.getPackageDesc());
									}
								}
							}
						}
						res.setReObj(list);
					}
			} else {
				res.setResultCode(LOGIC_ERROR);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
		
	}
	
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
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetpackbycode_605_CPHZ", params);
				
				logger.debug(" ====== 查询套餐配置请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetpackbycode_605_CPHZ", this.generateCity(params)));
				logger.debug(" ====== 查询套餐配置返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY050017", "cc_cgetpackbycode_605_CPHZ", errCode);
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
}

