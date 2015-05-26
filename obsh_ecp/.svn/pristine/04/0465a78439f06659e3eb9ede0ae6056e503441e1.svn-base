package com.xwtech.xwecp.service.logic.invocation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.ServiceException;
import com.xwtech.xwecp.service.ServiceInfo;
import com.xwtech.xwecp.service.ServiceLocator;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.MyTotalInfo;
import com.xwtech.xwecp.service.logic.pojo.NewScoreDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;
import com.xwtech.xwecp.service.logic.pojo.QRY030011Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040047Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 查询我的移动我的消费信息
 * 
 * @author 杨光
 * 
 */
public class QueryMyTotalInfoBossInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryMyTotalInfoInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;
	
	private ParseXmlConfig config;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");

	//1-余额；2-当前消费；3-当前积分或M值；4-商城币
	private final String QUERYTYPE_FEE 	= "1"; 
	private final String QUERYTYPE_PAY 	= "2";
	private final String QUERYTYPE_SCORE = "3";
	private final String QUERYTYPE_SCB = "4";
	//结果值：0-正常；1-失败
	private final String RETCODE_SUCCESS= "0";
	private final String RETCODE_FAILURE = "1";
	
	
	
	public QueryMyTotalInfoBossInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY040047Result res = new QRY040047Result();
		try {
			//获取puk
			getPuk(accessId,config, params,res);
			
			//返回的LIST
			List<MyTotalInfo> retList = new ArrayList<MyTotalInfo>();
			
			try{ 
				//取用户余额
				//-查询用户余额和积分M值信息 QRY040002
				List<MyTotalInfo> feeAndScoreList = new ArrayList<MyTotalInfo>();
				feeAndScoreList = this.queryFeeAndScore(accessId, config, params, res);
			    retList.addAll(feeAndScoreList);
				res.setMyTotalInfoList(retList);
				
			}catch (Exception e) {
				e.printStackTrace();
				res.setResultCode(LOGIC_ERROR);
				res.setErrorMessage("查询余额和有效期异常");
			}finally{
				try{ 
					//当前年份
					Calendar cal = Calendar.getInstance();  
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
					String todayMonth = sdf.format(cal.getTime());

					//取当月消费
					/**
					 * idType = (long)6;
					modeId = 10000001
					qryType = (Integer)1;
					startCycle传 201112的格式。到月份就行了
					Integer billType = 2;// 账单类型(默认2)
					Integer billLevel = 3;	

					 */
					//查询账单必要参数
					params.add(new RequestParameter("idType", "6"));
					params.add(new RequestParameter("modeId", "10000001"));
					params.add(new RequestParameter("startcycle", todayMonth));
					params.add(new RequestParameter("endcycle", todayMonth));
					params.add(new RequestParameter("billType", "2"));
					params.add(new RequestParameter("billlType", "3"));
					params.add(new RequestParameter("userMsisdn", (String) getParameters(params, "phoneNum")));
					
					String payFee = "";
					payFee = this.queryPayfee(accessId, config, params, res);
					MyTotalInfo retBean = new MyTotalInfo();
					if(!StringUtils.isBlank(payFee)){
						retBean.setValue(payFee);
						retBean.setRetCode(RETCODE_SUCCESS);
					}else{
						retBean.setRetCode(RETCODE_FAILURE);
					}
					retBean.setType(QUERYTYPE_PAY);
					retBean.setTypeName("当前消费");
					retList.add(retBean);
				}catch (Exception e) {
					e.printStackTrace();
					res.setResultCode(LOGIC_ERROR);
					res.setErrorMessage("查询账单当月消费异常");
				}finally{
					try{ 
						// 5-商城币查询
						String scb = "";
						scb = this.queryScb(accessId, config, params, res);
						MyTotalInfo retBean = new MyTotalInfo();
						if(!StringUtils.isBlank(scb)){
							retBean.setValue(scb);
							retBean.setRetCode(RETCODE_SUCCESS);
						}else{
							retBean.setRetCode(RETCODE_FAILURE);
						}
						retBean.setType(QUERYTYPE_SCB);
						retBean.setTypeName("商城币");
						retList.add(retBean);
					}catch (Exception e) {
						e.printStackTrace();
						res.setResultCode(LOGIC_ERROR);
						res.setErrorMessage("查询当前商城币异常");
					}finally{
						
					}
				}
				
			}
			
//			//1-查询当前余额 QRY010003
//			// 设置查询余额的type TODO
//			//params.add(new RequestParameter("type", "1"));
//			//String crrentFee = this.queryFeeAcc(accessId, config, params, res);
//			
//			
//			
//			//3-查询实施账单中的当月消费 QRY010018
//		
//	
//			
//			// 5-商城币查询
//			String scb = "-";
//			scb = this.queryScb(accessId, config, params, res);
//    	     		
//			MyTotalInfo retBean = new MyTotalInfo(); 	
//		   if(feeAndScoreBean != null ){
//			  retBean.setCurrentFee(feeAndScoreBean.getCurrentFee()) ;
//			  retBean.setCurrentScore(feeAndScoreBean.getCurrentScore());
//		   }
//		   retBean.setCurrentPayfee(payFee);
//		   retBean.setCurrentScb(scb);
		   
		   
		  
    	   res.setMyTotalInfoList(retList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	
//	/**
//	 * 查询话费帐户
//	 * @param accessId
//	 * @param config
//	 * @param params
//	 * @return String
//	 */
//	public String queryFeeAcc (String accessId, ServiceConfig config, 
//			List<RequestParameter> params, QRY040047Result res)
//	{
//		String reqXml = "";
//		String rspXml = "";
//		Element root = null;
//		String resp_code = "";
//		String strDate ="";
//		ErrorMapping errDt = null;
//		
//		try
//		{
//			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetusercustaccbalance_67", params);
//			logger.debug(" ====== 查询话费帐户 发送报文 ====== \n" + reqXml);
//			if (null != reqXml && !"".equals(reqXml))
//			{
//				rspXml = (String)this.remote.callRemote(
//						 new StringTeletext(reqXml, accessId, "cc_cgetusercustaccbalance_67", this.generateCity(params)));
//				logger.debug(" ====== 查询话费帐户 接收报文 ====== \n" + rspXml);
//			}
//			if (null != rspXml && !"".equals(rspXml))
//			{
//				root = this.config.getElement(rspXml);
//				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
//				res.setResultCode("0000".equals(resp_code)?"0":"1");
//				if (!"0000".equals(resp_code))
//				{
//					errDt = this.wellFormedDAO.transBossErrCode("QRY040047", "cc_cgetusercustaccbalance_67", resp_code);
//					if (null != errDt)
//					{
//						res.setErrorCode(errDt.getLiErrCode());
//						res.setErrorMessage(errDt.getLiErrMsg());
//					}
//				}
//			}
//			if (null != resp_code && "0000".equals(resp_code))
//			{
//				strDate = String.valueOf(Double.parseDouble(
//				   this.config.getChildText(this.config.getElement(root, "content"), "balance")));
//		
//
//			}
//		}
//		catch (Exception e)
//		{
//			logger.error(e, e);
//		}
//		return strDate;
//	}
	
	
	/**
	 * 查询余额和积分
	 * @param accessId
	 * @param config
	 * @param params
	 * @return List<MyTotalInfo>
	 */
	public List<MyTotalInfo> queryFeeAndScore (String accessId, ServiceConfig config, 
			List<RequestParameter> params, QRY040047Result res)
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		ErrorMapping errDt = null;
		List<MyTotalInfo> retList = new ArrayList<MyTotalInfo>();
		
		try
		{
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetuseraccscore_361", params);
			logger.debug(" ====== 查询余额和积分 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_cgetuseraccscore_361", this.generateCity(params)));
				logger.debug(" ====== 查询余额和积分 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY040047", "cc_cgetuseraccscore_770", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			
			String currentFee= "-";
			String currentScore = "-";
			if (null != resp_code && "0000".equals(resp_code))
			{
				List blist = null;
				List slist = null;
				List mlist = null;
				//余额
				blist = root.getChild("content").getChildren("balance_response");
				//积分
				slist = root.getChild("content").getChildren("score_response");
				//M值
				mlist = root.getChild("content").getChildren("zonemvalue_response");
				
				if (null != blist && blist.size() > 0) {
					for (int i = 0; i < blist.size(); i++)
					{
						Element list_element = (Element)blist.get(i);
						MyTotalInfo retBean = new MyTotalInfo();
						if (null != list_element)
						{
							currentFee = p.matcher(list_element.getChildText("balance")).replaceAll("");
							retBean.setValue(currentFee);
							retBean.setRetCode(RETCODE_SUCCESS);
						}else{
							retBean.setRetCode(RETCODE_FAILURE);
						}
						retBean.setType(QUERYTYPE_FEE);
						retBean.setTypeName("余额");
						retList.add(retBean);
					}
					
				}
				if (null != slist && slist.size() > 0) {
					for (int i = 0; i < slist.size(); i++)
					{
						Element list_element = (Element)slist.get(i);
						MyTotalInfo retBean = new MyTotalInfo();
						if (null != list_element)
						{
							
							currentScore = p.matcher(list_element.getChildText("score_leavings_score")).replaceAll("");
							retBean.setValue(currentScore);
							retBean.setRetCode(RETCODE_SUCCESS);
							
						}else{
							retBean.setRetCode(RETCODE_FAILURE);
						}
						retBean.setType(QUERYTYPE_SCORE);
						retBean.setTypeName("当前积分");
						retList.add(retBean);
					}
					
				}
				if (null != mlist && mlist.size() > 0) {
					for (int i = 0; i < mlist.size(); i++)
					{
						Element list_element = (Element)mlist.get(i);
						MyTotalInfo retBean = new MyTotalInfo();
						if (null != list_element)
						{
							currentScore = p.matcher(list_element.getChildText("zonemvalue_used_mvalue")).replaceAll("");
							retBean.setValue(currentScore);
							retBean.setRetCode(RETCODE_SUCCESS);
							
						}else{
							retBean.setRetCode(RETCODE_FAILURE);
						}
						retBean.setType(QUERYTYPE_SCORE);
						retBean.setTypeName("当前M值");
						retList.add(retBean);
					}
					
				}
			}
			
			//20111231 yangg add start
			//如果是神州行品牌,需要判断号码是否开通了"神州行积分计划",如果未开通，resultcode返回xxx.积分数量0
			String brand  = (String)getParameters(params, "brand");
			if(!StringUtils.isBlank(brand) && brand.length() >=3 && brand.startsWith("SZX"))
			{
				ServiceLocator sl1 = (ServiceLocator)XWECPApp.SPRING_CONTEXT.getBean("serviceLocator");
				ServiceInfo si1;
				try {
					params.add(new RequestParameter("type", 1));
					params.add(new RequestParameter("bizId", "SZX_JFJH")); //查询神州行积分计划
					si1 = sl1.locate("QRY020001", params);
					QRY020001Result re = (QRY020001Result)si1.getServiceInstance().execute(accessId);
					if (re != null)
					{
						if (null != re.getGommonBusiness() && re.getGommonBusiness().size() > 0)
						{
							for (GommonBusiness g : re.getGommonBusiness())
							{
								if("SZX_JFJH".equals(g.getId()) && 2 != g.getState()){
								
								}else{						//开通了积分计划，查询积分，调用QRY030011
									//
									String szx_jf = "";
									ServiceLocator sl2 = (ServiceLocator)XWECPApp.SPRING_CONTEXT.getBean("serviceLocator");
									ServiceInfo si2;
									params.add(new RequestParameter("flag", "2"));
									si2 = sl2.locate("QRY030011", params);
									QRY030011Result re_szxjf = (QRY030011Result)si2.getServiceInstance().execute(accessId);
									if(re_szxjf!=null && re_szxjf.getNewScoreDetail() != null){
										for(NewScoreDetail nsd : re_szxjf.getNewScoreDetail())
										{
											if("7".equals(nsd.getScorenId())){
												szx_jf = String.valueOf(nsd.getScore());
												currentScore = szx_jf;  //
												MyTotalInfo retBean = new MyTotalInfo();
												retBean.setType(QUERYTYPE_SCORE);
												retBean.setTypeName("当前神州行积分");
												retBean.setRetCode(RETCODE_SUCCESS);
												retBean.setValue(currentScore);
												retList.add(retBean);
												break;
											}
										}
									}
								}
							}
							
						}
					}
					
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return retList;
	}
	
	/**
	 * 通过账单接口查询当前消费
	 * @param accessId
	 * @param config
	 * @param params
	 * @return String
	 */
	public String queryPayfee (String accessId, ServiceConfig config, 
			List<RequestParameter> params, QRY040047Result res)
	{
		String reqXml = "";
		String rspXml = "";

		String resp_code = "";
		String payfee= "";
		try
		{
			// 初始化准备
			reqXml = this.bossTeletextUtil.mergeTeletext("ac_acqryrealtimebill_361", params);
			logger.debug(" ====== 查询用户在用/(新)账单查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_acqryrealtimebill_361", super.generateCity(params)));
				logger.debug(" ====== 查询用户在用/(新)账单查询 接收报文 ====== \n" + rspXml);
			}

			// 解析BOSS报文
			if (null != rspXml && !"".equals(rspXml)) {
				// 解析报文 根节点
				Element root = this.config.getElement(rspXml);
				// 获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				// 设置结果信息
				this.getErrInfo(res, resp_code, "ac_acqryrealtimebill_361");
				
				Element resRoot = this.config.getElement(root, "content");
				// 成功
				if ("0000".equals(resp_code)) {
					payfee = resRoot.getChildText("totalfee") == null ? null : resRoot.getChildText("totalfee");
					
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return payfee;
	}
	
	
	/**
	 * 查询当前商城币
	 * @param accessId
	 * @param config
	 * @param params
	 * @return String
	 */
	public String queryScb(String accessId, ServiceConfig config, 
			List<RequestParameter> params, QRY040047Result res)
	{
		String reqXml = "";
		String rspXml = "";
		String resp_code = "";
		String scb = "";
		try
		{
			// 初始化准备
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_qryshangchscore", params);
			logger.debug(" ====== 查询商城币发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_qryshangchscore", super.generateCity(params)));
				logger.debug(" ====== 查询商城币接收报文 ====== \n" + rspXml);
			}

			// 解析BOSS报文
			if (null != rspXml && !"".equals(rspXml)) {
				// 解析报文 根节点
				Element root = this.config.getElement(rspXml);
				// 获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				// 设置结果信息
				this.getErrInfo(res, resp_code, "cc_qryshangchscore");
				
				Element resRoot = this.config.getElement(root, "content");
				// 成功
				if ("0000".equals(resp_code)) {
					scb = resRoot.getChildText("score_value") == null ? null : resRoot.getChildText("score_value");
					
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return scb;
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
			final QRY040047Result result) {
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
	
	//根据需求添加方法获取puk码
	private void getPuk(String accessId, ServiceConfig config, 
			List<RequestParameter> params, QRY040047Result res)
	{
		String reqXml = "";
		String rspXml = "";
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cquerysim_569", params);

			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cquerysim_569", this.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				//判断是否接受到成功报文
				if (BOSS_SUCCESS.equals(errCode))
				{					
					if (null != errCode && (BOSS_SUCCESS.equals(errCode))) 
					{
					
						res.setPuk1(root.getChild("content").getChildText("puk1"));
						res.setPuk2(root.getChild("content").getChildText("puk2"));
					}
				}
				else
				{
					
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
				}
			}
		} 
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
	/**
	 * 设置结果信息
	 * @param res - 实体类
	 * @param resp_code - 返回代码
	 * @param xmlName - xml报文
	 */
	public void getErrInfo(QRY040047Result res, String resp_code, String xmlName) {
		ErrorMapping errDt = null; // 错误编码解析

		try {
			// 设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code) ? "0" : "1");

			// 失败
			if (!"0000".equals(resp_code)) {
				// 解析错误信息
				errDt = this.wellFormedDAO.transBossErrCode("QRY040047", xmlName, resp_code);
				if (null != errDt) {
					res.setErrorCode(errDt.getLiErrCode()); 	// 设置错误编码
					res.setErrorMessage(errDt.getLiErrMsg()); 	// 设置错误信息
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

}

