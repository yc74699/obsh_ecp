package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.ProIncrement;
import com.xwtech.xwecp.service.logic.pojo.ProService;
import com.xwtech.xwecp.service.logic.pojo.QRY050035Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 产品互转 查询已开通附加功能和增值业务
 * 
 * @author 吴宗德
 *
 */
public class GetServiceAndIncInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(GetServiceAndIncInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public GetServiceAndIncInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY050035Result res = new QRY050035Result();
		try
		{
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			BaseResult serviceListRet = this.getServiceList(accessId, config, params);
			if (LOGIC_SUCESS.equals(serviceListRet.getResultCode())) {
				List<UserAddonFunctionBean> serviceList = (List<UserAddonFunctionBean>)serviceListRet.getReObj();
				
				BaseResult incListRet = this.getIncList(accessId, config, params);
				if (LOGIC_SUCESS.equals(incListRet.getResultCode())) {
					List<CcCGetIncInfoBean> incList = (List<CcCGetIncInfoBean>)incListRet.getReObj();
					
					//附加功能
					if (serviceList != null && serviceList.size() > 0) {
						List<ProService> serList = new ArrayList<ProService>(serviceList.size());
						ProService service = null;
						for (UserAddonFunctionBean objS:serviceList) {
							service = new ProService();
							
							String serviceId = objS.getSvcode();
							service.setServiceId(serviceId);
							service.setServiceName(getAdjFunDic().get(serviceId));
							
							serList.add(service);
						}
						res.setServices(serList);
					}
					
					//增值业务
					if (incList != null && incList.size() > 0) {
						List<ProIncrement> incrList = new ArrayList<ProIncrement>(incList.size());
						ProIncrement increment = null;
						for (CcCGetIncInfoBean objI:incList) {
							increment = new ProIncrement();
							
							String incId = objI.getSmscall_deal_code();
							
							increment.setIncrementId(incId);
							increment.setIncrementName(getAddIncDic().get(incId));
							
							incrList.add(increment);
						}
						res.setIncrements(incrList);
					}
				} else {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(incListRet.getErrorCode());
					res.setErrorMessage(incListRet.getErrorMessage());
				}
			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(serviceListRet.getErrorCode());
				res.setErrorMessage(serviceListRet.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 查询开通的附加功能
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult getServiceList(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		List<UserAddonFunctionBean> serviceList = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetusersvropt_79", params);

			logger.debug(" ====== 查询开通的附加功能请求报文 ======\n" + reqXml);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetusersvropt_79", this.generateCity(params)));
			logger.debug(" ====== 查询开通的附加功能返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY050035", "cc_cgetusersvropt_79", errCode);
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
					List sv_opt_usrid = null;
					try
					{
						sv_opt_usrid = root.getChild("content").getChildren("sv_opt_usrid");
					}
					catch (Exception e)
					{
						sv_opt_usrid = null;
					}
					if (null != sv_opt_usrid && sv_opt_usrid.size() > 0) {
						serviceList = new ArrayList<UserAddonFunctionBean>(sv_opt_usrid.size());
						UserAddonFunctionBean bean = null;
						for (int i = 0; i < sv_opt_usrid.size(); i++)
						{
							Element cuserserviceoptdt = ((Element)sv_opt_usrid.get(i)).getChild("cuserserviceoptdt");
							if (null != cuserserviceoptdt)
							{
								bean = new UserAddonFunctionBean();
								
								bean.setUsrid(p.matcher(cuserserviceoptdt.getChildText("sv_opt_usrid")).replaceAll(""));
								bean.setSvcode(p.matcher(cuserserviceoptdt.getChildText("sv_opt_svcode")).replaceAll(""));
								bean.setApplydate(p.matcher(cuserserviceoptdt.getChildText("sv_opt_applydate")).replaceAll(""));
								bean.setState(p.matcher(cuserserviceoptdt.getChildText("sv_opt_state")).replaceAll(""));
								bean.setEnddate(p.matcher(cuserserviceoptdt.getChildText("sv_opt_enddate")).replaceAll(""));
								bean.setChgdate(p.matcher(cuserserviceoptdt.getChildText("sv_opt_chgdate")).replaceAll(""));
								bean.setHissrl(p.matcher(cuserserviceoptdt.getChildText("sv_opt_hissrl")).replaceAll(""));
								bean.setOperating(p.matcher(cuserserviceoptdt.getChildText("sv_opt_operating")).replaceAll(""));
								
								serviceList.add(bean);
							}
						}
						res.setReObj(serviceList);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 查询开通的增值业务
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult getIncList(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		List<CcCGetIncInfoBean> incList = null;
		try {
			List<RequestParameter> paramNew = copyParam(params);
			List list = new ArrayList();
			paramNew.add(new RequestParameter("codeCount", list));
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetincinfo_346", params);

			logger.debug(" ====== 查询开通的增值业务请求报文 ======\n" + reqXml);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetincinfo_346", this.generateCity(params)));
			logger.debug(" ====== 查询开通的增值业务返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY050035", "cc_cgetincinfo_346", errCode);
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
					List smscall_deal_code = null;
					try
					{
						smscall_deal_code = root.getChild("content").getChildren("smscall_deal_code");
					}
					catch (Exception e)
					{
						smscall_deal_code = null;
					}
					if (null != smscall_deal_code && smscall_deal_code.size() > 0) {
						incList = new ArrayList<CcCGetIncInfoBean>(smscall_deal_code.size());
						CcCGetIncInfoBean bean = null;
						for (int i = 0; i < smscall_deal_code.size(); i++)
						{
							Element csmscalldt = ((Element)smscall_deal_code.get(i)).getChild("csmscalldt");
							if (null != csmscalldt)
							{
								bean = new CcCGetIncInfoBean();
								
								bean.setSmscall_state(p.matcher(csmscalldt.getChildText("smscall_state")).replaceAll(""));
								bean.setSmscall_start_date(p.matcher(csmscalldt.getChildText("smscall_start_date")).replaceAll(""));
			                    bean.setSmscall_operating_srl(p.matcher(csmscalldt.getChildText("smscall_operating_srl")).replaceAll(""));
			                    bean.setSmscall_reserved1(p.matcher(csmscalldt.getChildText("smscall_reserved1")).replaceAll(""));
			                    bean.setSmscall_reserved2(p.matcher(csmscalldt.getChildText("smscall_reserved2")).replaceAll(""));
			                    bean.setSmscall_remark(p.matcher(csmscalldt.getChildText("smscall_remark")).replaceAll(""));
			                    bean.setSmscall_deal_code(p.matcher(csmscalldt.getChildText("smscall_deal_code")).replaceAll(""));
			                    bean.setSmscall_end_date(p.matcher(csmscalldt.getChildText("smscall_end_date")).replaceAll(""));
			                    bean.setSmscall_gsm_user_id(p.matcher(csmscalldt.getChildText("smscall_gsm_user_id")).replaceAll(""));
			                    bean.setSmscall_apply_date(p.matcher(csmscalldt.getChildText("smscall_apply_date")).replaceAll(""));
								
			                    incList.add(bean);
							}
						}
						res.setReObj(incList);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
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
		
		/**
		 * @return the usrid
		 */
		public String getUsrid() {
			return usrid;
		}
		/**
		 * @param usrid the usrid to set
		 */
		public void setUsrid(String usrid) {
			this.usrid = usrid;
		}
		/**
		 * @return the svcode
		 */
		public String getSvcode() {
			return svcode;
		}
		/**
		 * @param svcode the svcode to set
		 */
		public void setSvcode(String svcode) {
			this.svcode = svcode;
		}
		/**
		 * @return the applydate
		 */
		public String getApplydate() {
			return applydate;
		}
		/**
		 * @param applydate the applydate to set
		 */
		public void setApplydate(String applydate) {
			this.applydate = applydate;
		}
		/**
		 * @return the startdate
		 */
		public String getStartdate() {
			return startdate;
		}
		/**
		 * @param startdate the startdate to set
		 */
		public void setStartdate(String startdate) {
			this.startdate = startdate;
		}
		/**
		 * @return the enddate
		 */
		public String getEnddate() {
			return enddate;
		}
		/**
		 * @param enddate the enddate to set
		 */
		public void setEnddate(String enddate) {
			this.enddate = enddate;
		}
		/**
		 * @return the chgdate
		 */
		public String getChgdate() {
			return chgdate;
		}
		/**
		 * @param chgdate the chgdate to set
		 */
		public void setChgdate(String chgdate) {
			this.chgdate = chgdate;
		}
		/**
		 * @return the hissrl
		 */
		public String getHissrl() {
			return hissrl;
		}
		/**
		 * @param hissrl the hissrl to set
		 */
		public void setHissrl(String hissrl) {
			this.hissrl = hissrl;
		}
		/**
		 * @return the operating
		 */
		public String getOperating() {
			return operating;
		}
		/**
		 * @param operating the operating to set
		 */
		public void setOperating(String operating) {
			this.operating = operating;
		}
		/**
		 * @return the state
		 */
		public String getState() {
			return state;
		}
		/**
		 * @param state the state to set
		 */
		public void setState(String state) {
			this.state = state;
		}
	}
	
	public class CcCGetIncInfoBean {
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
	
}