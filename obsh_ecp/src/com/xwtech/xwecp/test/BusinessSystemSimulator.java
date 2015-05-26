package com.xwtech.xwecp.test;


import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryBusinessService;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryBusinessServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;
import com.xwtech.xwecp.util.ConfigurationRead;


public class BusinessSystemSimulator
{
	private static final Logger logger = Logger.getLogger(BusinessSystemSimulator.class);
	
	public static void main(String args[]) throws Exception
	{
		final String mobile = ConfigurationRead.getInstance().getValue("mobile");
		String brand = ConfigurationRead.getInstance().getValue("brandnum");
		
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://192.168.0.189/xwecp/xwecp.do");
//		props.put("platform.url", "http://10.32.172.65:8089/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段		
//		LIInvocationContext lic = LIInvocationContext.getContext();
//		lic.setBizCode("biz_code_19234");
//		lic.setOpType("开通/关闭/查询/变更");
//		lic.setUserBrand("动感地带");
//		lic.setUserCity("用户县市");
//		lic.setUserMobile(mobile);
//		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("route_type", "2");
//		ic.addContextParameter("route_value", mobile);
//		ic.addContextParameter("login_msisdn", mobile);
//		ic.addContextParameter("loginiplock_login_ip", "127.0.0.1");
//		ic.addContextParameter("ddr_city", ConfigurationRead.getInstance().getValue("city"));
//		ic.addContextParameter("user_id", ConfigurationRead.getInstance().getValue("userid"));
//		ic.addContextParameter("brand", ConfigurationRead.getInstance().getValue("brand"));
//		ic.addContextParameter("country", ConfigurationRead.getInstance().getValue("country"));
//		ic.addContextParameter("user_change_remark", "");
//		lic.setContextParameter(ic);
		//停复机
//		ISuspendResumeServService ius = new SuspendResumeServServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		DEL030001Result r = ius.suspendResumeServ(mobile, 1);
		//延长话费有效期
//		IExtFeeExpireService ius = new ExtFeeExpireServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		DEL040001Result r = ius.extFeeExpire(mobile, 95);
		//延长充值卡有效期
//		IExtCardExpireService ius = new ExtCardExpireServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		DEL040002Result r = ius.extCardExpire("3138400339722532", 95);
		//查询用户手机俱乐部信息
//		IQueryMobileClubService ius = new QueryMobileClubServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY050009Result r = ius.queryMobileClub(mobile);
		//手机俱乐部受理
//		ITransactMobileClubService ius = new TransactMobileClubServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		DEL040012Result r = ius.transactMobileClub(mobile, 101);
		//省内一卡多号 状态查询
//		IQueryMultiInfoService ius = new QueryMultiInfoServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY020006Result r = ius.queryMultiInfo(mobile);
		//省内一卡多号 副号资源查询
//		IQueryViceNumberService ius = new QueryViceNumberServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY050014Result r = ius.queryViceNumber(0, "");
		//省内一卡多号 副号资源查询
//		ITransactMultiInfoService ius = new TransactMultiInfoServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		DEL010003Result r = ius.transactMultiInfo(mobile, "15251740184", 1);
		//在线入网 网上选号
//		IGetNumResourceService ius = new GetNumResourceServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY050015Result r = ius.getNumResource("NJDQ", "NJ");
		//在线入网 产品选择
//		IGetProResourceService ius = new GetProResourceServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY050016Result r = ius.getProResource("NJDQ");
		//在线入网 产品业务选择
//		IGetProBusinessService ius = new GetProBusinessServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY050017Result r = ius.getProBusiness("NJDQ", "100049");
		//在线入网 自取营业厅查询
//		IQueryHallAddrService ius = new QueryHallAddrServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY050018Result r = ius.queryHallAddr("NJDQ", "NJ");
		//全品牌在线入网订单查询
//		IQueryAllBrandOrderService ius = new QueryAllBrandOrderServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY050019Result r = ius.queryAllBrandOrder("15951971532");
		//用户产品信息查询
//		IGetUserProInfoService ius = new GetUserProInfoServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY050020Result r = ius.getUserProInfo(mobile);
		//产品互转 可转产品查询
//		IGetUserProResourceService ius = new GetUserProResourceServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY050022Result r = ius.getUserProResource("100049");
		//产品互转 产品转换时需关闭的附加功能查询
//		IGetCloseServiceService ius = new GetCloseServiceServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY050023Result r = ius.getCloseService(mobile, "100063", "100050");
		//产品互转 产品转换时需关闭的增值业务查询
//		IGetCloseIncrementService ius = new GetCloseIncrementServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY050024Result r = ius.getCloseIncrement(mobile, "100063", "100050");
		//产品互转 用户校验
//		ICheckUserService ius = new CheckUserServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY050025Result r = ius.checkUser(mobile, "100062", "1", 1, 1);
		//手机游戏 手机游戏订购关系查询
//		IQueryMobileGameService ius = new QueryMobileGameServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY020010Result r = ius.queryMobileGame(mobile);
		//M值兑换新业务可兑换列表查询
//		IQueryExchangeBizListService ius = new QueryExchangeBizListServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY030008Result r = ius.queryExchangeBizList();
		//查询用户余额和积分M值信息
//		IQueryBalScoreService ius = new QueryBalScoreServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY040002Result r = ius.queryBalScore(mobile, brand);
		//手机游戏 手机游戏办理
//		ITransactMobileGameService ius = new TransactMobileGameServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		List<MobileGame> mobileGames = new ArrayList<MobileGame>();
//		MobileGame mobileGame1 = new MobileGame();
//		mobileGame1.setSpId("701001");
//		mobileGame1.setBizCode("500230544000");
//		mobileGame1.setOprType(1);
//		MobileGame mobileGame2 = new MobileGame();
//		mobileGame2.setSpId("701001");
//		mobileGame2.setBizCode("500231882000");
//		mobileGame2.setOprType(1);
//		mobileGames.add(mobileGame1);
//		mobileGames.add(mobileGame2);
		
//		DEL010004Result r = ius.transactMobileGame(mobile, mobileGames);
		
//		业务优惠使用区 查询已体验的业务及可体验的业务
//		IQueryAlreadyAndCanExperienceBizService ius = new QueryAlreadyAndCanExperienceBizServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY050031Result r = ius.queryAlreadyAndCanExperienceBiz(mobile);
		
		//业务优惠使用区 优惠业务体验申请、中止
//		IDealExtCardService ius = new DealExtCardServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
		//113146 时尚彩讯 100104 12580联盟会员3元体验
//		DEL040020Result r = ius.dealExtCard(mobile, "100104", 1);
		
		//用户套餐查询
//		IQueryUserPackageUsedService ius = new QueryUserPackageUsedServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY040011Result r = ius.queryUserPackageUsed(mobile, 1, 3);
		
		//套餐及业务 附加功能查询
		int t = 0;
		while (true) {
			if (t >= 10) {
				break;
			}
			new Thread(){
				public void run() {
					int i = 0;
					int s = 0;
					int f = 0;
					
					long total = 0;
					while (true) {
						if (i >= 20) {
							break;
						}
						long bg = System.currentTimeMillis();
						//逻辑接口调用片段		
						LIInvocationContext lic = LIInvocationContext.getContext();
						lic.setBizCode("biz_code_19234");
						lic.setOpType("开通/关闭/查询/变更");
						lic.setUserBrand("动感地带");
						lic.setUserCity("用户县市");
						lic.setUserMobile(mobile);
						InvocationContext ic = new InvocationContext();
						ic.addContextParameter("route_type", "2");
						ic.addContextParameter("route_value", mobile);
						ic.addContextParameter("login_msisdn", mobile);
						ic.addContextParameter("loginiplock_login_ip", "127.0.0.1");
						ic.addContextParameter("ddr_city", ConfigurationRead.getInstance().getValue("city"));
						ic.addContextParameter("user_id", ConfigurationRead.getInstance().getValue("userid"));
						ic.addContextParameter("brand", ConfigurationRead.getInstance().getValue("brand"));
						ic.addContextParameter("country", ConfigurationRead.getInstance().getValue("country"));
						ic.addContextParameter("user_change_remark", "");
						lic.setContextParameter(ic);
						IQueryBusinessService ius = new QueryBusinessServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//						IQueryNumBelongService ius = new QueryNumBelongServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
						QRY020001Result r = null;
//						QRY050004Result r = null;
						try {
//							r = ius.queryNumBelong(mobile);
							r = ius.queryBusiness(mobile, 2, "JFCX");
						} catch (LIException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						long ed = System.currentTimeMillis();
						if(r != null && r.getResultCode().equals("0"))
						{
							logger.debug("逻辑接口测试OK!!!!!!!!!!!!!!!!!!!!!花费： " + (ed - bg) + " ms!");
							s++;
							
						} else {
							f++;
							logger.debug("==================>>>>>>>>>>>>>>>>>>>>>" + r.getErrorCode());
							logger.debug("==================>>>>>>>>>>>>>>>>>>>>>" + r.getErrorMessage());
						}
						total += (ed - bg);
						i ++;
					}
				}
			}.start();
			t++;
		}
		
		//M值兑换业务 查询
//		IQueryExchangeBizListService ius = new QueryExchangeBizListServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY030008Result r = ius.queryExchangeBizList();
		
		//M值兑换业务 兑换
//		IMExchangeBizService ius = new MExchangeBizServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		DEL020002Result r = ius.mExchangeBiz(mobile, "1", "5012", 1, 0);
		
		//充值缴费记录
//		IQueryPayHistoryService ius = new QueryPayHistoryServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY010008Result r = ius.queryPayHistory(mobile, "20091012", "20100412");
		
		//办理历史查询
//		IQueryOperDetailService ius = new QueryOperDetailServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY040004Result r = ius.queryOperDetail(mobile, "20100114", "20100414");
		
		//梦网业务查询
//		IQueryMonternetService ius = new QueryMonternetServiceClientImpl();	//这里可以使用spring注入, 不过, 是不是应该生成spring-context?
//		QRY050002Result r = ius.queryMonternet(mobile);
		
//		if(r != null && r.getResultCode().equals("0"))
//		{
//			logger.info("逻辑接口测试OK!!!!!!!!!!!!!!!!!!!!!");
//			List<AlreadyExperienceBiz> allBizs = r.getAlreadyExperienceBizs();
//			List<CanExperienceBiz> canBizs = r.getCanExperienceBizs();
//			
//			if (allBizs != null && allBizs.size() > 0) {
//				logger.info("================================已经体验的业务列表 " + allBizs.size());
//				
//				for (AlreadyExperienceBiz alBiz:allBizs) {
//					logger.info("============>>>>>>>>>>>>>>>>>>>>>> " + alBiz.getBizName());
//					logger.info("============>>>>>>>>>>>>>>>>>>>>>> " + alBiz.getBizId());
//				}
//			}
//			
//			if (canBizs != null && canBizs.size() > 0) {
//				logger.info("================================可以体验的业务列表 " + canBizs.size());
//				
//				for (CanExperienceBiz obj:canBizs) {
//					logger.info("============>>>>>>>>>>>>>>>>>>>>>> " + obj.getBizName());
//					logger.info("============>>>>>>>>>>>>>>>>>>>>>> " + obj.getBizId());
//				}
//			}
			
//			List<ExchangeType> exchangeTypes = r.getExchangeTypes();
//			List<PackageType> packageTypes = r.getPackageTypes();
//			List<ExchangeBizInfo> exchangeBizInfos = r.getExchangeBizInfos();
//			
//			if (exchangeTypes != null && exchangeTypes.size() > 0) {
//				logger.info("=================>>>>>>>>=====type : " + exchangeTypes.size());
//				for (ExchangeType type:exchangeTypes) {
//					logger.info("=================>>>>>>>>=====type : " + type.getTypeId() + "~~~~~" + type.getTypeName());
//				}
//			}
//			
//			if (packageTypes != null && packageTypes.size() > 0) {
//				logger.info("=================>>>>>>>>=====pkg : " + packageTypes.size());
//				for (PackageType pkg:packageTypes) {
//					logger.info("=================>>>>>>>>=====pkg : " + pkg.getTypeId() + "~~~~~" + pkg.getTypeName());
//				}
//			}
//			
//			if (exchangeBizInfos != null && exchangeBizInfos.size() > 0) {
//				logger.info("=================>>>>>>>>=====biz : " + exchangeBizInfos.size());
//				for (ExchangeBizInfo biz:exchangeBizInfos) {
//					logger.info("=================>>>>>>>>=====biz : " + biz.getExchangeTypeId() + "~~~~~" + biz.getExchangeCode() + "~~~~~" + biz.getPackageTypeId() + "~~~~~" + biz.getExchangeName() + "~~~~~" + biz.getScores());
//				}
//			}
			
//			List<PkgDetail> pkgDetails = r.getPkgDetail();
//			
//			if (pkgDetails != null && pkgDetails.size() > 0) {
//				for (PkgDetail p:pkgDetails) {
//					System.out.println("id：" + p.getPkgId() + " name：" + p.getPkgName());
//				}
//			}
			
//			List<OperDetail> operDetail = r.getOperDetail();
//			
//			if (operDetail != null && operDetail.size() > 0) {
//				for (OperDetail b:operDetail) {
//					System.out.println(b.getOprTime() + " ==== " + b.getOprBiz() + " ==== " + b.getOprChannel());
//				}
//			}
			
			
//		} else {
//			logger.info("==================>>>>>>>>>>>>>>>>>>>>>" + r.getErrorCode());
//			logger.info("==================>>>>>>>>>>>>>>>>>>>>>" + r.getErrorMessage());
//		}
	}
	public static void __main(String args[]) throws Exception
	{
		/*
		HttpCommunicator http = new HttpCommunicator();
		http.setRemoteURL("http://127.0.0.1:10000/xwecp/xwecp.do");
		MessageHelper msgHelper = new MessageHelper("test");
		int i = 0;
		while(true)
		{
			ServiceMessage msg = msgHelper.createRequestMessage("L10012");
			RequestParameter param = new RequestParameter("mobile", "13601400067");
			RequestParameter param1 = new RequestParameter("password", "123456");
			UserInfo userInfo = new UserInfo();
			UserBrand userBrand = new UserBrand();
			userBrand.setUserBrand("动感地带");
			userBrand.setUserBrandId(11);
			userInfo.setUserId("13601400067");
			userInfo.setUserBrand(userBrand);
			RequestParameter param2 = new RequestParameter("userInfo", userBrand);
			RequestData requestData = new RequestData();
			requestData.getParams().add(param);
			requestData.getParams().add(param1);
			requestData.getParams().add(param2);
			msg.setData(requestData);
			String xmlRequest = msg.toString();
			//logger.info("requst: \n" + xmlRequest);
			String xmlResponse = http.send(xmlRequest);
			//logger.info("==========================================================");
			//logger.info("==========================================================");
			//logger.info("==========================================================");
			//System.out.println(xmlResponse);
			//System.out.println("============================================================================================");
			Thread.sleep(100);
			break;
		}
		*/
	}
}
