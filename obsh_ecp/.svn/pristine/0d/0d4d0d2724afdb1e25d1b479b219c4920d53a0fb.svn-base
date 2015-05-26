package com.xwtech.xwecp.service.logic.invocation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.xwtech.xwecp.service.logic.pojo.DEL011003Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.ProIncrement;
import com.xwtech.xwecp.service.logic.pojo.ProPackage;
import com.xwtech.xwecp.service.logic.pojo.ProSelf;
import com.xwtech.xwecp.service.logic.pojo.ProService;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 产品互转
 * 
 * @author 吴宗德
 *
 */
public class SwitchProductInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(SwitchProductInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public SwitchProductInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		DEL011003Result res = new DEL011003Result();
		try
		{
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			//旧产品编码
			String oldProId = "";
			//新产品编码
			String newProId = "";
			
			//用户在用的套餐
			List<ProPackage> oldPackages = null;
			//用户在用的附加功能
			List<ProService> oldServices = null;
			//用户在用的增值业务
			List<ProIncrement> oldIncrements = null;
			//要关闭的套餐
			List<ProPackage> closePackages = null;
			//要关闭的附加功能
			List<ProService> closeServices = null;
			//要关闭的增值业务
			List<ProIncrement> closeIncrements = null;
			//要开通的套餐
			List<ProPackage> openPackages = null;
			//要开通的附加功能
			List<ProService> openServices = null;
			//要开通的增值业务
			List<ProIncrement> openIncrements = null;
			//要开通的自有业务
			List<ProSelf> openSelfs = null;
			
			for (RequestParameter param:params) {
				if ("oldProId".equals(param.getParameterName())) {
					oldProId = (String)param.getParameterValue();
				}
				if ("newProId".equals(param.getParameterName())) {
					newProId = (String)param.getParameterValue();
				}
				if ("oldPackages".equals(param.getParameterName())) {
					oldPackages = (List<ProPackage>)param.getParameterValue();
				}
				if ("oldServices".equals(param.getParameterName())) {
					oldServices = (List<ProService>)param.getParameterValue();
				}
				if ("oldIncrements".equals(param.getParameterName())) {
					oldIncrements = (List<ProIncrement>)param.getParameterValue();
				}
				if ("closePackages".equals(param.getParameterName())) {
					closePackages = (List<ProPackage>)param.getParameterValue();
				}
				if ("closeServices".equals(param.getParameterName())) {
					closeServices = (List<ProService>)param.getParameterValue();
				}
				if ("closeIncrements".equals(param.getParameterName())) {
					closeIncrements = (List<ProIncrement>)param.getParameterValue();
				}
				if ("openPackages".equals(param.getParameterName())) {
					openPackages = (List<ProPackage>)param.getParameterValue();
				}
				if ("openServices".equals(param.getParameterName())) {
					openServices = (List<ProService>)param.getParameterValue();
				}
				if ("openIncrements".equals(param.getParameterName())) {
					openIncrements = (List<ProIncrement>)param.getParameterValue();
				}
				if ("openSelfs".equals(param.getParameterName())) {
					openSelfs = (List<ProSelf>)param.getParameterValue();
				}
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			
			Calendar cal = Calendar.getInstance();
			
			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			//次月第一天
			String startDate = sdf.format(cal.getTime());
			
			cal.add(Calendar.DAY_OF_MONTH, -1);
			//当月最后一天
			String lastDate = sdf.format(cal.getTime());
			
			List<CUserProductFreezeInfoDtBean> CUserProductFreezeInfoDts = new ArrayList<CUserProductFreezeInfoDtBean>();
			CUserProductFreezeInfoDtBean bean1 = null;
			
			List<PackageChgBean> cplanpackagedts = new ArrayList<PackageChgBean>();
			PackageChgBean bean2 = null;
			
			List<CUserServiceOptDtBean> CUserServiceOptDts = new ArrayList<CUserServiceOptDtBean>();
			CUserServiceOptDtBean bean3 = null;
			
			List<CSmsCallDtBean> CSmsCallDts = new ArrayList<CSmsCallDtBean>();
			CSmsCallDtBean bean4 = null;
			
			List<CUserServOptXpInfoDtBean> CUserServOptXpInfoDts = new ArrayList<CUserServOptXpInfoDtBean>();
			CUserServOptXpInfoDtBean bean5 = null;
			
			List<CUserIncrementXpInfoDtBean> CUserIncrementXpInfoDts = new ArrayList<CUserIncrementXpInfoDtBean>();
			CUserIncrementXpInfoDtBean bean6 = null;
			
			List<CSelfPlatBizDtBean> CSelfPlatBizDts = new ArrayList<CSelfPlatBizDtBean>();
			CSelfPlatBizDtBean bean7 = null;
			
			if (null != openPackages && openPackages.size() > 0) {
				for (ProPackage objOpn:openPackages) {
					int openFlag = 1;
					int closeFlag = 0;
					
					// 在关闭列表中的不再开通（除了business_id=1009和1017的）
					if (null != closePackages && closePackages.size() > 0) {
						for (ProPackage objCls:closePackages) {
							if (objOpn.getPkgId().equals(objCls.getPkgId())) {
								if (!"1009".equals(objOpn.getTypeId()) && !"1017".equals(objOpn.getTypeId())) {
									closeFlag = 1;
									openFlag = 0;
									break;
								}
							}
						}
					}
					// 不在关闭列表中，但是已经开通且没有预约关闭的不开通（除了business_id=1009和1017的）
					if (closeFlag == 0) {
						if (null != oldPackages && oldPackages.size() > 0) {
							for (ProPackage objOld:oldPackages) {
								if (objOpn.getPkgId().equals(objOld.getPkgId()) && "".equals(objOld.getEndDate())) {
									if (!"1009".equals(objOpn.getTypeId()) && !"1017".equals(objOpn.getTypeId())) {
										openFlag = 0;
										break;
									}
								}
							}
						}
					}
					
					if (openFlag == 1) {
						bean2 = new PackageChgBean();
						
						bean2.setProduct_switch_package_flag("1");
						bean2.setProduct_switch_package_reason("11");
						bean2.setUser_id("");
						bean2.setType(objOpn.getTypeId());
						bean2.setLevel("0");
						bean2.setCode(objOpn.getPkgId());
						bean2.setApply_date("");
						bean2.setUse_date(startDate + "000000");
						bean2.setHistory_srl("");
						bean2.setEnd_date("");
						bean2.setChange_date("");
						bean2.setState("0");
						
						cplanpackagedts.add(bean2);
					}
					if (objOpn.getFreezePeriod() > 0) {
						bean1 = new CUserProductFreezeInfoDtBean();
						
						bean1.setProduct_info_id("");
						bean1.setDeal_type("4");
						bean1.setDeal_code(objOpn.getPackId());
						bean1.setBegin_date(startDate + "000000");
						
						Calendar calI = Calendar.getInstance();
						
						calI.add(Calendar.MONTH, 1 + objOpn.getFreezePeriod());
						calI.set(Calendar.DAY_OF_MONTH, 1);
						calI.add(Calendar.DAY_OF_MONTH, -1);
						
						String endDate = sdf.format(calI.getTime()) + "235959";
						
						bean1.setEnd_date(endDate);
						
						bean1.setOperating_srl("");
						bean1.setReserved_1("");
						bean1.setReserved_2("");
						bean1.setUser_id("");
						
						CUserProductFreezeInfoDts.add(bean1);
					}
				}
			}
			
			if (null != openServices && openServices.size() > 0) {
				for (ProService objOpn:openServices) {
					int openFlag = 1;
					int closeFlag = 0;
					
					if (null != closeServices && closeServices.size() > 0) {
						for (ProService objCls:closeServices) {
							if (objOpn.getServiceId().equals(objCls.getServiceId())) {
								closeFlag = 1;
								openFlag = 0;
								break;
							}
						}
					}
					
					if (closeFlag == 0) {
						if (null != oldServices && oldServices.size() > 0) {
							for (ProService objOld:oldServices) {
								if (objOpn.getServiceId().equals(objOld.getServiceId())) {
									openFlag = 0;
									break;
								}
							}
						}
					}
					
					if (openFlag == 1) {
						bean3 = new CUserServiceOptDtBean();
						
						bean3.setProduct_switch_service_opt_flag("1");
						bean3.setProduct_switch_service_opt_reason("12");
						bean3.setState("1");
						bean3.setSvcode(objOpn.getServiceId());
						bean3.setStartdate(startDate + "000000");
						
						CUserServiceOptDts.add(bean3);
					}
					
					if (objOpn.getExperiencePeriod() > 0 && objOpn.getIsExperience() == 1) {
						bean5 = new CUserServOptXpInfoDtBean();
						
						bean5.setService_code(objOpn.getServiceId());
						bean5.setStart_date(startDate);
						bean5.setProduct_id(newProId);
						
						Calendar calI = Calendar.getInstance();
						
						calI.add(Calendar.MONTH, 1 + objOpn.getFreezePeriod());
						calI.set(Calendar.DAY_OF_MONTH, 1);
						calI.add(Calendar.DAY_OF_MONTH, -1);
						
						String endDate = sdf.format(calI.getTime()) + "235959";
						
						bean5.setEnd_date(endDate);
						bean5.setEnd_reason("1");
						bean5.setIs_continue_use("0");
						bean5.setCreate_operator("99700005");
						bean5.setChange_operator("99700005");
						
						CUserServOptXpInfoDts.add(bean5);
					}
					
					if (objOpn.getFreezePeriod() > 0) {
						bean1 = new CUserProductFreezeInfoDtBean();
						
						bean1.setDeal_type("1");
						bean1.setDeal_code(objOpn.getServiceId());
						bean1.setBegin_date(startDate + "000000");
						
						Calendar calI = Calendar.getInstance();
						
						calI.add(Calendar.MONTH, 1 + objOpn.getFreezePeriod());
						calI.set(Calendar.DAY_OF_MONTH, 1);
						calI.add(Calendar.DAY_OF_MONTH, -1);
						
						String endDate = sdf.format(calI.getTime()) + "235959";
						
						bean1.setEnd_date(endDate);
						
						CUserProductFreezeInfoDts.add(bean1);
					}
				}
			}
			
			if (null != openIncrements && openIncrements.size() > 0) {
				for (ProIncrement objOpn:openIncrements) {
					int openFlag = 1;
					int closeFlag = 0;
					
					if (null != closeIncrements && closeIncrements.size() > 0) {
						for (ProIncrement objCls:closeIncrements) {
							if (objOpn.getIncrementId().equals(objCls.getIncrementId())) {
								closeFlag = 1;
								openFlag = 0;
								break;
							}
						}
					}
					
					if (closeFlag == 0) {
						if (null != oldIncrements && oldIncrements.size() > 0) {
							for (ProIncrement objOld:oldIncrements) {
								if (objOpn.getIncrementId().equals(objOld.getIncrementId())) {
									openFlag = 0;
									break;
								}
							}
						}
					}
					
					if (openFlag == 1) {
						bean4 = new CSmsCallDtBean();
						
						bean4.setProduct_switch_sms_call_flag("1");
						bean4.setProduct_switch_sms_call_reason("12");
						bean4.setState("1");
						bean4.setStart_date(startDate + "000000");
						bean4.setDeal_code(objOpn.getIncrementId());
						
						CSmsCallDts.add(bean4);
					}
					
					if (objOpn.getExperiencePeriod() > 0 && objOpn.getIsExperience() == 1) {
						bean6 = new CUserIncrementXpInfoDtBean();
						
						bean6.setIncrement_id(objOpn.getIncrementId());
						bean6.setStart_date(startDate + "000000");
						bean6.setProduct_id(newProId);
						
						Calendar calI = Calendar.getInstance();
						
						calI.add(Calendar.MONTH, 1 + objOpn.getFreezePeriod());
						calI.set(Calendar.DAY_OF_MONTH, 1);
						calI.add(Calendar.DAY_OF_MONTH, -1);
						
						String endDate = sdf.format(calI.getTime()) + "235959";
						bean6.setEnd_date(endDate);
						bean6.setEnd_reason("1");
						bean6.setIs_continue_use("0");
						bean5.setCreate_operator("99700005");
						bean5.setChange_operator("99700005");
						
						CUserIncrementXpInfoDts.add(bean6);
					}
					
					if (objOpn.getFreezePeriod() > 0) {
						bean1 = new CUserProductFreezeInfoDtBean();
						
						bean1.setDeal_type("2");
						bean1.setDeal_code(objOpn.getIncrementId());
						bean1.setBegin_date(DateTimeUtil.getTodayChar14());
						
						Calendar calI = Calendar.getInstance();
						
						calI.add(Calendar.MONTH, 1 + objOpn.getFreezePeriod());
						calI.set(Calendar.DAY_OF_MONTH, 1);
						calI.add(Calendar.DAY_OF_MONTH, -1);
						
						String endDate = sdf.format(calI.getTime()) + "235959";
						bean1.setEnd_date(endDate);
						
						CUserProductFreezeInfoDts.add(bean1);
					}
				}
			}
			
			if (null != openSelfs && openSelfs.size() > 0) {
				for (ProSelf objOpn:openSelfs) {
					bean7 = new CSelfPlatBizDtBean();
					
					bean7.setDomain_code(objOpn.getSelfId1());
					bean7.setBiz_code(objOpn.getSelfId2());
					bean7.setPrd_code(objOpn.getSelfId3());
					bean7.setProduct_switch_selfplat_flag("");
					
					CSelfPlatBizDts.add(bean7);
				}
			}
			
			if (null != closePackages && closePackages.size() > 0) {
				for (ProPackage objCls:closePackages) {
					int closeFlag = 1;
					
					if (null != openPackages && openPackages.size() > 0) {
						for (ProPackage objOpn:openPackages) {
							if (objCls.getPkgId().equals(objOpn.getPkgId())) {
								if (!"1009".equals(objCls.getTypeId()) && !"1017".equals(objCls.getTypeId())) {
									closeFlag = 0;
									break;
								}
							}
						}
					}
					
					if (closeFlag == 1) {
						bean2 = new PackageChgBean();
						
						bean2.setProduct_switch_package_flag("0");
						bean2.setProduct_switch_package_reason("2");
						bean2.setType(objCls.getTypeId());
						bean2.setLevel(objCls.getPkgLevel());
						bean2.setCode(objCls.getPkgId());
						bean2.setApply_date("");
						bean2.setUse_date(objCls.getBeginDate());
						bean2.setHistory_srl("");
						bean2.setEnd_date(lastDate + "235959");
						bean2.setChange_date("");
						bean2.setState(objCls.getState());
						
						cplanpackagedts.add(bean2);
					}
				}
			}
			
			if (null != closeServices && closeServices.size() > 0) {
				for (ProService objCls:closeServices) {
					int closeFlag = 1;
					
					if (null != openServices && openServices.size() > 0) {
						for (ProService objOpn:openServices) {
							if (objCls.getServiceId().equals(objOpn.getServiceId())) {
								closeFlag = 0;
								break;
							}
						}
					}
					
					if (closeFlag == 1) {
						bean3 = new CUserServiceOptDtBean();
						
						bean3.setProduct_switch_service_opt_flag("0");
						bean3.setProduct_switch_service_opt_reason("2");
						bean3.setSvcode(objCls.getServiceId());
//						bean3.setApplydate(objCls.getApplydate());
						bean3.setState(objCls.getState());
//						bean3.setOperating(objCls.getOperating());
						bean3.setEnddate(objCls.getEndDate());
//						bean3.setHissrl(objCls.getHissrl());
//						bean3.setChgdate(objCls.getChgdate());
						bean3.setStartdate(objCls.getStartDate());
						
						CUserServiceOptDts.add(bean3);
					}
				}
			}
			
			if (null != closeIncrements && closeIncrements.size() > 0) {
				for (ProIncrement objCls:closeIncrements) {
					int closeFlag = 1;
					
					if (null != openIncrements && openIncrements.size() > 0) {
						for (ProIncrement objOpn:openIncrements) {
							if (objCls.getIncrementId().equals(objOpn.getIncrementId())) {
								closeFlag = 0;
								break;
							}
						}
					}
					
					if (closeFlag == 1) {
						bean4 = new CSmsCallDtBean();
						
						bean4.setProduct_switch_sms_call_flag("0");
						bean4.setProduct_switch_sms_call_reason("2");
//						bean4.setApply_date(objCls.getApply_date());
						bean4.setState(objCls.getState());
						bean4.setStart_date(objCls.getStartDate());
						bean4.setEnd_date(objCls.getEndDate());
//						bean4.setOperating_srl(objCls.getOperating_srl());
						bean4.setDeal_code(objCls.getIncrementId());
//						bean4.setRemark(objCls.getRemark());
//						bean4.setReserved1(objCls.getReserved1());
//						bean4.setReserved2(objCls.getReserved2());
						
						CSmsCallDts.add(bean4);
					}
				}
			}
			
			params.add(new RequestParameter("codes1", CUserProductFreezeInfoDts));
			params.add(new RequestParameter("codes2", cplanpackagedts));
			params.add(new RequestParameter("codes3", CUserServiceOptDts));
			params.add(new RequestParameter("codes4", CSmsCallDts));
			params.add(new RequestParameter("codes5", CUserServOptXpInfoDts));
			params.add(new RequestParameter("codes6", CUserIncrementXpInfoDts));
			params.add(new RequestParameter("codes7", CSelfPlatBizDts));
			
			
			BaseResult switchProductRet = this.switchProduct(accessId, config, params);
			if (!LOGIC_SUCESS.equals(switchProductRet.getResultCode())) {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(switchProductRet.getErrorCode());
				res.setErrorMessage(switchProductRet.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			res.setResultCode(LOGIC_ERROR);
			res.setErrorCode(LOGIC_EXCEPTION);
			logger.error(e, e);
		}
		return res;
	}
	
	
	/**
	 * 产品互转
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult switchProduct(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cproductswitch_354", params);

			logger.debug(" ====== 产品互转请求报文 ======\n" + reqXml);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cproductswitch_354", this.generateCity(params)));
			logger.debug(" ====== 产品互转返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL011003", "cc_cproductswitch_354", errCode);
					if (null != errDt)
					{
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
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
	
	public class CUserProductFreezeInfoDtBean {

		/**
		 * 用户产品标识代码
		 */
		private String product_info_id;

		/**
		 * 业务类型： 1－附加功能； 2－增值业务； 3－梦网业务； 4－套餐
		 */
		private String deal_type;

		/**
		 * 如果是套餐，则为套餐包标识； 如果是附加功能，则为附加功能代码； 如果是增值业务，则为增值业务代码； 如果是梦网业务，则为梦网业务代码
		 */
		private String deal_code;

		/**
		 * 冻结期开始时间； 入网时填，入网时间； 转换时，填新产品生效时间（即次月）； 预配号，填2100/01/01，在预配号资料录入（或第一次拨打电话时），有进程同步为用户成为正式用户的时间。
		 */
		private String begin_date;

		/**
		 * 冻结结束时间，根据开始时间和产品配置的冻结时长计算！
		 */
		private String end_date;

		/**
		 * 操作流水，入网则填入网时的操作流水； 产品转换，则填产品转换的操作流水； 预配，则填预配的操作流水，预配号用户在资料录入（或第一次拨打电话）而修改冻结开始时间时，不修改操作流水
		 */
		private String operating_srl;

		/**
		 * 备用，预留字段1；
		 */
		private String reserved_1;

		/**
		 * 备用，预留字段2
		 */
		private String reserved_2;

		/**
		 * 用户ID
		 */
		private String user_id;

		public CUserProductFreezeInfoDtBean() {
			super();
			this.product_info_id = "";
			this.deal_type = "";
			this.deal_code = "";
			this.begin_date = "";
			this.end_date = "";
			this.operating_srl = "";
			this.reserved_1 = "";
			this.reserved_2 = "";
			this.user_id = "";
		}

		public CUserProductFreezeInfoDtBean(String product_info_id, String deal_type, String deal_code, String begin_date, String end_date, String operating_srl, String reserved_1, String reserved_2, String user_id) {
			super();
			this.product_info_id = product_info_id;
			this.deal_type = deal_type;
			this.deal_code = deal_code;
			this.begin_date = begin_date;
			this.end_date = end_date;
			this.operating_srl = operating_srl;
			this.reserved_1 = reserved_1;
			this.reserved_2 = reserved_2;
			this.user_id = user_id;
		}

		public String getBegin_date() {
			return begin_date;
		}

		public void setBegin_date(String begin_date) {
			this.begin_date = begin_date;
		}

		public String getDeal_code() {
			return deal_code;
		}

		public void setDeal_code(String deal_code) {
			this.deal_code = deal_code;
		}

		public String getDeal_type() {
			return deal_type;
		}

		public void setDeal_type(String deal_type) {
			this.deal_type = deal_type;
		}

		public String getEnd_date() {
			return end_date;
		}

		public void setEnd_date(String end_date) {
			this.end_date = end_date;
		}

		public String getOperating_srl() {
			return operating_srl;
		}

		public void setOperating_srl(String operating_srl) {
			this.operating_srl = operating_srl;
		}

		public String getProduct_info_id() {
			return product_info_id;
		}

		public void setProduct_info_id(String product_info_id) {
			this.product_info_id = product_info_id;
		}

		public String getReserved_1() {
			return reserved_1;
		}

		public void setReserved_1(String reserved_1) {
			this.reserved_1 = reserved_1;
		}

		public String getReserved_2() {
			return reserved_2;
		}

		public void setReserved_2(String reserved_2) {
			this.reserved_2 = reserved_2;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

	}
	
	public class PackageChgBean {
		private String user_id;

		private String product_switch_package_flag;

		/**
		 * 查询套餐类型
		 */
		private String type;

		private String level;

		private String code;

		private String apply_date;

		private String use_date;

		private String history_srl;

		/**
		 * 查询套餐结束时间
		 */
		private String end_date;

		private String change_date;

		private String state;

		/**
		 * 套餐变更原因
		 */
		private String product_switch_package_reason;

		public PackageChgBean() {
			super();
			this.user_id = "";
			this.product_switch_package_flag = "";
			this.type = "";
			this.level = "";
			this.code = "";
			this.apply_date = "";
			this.use_date = "";
			this.history_srl = "";
			this.end_date = "";
			this.change_date = "";
			this.state = "";
			this.product_switch_package_reason = "";
		}

		public PackageChgBean(String user_id, String product_switch_package_flag, String type, String level, String code, String apply_date, String use_date, String history_srl, String end_date, String change_date, String state, String product_switch_package_reason) {
			super();
			this.user_id = user_id;
			this.product_switch_package_flag = product_switch_package_flag;
			this.type = type;
			this.level = level;
			this.code = code;
			this.apply_date = apply_date;
			this.use_date = use_date;
			this.history_srl = history_srl;
			this.end_date = end_date;
			this.change_date = change_date;
			this.state = state;
			this.product_switch_package_reason = product_switch_package_reason;
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

		public String getProduct_switch_package_flag() {
			return product_switch_package_flag;
		}

		public void setProduct_switch_package_flag(String product_switch_package_flag) {
			this.product_switch_package_flag = product_switch_package_flag;
		}

		public String getProduct_switch_package_reason() {
			return product_switch_package_reason;
		}

		public void setProduct_switch_package_reason(
				String product_switch_package_reason) {
			this.product_switch_package_reason = product_switch_package_reason;
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
	
	public class CUserServiceOptDtBean {

		/**
		 * 用户代码(前2位为地市代码)
		 */
		private String usrid;

		/**
		 * 用户代码(前2位为地市代码)
		 */
		private String product_switch_service_opt_flag;

		/**
		 * 附加功能代码
		 */
		private String svcode;

		/**
		 * 申请时间
		 */
		private String applydate;

		/**
		 * 附加功能状态(0-关 1-开)
		 */
		private String state;

		/**
		 * 操作流水
		 */
		private String operating;

		/**
		 * 结束使用时间
		 */
		private String enddate;

		/**
		 * 历史流水
		 */
		private String hissrl;

		/**
		 * 修改时间
		 */
		private String chgdate;

		/**
		 * 开始时间
		 */
		private String startdate;

		/**
		 * 附加功能变更原因
		 */
		private String product_switch_service_opt_reason;

		public CUserServiceOptDtBean() {
			super();
			this.usrid = "";
			this.product_switch_service_opt_flag = "";
			this.svcode = "";
			this.applydate = "";
			this.state = "";
			this.operating = "";
			this.enddate = "";
			this.hissrl = "";
			this.chgdate = "";
			this.startdate = "";
			this.product_switch_service_opt_reason = "";
		}

		public CUserServiceOptDtBean(String usrid, String product_switch_service_opt_flag, String svcode, String applydate, String state, String operating, String enddate, String hissrl, String chgdate, String startdate, String product_switch_service_opt_reason) {
			super();
			this.usrid = usrid;
			this.product_switch_service_opt_flag = product_switch_service_opt_flag;
			this.svcode = svcode;
			this.applydate = applydate;
			this.state = state;
			this.operating = operating;
			this.enddate = enddate;
			this.hissrl = hissrl;
			this.chgdate = chgdate;
			this.startdate = startdate;
			this.product_switch_service_opt_reason = product_switch_service_opt_reason;
		}

		public String getApplydate() {
			return applydate;
		}

		public void setApplydate(String applydate) {
			this.applydate = applydate;
		}

		public String getChgdate() {
			return chgdate;
		}

		public void setChgdate(String chgdate) {
			this.chgdate = chgdate;
		}

		public String getEnddate() {
			return enddate;
		}

		public void setEnddate(String enddate) {
			this.enddate = enddate;
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

		public String getProduct_switch_service_opt_flag() {
			return product_switch_service_opt_flag;
		}

		public void setProduct_switch_service_opt_flag(
				String product_switch_service_opt_flag) {
			this.product_switch_service_opt_flag = product_switch_service_opt_flag;
		}

		public String getProduct_switch_service_opt_reason() {
			return product_switch_service_opt_reason;
		}

		public void setProduct_switch_service_opt_reason(
				String product_switch_service_opt_reason) {
			this.product_switch_service_opt_reason = product_switch_service_opt_reason;
		}

		public String getStartdate() {
			return startdate;
		}

		public void setStartdate(String startdate) {
			this.startdate = startdate;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getSvcode() {
			return svcode;
		}

		public void setSvcode(String svcode) {
			this.svcode = svcode;
		}

		public String getUsrid() {
			return usrid;
		}

		public void setUsrid(String usrid) {
			this.usrid = usrid;
		}

	}
	
	public class CSmsCallDtBean {
		private String apply_date;

		private String product_switch_sms_call_flag;

		private String state;

		private String gsm_user_id;

		private String start_date;

		private String end_date;

		private String operating_srl;

		private String deal_code;

		private String remark;

		private String reserved1;

		private String reserved2;

		private String product_switch_sms_call_reason;

		public CSmsCallDtBean() {
			super();
			this.apply_date = "";
			this.product_switch_sms_call_flag = "";
			this.state = "";
			this.gsm_user_id = "";
			this.start_date = "";
			this.end_date = "";
			this.operating_srl = "";
			this.deal_code = "";
			this.remark = "";
			this.reserved1 = "";
			this.reserved2 = "";
			this.product_switch_sms_call_reason = "";
		}

		public CSmsCallDtBean(String apply_date, String product_switch_sms_call_flag, String state, String gsm_user_id, String start_date, String end_date, String operating_srl, String deal_code, String remark, String reserved1, String reserved2, String product_switch_sms_call_reason) {
			super();
			this.apply_date = apply_date;
			this.product_switch_sms_call_flag = product_switch_sms_call_flag;
			this.state = state;
			this.gsm_user_id = gsm_user_id;
			this.start_date = start_date;
			this.end_date = end_date;
			this.operating_srl = operating_srl;
			this.deal_code = deal_code;
			this.remark = remark;
			this.reserved1 = reserved1;
			this.reserved2 = reserved2;
			this.product_switch_sms_call_reason = product_switch_sms_call_reason;
		}

		public String getApply_date() {
			return apply_date;
		}

		public void setApply_date(String apply_date) {
			this.apply_date = apply_date;
		}

		public String getDeal_code() {
			return deal_code;
		}

		public void setDeal_code(String deal_code) {
			this.deal_code = deal_code;
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

		public String getOperating_srl() {
			return operating_srl;
		}

		public void setOperating_srl(String operating_srl) {
			this.operating_srl = operating_srl;
		}

		public String getProduct_switch_sms_call_flag() {
			return product_switch_sms_call_flag;
		}

		public void setProduct_switch_sms_call_flag(String product_switch_sms_call_flag) {
			this.product_switch_sms_call_flag = product_switch_sms_call_flag;
		}

		public String getProduct_switch_sms_call_reason() {
			return product_switch_sms_call_reason;
		}

		public void setProduct_switch_sms_call_reason(
				String product_switch_sms_call_reason) {
			this.product_switch_sms_call_reason = product_switch_sms_call_reason;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getReserved1() {
			return reserved1;
		}

		public void setReserved1(String reserved1) {
			this.reserved1 = reserved1;
		}

		public String getReserved2() {
			return reserved2;
		}

		public void setReserved2(String reserved2) {
			this.reserved2 = reserved2;
		}

		public String getStart_date() {
			return start_date;
		}

		public void setStart_date(String start_date) {
			this.start_date = start_date;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

	}
	
	public class CUserServOptXpInfoDtBean {

		/**
		 * 用户产品标识，地市代码+14位序列号
		 */
		private String product_info_id;

		/**
		 * 附加功能业务代码
		 */
		private String service_code;

		/**
		 * 体验开始时间
		 */
		private String start_date;

		/**
		 * 
		 */
		private String operating_srl;

		/**
		 * 用户标识
		 */
		private String user_id;

		/**
		 * 产品编码
		 */
		private String product_id;

		/**
		 * 体验结束时间
		 */
		private String end_date;

		/**
		 * 体验结束原因； 1－体验到期，结束体验， 2－产品转换，结束体验  在资料生成时，填1（即默认都是体验到期，自动结束），当产品转换时，对原产品没有到期的体验信息，该该原因为2；
		 */
		private String end_reason;

		/**
		 * 体验结束处理时间； 当体验到期处理方式是“到期关闭”，则填系统关闭对应业务的处理时间； 产品转换，结束体验，则填产品转换时间
		 */
		private String deal_date;

		/**
		 * 是否继续使用： 0－不继续使用； 1－继续使用
		 */
		private String is_continue_use;

		/**
		 * 创建人员
		 */
		private String create_operator;

		/**
		 * 创建时间
		 */
		private String create_date;

		/**
		 * 修改人员
		 */
		private String change_operator;

		/**
		 * 修改时间
		 */
		private String change_date;

		public CUserServOptXpInfoDtBean() {
			super();
			this.product_info_id = "";
			this.service_code = "";
			this.start_date = "";
			this.operating_srl = "";
			this.user_id = "";
			this.product_id = "";
			this.end_date = "";
			this.end_reason = "";
			this.deal_date = "";
			this.is_continue_use = "";
			this.create_operator = "";
			this.create_date = "";
			this.change_operator = "";
			this.change_date = "";
		}

		public CUserServOptXpInfoDtBean(String product_info_id, String service_code, String start_date, String operating_srl, String user_id, String product_id, String end_date, String end_reason, String deal_date, String is_continue_use, String create_operator, String create_date, String change_operator, String change_date) {
			super();
			this.product_info_id = product_info_id;
			this.service_code = service_code;
			this.start_date = start_date;
			this.operating_srl = operating_srl;
			this.user_id = user_id;
			this.product_id = product_id;
			this.end_date = end_date;
			this.end_reason = end_reason;
			this.deal_date = deal_date;
			this.is_continue_use = is_continue_use;
			this.create_operator = create_operator;
			this.create_date = create_date;
			this.change_operator = change_operator;
			this.change_date = change_date;
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

		public String getDeal_date() {
			return deal_date;
		}

		public void setDeal_date(String deal_date) {
			this.deal_date = deal_date;
		}

		public String getEnd_date() {
			return end_date;
		}

		public void setEnd_date(String end_date) {
			this.end_date = end_date;
		}

		public String getEnd_reason() {
			return end_reason;
		}

		public void setEnd_reason(String end_reason) {
			this.end_reason = end_reason;
		}

		public String getIs_continue_use() {
			return is_continue_use;
		}

		public void setIs_continue_use(String is_continue_use) {
			this.is_continue_use = is_continue_use;
		}

		public String getOperating_srl() {
			return operating_srl;
		}

		public void setOperating_srl(String operating_srl) {
			this.operating_srl = operating_srl;
		}

		public String getProduct_id() {
			return product_id;
		}

		public void setProduct_id(String product_id) {
			this.product_id = product_id;
		}

		public String getProduct_info_id() {
			return product_info_id;
		}

		public void setProduct_info_id(String product_info_id) {
			this.product_info_id = product_info_id;
		}

		public String getService_code() {
			return service_code;
		}

		public void setService_code(String service_code) {
			this.service_code = service_code;
		}

		public String getStart_date() {
			return start_date;
		}

		public void setStart_date(String start_date) {
			this.start_date = start_date;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

	}
	
	public class CUserIncrementXpInfoDtBean {

		/**
		 * 用户产品标识，地市代码+14位序列号
		 */
		private String product_info_id;

		/**
		 * 数据增值业务编码
		 */
		private String increment_id;

		/**
		 * 体验开始时间
		 */
		private String start_date;

		/**
		 * 流水
		 */
		private String operating_srl;

		/**
		 * 用户标识
		 */
		private String user_id;

		/**
		 * 产品编码
		 */
		private String product_id;

		/**
		 * 体验结束时间
		 */
		private String end_date;

		/**
		 * 体验结束原因； 1－体验到期，结束体验， 2－产品转换，结束体验  在资料生成时，填1（即默认都是体验到期，自动结束），当产品转换时，对原产品没有到期的体验信息，该该原因为2；
		 */
		private String end_reason;

		/**
		 * 体验结束处理时间； 当体验到期处理方式是“到期关闭”，则填系统关闭对应业务的处理时间； 产品转换，结束体验，则填产品转换时间
		 */
		private String deal_date;

		/**
		 * 是否继续使用； 0－不继续使用， 1－继续使用
		 */
		private String is_continue_use;

		/**
		 * 创建人员
		 */
		private String create_operator;

		/**
		 * 创建时间
		 */
		private String create_date;

		/**
		 * 修改人员
		 */
		private String change_operator;

		/**
		 * 修改时间
		 */
		private String change_date;

		public CUserIncrementXpInfoDtBean() {
			super();
		}

		public CUserIncrementXpInfoDtBean(String product_info_id, String increment_id, String start_date, String operating_srl, String user_id, String product_id, String end_date, String end_reason, String deal_date, String is_continue_use, String create_operator, String create_date, String change_operator, String change_date) {
			super();
			this.product_info_id = product_info_id;
			this.increment_id = increment_id;
			this.start_date = start_date;
			this.operating_srl = operating_srl;
			this.user_id = user_id;
			this.product_id = product_id;
			this.end_date = end_date;
			this.end_reason = end_reason;
			this.deal_date = deal_date;
			this.is_continue_use = is_continue_use;
			this.create_operator = create_operator;
			this.create_date = create_date;
			this.change_operator = change_operator;
			this.change_date = change_date;
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

		public String getDeal_date() {
			return deal_date;
		}

		public void setDeal_date(String deal_date) {
			this.deal_date = deal_date;
		}

		public String getEnd_date() {
			return end_date;
		}

		public void setEnd_date(String end_date) {
			this.end_date = end_date;
		}

		public String getEnd_reason() {
			return end_reason;
		}

		public void setEnd_reason(String end_reason) {
			this.end_reason = end_reason;
		}

		public String getIncrement_id() {
			return increment_id;
		}

		public void setIncrement_id(String increment_id) {
			this.increment_id = increment_id;
		}

		public String getIs_continue_use() {
			return is_continue_use;
		}

		public void setIs_continue_use(String is_continue_use) {
			this.is_continue_use = is_continue_use;
		}

		public String getOperating_srl() {
			return operating_srl;
		}

		public void setOperating_srl(String operating_srl) {
			this.operating_srl = operating_srl;
		}

		public String getProduct_id() {
			return product_id;
		}

		public void setProduct_id(String product_id) {
			this.product_id = product_id;
		}

		public String getProduct_info_id() {
			return product_info_id;
		}

		public void setProduct_info_id(String product_info_id) {
			this.product_info_id = product_info_id;
		}

		public String getStart_date() {
			return start_date;
		}

		public void setStart_date(String start_date) {
			this.start_date = start_date;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

	}
	
	public class CSelfPlatBizDtBean {
		private String domain_code;

		private String product_switch_selfplat_flag;

		private String biz_code;

		private String prd_code;
		
		public CSelfPlatBizDtBean() {
			super();
		}

		public CSelfPlatBizDtBean(String domain_code, String product_switch_selfplat_flag, String biz_code, String prd_code) {
			super();
			this.domain_code = domain_code;
			this.product_switch_selfplat_flag = product_switch_selfplat_flag;
			this.biz_code = biz_code;
			this.prd_code = prd_code;
		}

		public String getBiz_code() {
			return biz_code;
		}

		public void setBiz_code(String biz_code) {
			this.biz_code = biz_code;
		}

		public String getDomain_code() {
			return domain_code;
		}

		public void setDomain_code(String domain_code) {
			this.domain_code = domain_code;
		}

		public String getPrd_code() {
			return prd_code;
		}

		public void setPrd_code(String prd_code) {
			this.prd_code = prd_code;
		}

		public String getProduct_switch_selfplat_flag() {
			return product_switch_selfplat_flag;
		}

		public void setProduct_switch_selfplat_flag(String product_switch_selfplat_flag) {
			this.product_switch_selfplat_flag = product_switch_selfplat_flag;
		}

	}
	
}