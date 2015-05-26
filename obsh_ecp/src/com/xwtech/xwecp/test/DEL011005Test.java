 package com.xwtech.xwecp.test;     

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.ICnetInstallPrd;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CnetInstallPrdClientImpl;
import com.xwtech.xwecp.service.logic.pojo.CwebCustInfoBean;
import com.xwtech.xwecp.service.logic.pojo.CwebIncrementInfoBean;
import com.xwtech.xwecp.service.logic.pojo.CwebPackageInfoBean;
import com.xwtech.xwecp.service.logic.pojo.CwebSelfPlatinfoBean;
import com.xwtech.xwecp.service.logic.pojo.CwebServiceInfoBean;
import com.xwtech.xwecp.service.logic.pojo.DEL011005Result;

/**   
 * @author 工号：743 
 * @version 1.0
 * @CreateDate  Jun 27, 2011 2:28:12 PM
 */
public class DEL011005Test {

	private static final Logger logger = Logger.getLogger(DEL011005Test.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("11");
		lic.setUserMobile("13814812424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13814812424");
		//ic.addContextParameter("ddr_city", "12");
		lic.setContextParameter(ic);
		
		ICnetInstallPrd co = new CnetInstallPrdClientImpl();
		List<CwebCustInfoBean> cwebCustInfoList = new ArrayList<CwebCustInfoBean>();
		CwebCustInfoBean cwebCustInfoBean = new CwebCustInfoBean();
		cwebCustInfoBean.setAddrId("17307653");
		cwebCustInfoBean.setAddrName(" 苏州市区园区园区湖东淞江路以北，斜塘河以南金色尚城Z5栋1单元102室");
		cwebCustInfoBean.setAppointAffix("2011-09-23");
		cwebCustInfoBean.setContactPhone("13814812424");
		cwebCustInfoBean.setDistrictId("18026");
		cwebCustInfoBean.setDistrictName("金色尚城Z");
		cwebCustInfoBean.setFactoryType("1");
		cwebCustInfoBean.setGimsAreaType("1");
		cwebCustInfoBean.setGimsUserType("1");
		cwebCustInfoBean.setLinkMan("DSLAM");
		cwebCustInfoBean.setNotes("");
		cwebCustInfoBean.setOldUserAcct("");
		cwebCustInfoBean.setOldUserType("");
		cwebCustInfoBean.setRadiusType("1");
		cwebCustInfoBean.setSupplyType("1");
		cwebCustInfoBean.setWebcustinfoAmount("0");
		cwebCustInfoBean.setWebcustinfoBrandId("1");
		cwebCustInfoBean.setWebcustinfoCityId("11");
		cwebCustInfoBean.setWebcustinfoCountyId("1101");
		cwebCustInfoBean.setWebcustinfoCreateTime("20110920");
		cwebCustInfoBean.setWebcustinfoCustAddr("");
		cwebCustInfoBean.setWebcustinfoCustName("");
		cwebCustInfoBean.setWebcustinfoEmsNo("");
		cwebCustInfoBean.setWebcustinfoFetchFlag("");
		cwebCustInfoBean.setWebcustinfoHrn("");
		cwebCustInfoBean.setWebcustinfoIcAddr("");
		cwebCustInfoBean.setWebcustinfoIcNo("13814812424");
		cwebCustInfoBean.setWebcustinfoIcType("0");
		//==============营销方案开始===========
		//2512104963:手机代付费宽带有礼 对应的是1300000001
		//2000002848 手机代付20元包40小时 对应的是1300000003
		cwebCustInfoBean.setWebcustinfoMarketId("");
		cwebCustInfoBean.setWebcustinfoMarketLevelId("");
		cwebCustInfoBean.setMarketFee("");
		cwebCustInfoBean.setMarketGoodsPackId("");
		cwebCustInfoBean.setMarketBusiPackId("");
		//==============营销方案结束===========
		cwebCustInfoBean.setWebcustinfoMsisdn("13814812424");
		cwebCustInfoBean.setWebcustinfoOpenMarket("");
		cwebCustInfoBean.setWebcustinfoOperatingSrl("");
		cwebCustInfoBean.setWebcustinfoOperSource("");
		cwebCustInfoBean.setWebcustinfoOtherMarket("");
		cwebCustInfoBean.setWebcustinfoPayType("1");
		cwebCustInfoBean.setWebcustinfoPostCode("");
		cwebCustInfoBean.setWebcustinfoProductId("1300000001");
		cwebCustInfoBean.setWebcustinfoReceivableFee("0");
		cwebCustInfoBean.setWebcustinfoSiteAddr("");
		cwebCustInfoBean.setWebcustinfoSiteId("");
		cwebCustInfoBean.setWebcustinfoSiteName("");
		cwebCustInfoBean.setWebcustinfoStatus("1");
		cwebCustInfoBean.setWebcustinfoTel("");
		cwebCustInfoBean.setWebcustinfoWebBookingId("");
		cwebCustInfoList.add(cwebCustInfoBean);
		
		List<CwebPackageInfoBean> cwebPackageInfoList = new ArrayList<CwebPackageInfoBean>();
		CwebPackageInfoBean cwebPackageInfoBean = new CwebPackageInfoBean();
		cwebPackageInfoBean.setWebpackageinfoApplyDate("20110920");
		cwebPackageInfoBean.setWebpackageinfoEndDate("");
		cwebPackageInfoBean.setWebpackageinfoIsOpen("3");
		cwebPackageInfoBean.setWebpackageinfoMsisdn("13814812424");
		cwebPackageInfoBean.setWebpackageinfoOperatingSrl("");
		cwebPackageInfoBean.setWebpackageinfoPackageCode("2000002090");
		cwebPackageInfoBean.setWebpackageinfoPackageLevel("15");
		cwebPackageInfoBean.setWebpackageinfoPackageType("1041");
		cwebPackageInfoBean.setWebpackageinfoUseDate("20110920");
		cwebPackageInfoList.add(cwebPackageInfoBean);
		
		List<CwebServiceInfoBean> cwebServiceInfoList = new ArrayList<CwebServiceInfoBean>();
		
		List<CwebIncrementInfoBean> cwebIncrementInfoList = new ArrayList<CwebIncrementInfoBean>();
		
		List<CwebSelfPlatinfoBean> cwebSelfPlatinfoList = new ArrayList<CwebSelfPlatinfoBean>();
		
		DEL011005Result re = co.transactBroadBand("11",cwebCustInfoList, cwebPackageInfoList, cwebServiceInfoList,cwebIncrementInfoList,cwebSelfPlatinfoList);
//		logger.info(" ====== 开始返回参数 ======");
//		if (re != null)
//		{
//			System.out.println(" ====== retCode: ======" + re.getRetCode());
//			System.out.println(" ====== ccOperatingSrl ======" + re.getCcOperatingSrl());
//			System.out.println(" ====== webcustinfoWebBookingId ======" + re.getWebcustinfoWebBookingId());
//		}
	}

}
  