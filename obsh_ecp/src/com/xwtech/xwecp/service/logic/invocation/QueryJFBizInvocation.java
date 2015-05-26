package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
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
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 积分类业务查询
 * 
 * @author 吴宗德
 *
 */
public class QueryJFBizInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(QueryJFBizInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public QueryJFBizInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY020001Result res = new QRY020001Result();
		
		List<GommonBusiness> retList = new ArrayList<GommonBusiness>();
		res.setGommonBusiness(retList);
		try
		{
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			//个人套餐
			BaseResult bizPackage = this.getBizPackage(accessId, config, params);
			if (LOGIC_SUCESS.equals(bizPackage.getResultCode())) {
				if (bizPackage.getReObj() != null && bizPackage.getReObj() instanceof List) {
					List<GommonBusiness> pkgList = (List<GommonBusiness>)bizPackage.getReObj();
					if (pkgList != null && pkgList.size() > 0) {
						for (GommonBusiness bean:pkgList) {
							retList.add(bean);
						}
					}
				}
				//附加功能
				BaseResult bizService = this.getBizService(accessId, config, params);
				if (LOGIC_SUCESS.equals(bizService.getResultCode())) {
					if (bizService.getReObj() != null && bizService.getReObj() instanceof List) {
						List<GommonBusiness> serviceList = (List<GommonBusiness>)bizService.getReObj();
						if (serviceList != null && serviceList.size() > 0) {
							for (GommonBusiness bean:serviceList) {
								retList.add(bean);
							}
						}
					}
					//增值业务
					BaseResult bizIncrement = this.getBizIncrement(accessId, config, params);
					if (LOGIC_SUCESS.equals(bizIncrement.getResultCode())) {
						if (bizIncrement.getReObj() != null && bizIncrement.getReObj() instanceof List) {
							List<GommonBusiness> incrementList = (List<GommonBusiness>)bizIncrement.getReObj();
							if (incrementList != null && incrementList.size() > 0) {
								for (GommonBusiness bean:incrementList) {
									retList.add(bean);
								}
							}
						}
						//自有业务
						BaseResult bizSelfhood = this.getBizSelfhood(accessId, config, params);
						if (LOGIC_SUCESS.equals(bizSelfhood.getResultCode())) {
							if (bizSelfhood.getReObj() != null && bizSelfhood.getReObj() instanceof List) {
								List<GommonBusiness> selfhoodList = (List<GommonBusiness>)bizSelfhood.getReObj();
								if (selfhoodList != null && selfhoodList.size() > 0) {
									for (GommonBusiness bean:selfhoodList) {
										retList.add(bean);
									}
								}
							}
							//梦网业务
							BaseResult bizMonternet = this.getBizMonternet(accessId, config, params);
							if (LOGIC_SUCESS.equals(bizMonternet.getResultCode())) {
								if (bizMonternet.getReObj() != null && bizMonternet.getReObj() instanceof List) {
									List<GommonBusiness> monternetList = (List<GommonBusiness>)bizMonternet.getReObj();
									if (monternetList != null && monternetList.size() > 0) {
										for (GommonBusiness bean:monternetList) {
											retList.add(bean);
										}
									}
								}
							} else {
								res.setResultCode(LOGIC_ERROR);
								res.setErrorCode(bizMonternet.getErrorCode());
								res.setErrorMessage(bizMonternet.getErrorMessage());
							}
						} else {
							res.setResultCode(LOGIC_ERROR);
							res.setErrorCode(bizSelfhood.getErrorCode());
							res.setErrorMessage(bizSelfhood.getErrorMessage());
						}
					} else {
						res.setResultCode(LOGIC_ERROR);
						res.setErrorCode(bizIncrement.getErrorCode());
						res.setErrorMessage(bizIncrement.getErrorMessage());
					}
				} else {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(bizService.getErrorCode());
					res.setErrorMessage(bizService.getErrorMessage());
				}
			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(bizPackage.getErrorCode());
				res.setErrorMessage(bizPackage.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 查询个人套餐
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getBizPackage(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		List<GommonBusiness> retList = null;
		
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_find_package_62_TC", params);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_find_package_62_TC", super.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				errDt = this.wellFormedDAO.transBossErrCode("QRY020001", "cc_find_package_62_TC", errCode);
				if (null != errDt)
				{
					errCode = errDt.getLiErrCode();
					errDesc = errDt.getLiErrMsg();
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);

				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					List package_code = root.getChild("content").getChildren("package_code");
					if (null != package_code && package_code.size() > 0)
					{
						retList = new ArrayList(package_code.size());
						GommonBusiness bean = null;
						for (int i = 0; i < package_code.size(); i++)
						{
							bean = new GommonBusiness();

							Element cplanpackagedt = ((Element)package_code.get(i)).getChild("cplanpackagedt");

							bean.setId(p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll(""));

							String oTime = p.matcher(cplanpackagedt.getChildText("package_use_date")).replaceAll("");
							long disDay = Long.parseLong(getDistanceDT(getTodayChar14(), oTime, "d"));

							bean.setState(disDay > 0 ? 3 : 2);

							//开始日期
							bean.setBeginDate(oTime);
							//结束日期
							bean.setEndDate(p.matcher(cplanpackagedt.getChildText("package_end_date")).replaceAll(""));

							retList.add(bean);

						}
						
						res.setReObj(retList);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 查询附加功能
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult getBizService(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		List<GommonBusiness> retList = null;
		
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetusersvropt_79", params);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetusersvropt_79", super.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				errDt = this.wellFormedDAO.transBossErrCode("QRY020001", "cc_cgetusersvropt_79", errCode);
				if (null != errDt)
				{
					errCode = errDt.getLiErrCode();
					errDesc = errDt.getLiErrMsg();
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);

				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					List sv_opt_usrid = root.getChild("content").getChildren("sv_opt_usrid");
					if (null != sv_opt_usrid && sv_opt_usrid.size() > 0)
					{
						retList = new ArrayList<GommonBusiness>(sv_opt_usrid.size());
						GommonBusiness bean = null;
						for (int i = 0; i < sv_opt_usrid.size(); i++)
						{
							bean = new GommonBusiness();

							Element cuserserviceoptdt = ((Element)sv_opt_usrid.get(i)).getChild("cuserserviceoptdt");

							bean.setId(p.matcher(cuserserviceoptdt.getChildText("sv_opt_svcode")).replaceAll(""));

//							String state = p.matcher(cuserserviceoptdt.getChildText("sv_opt_state")).replaceAll("");
//							bean.setState(Integer.parseInt(state));
							bean.setState(2);

							//开始日期
							bean.setBeginDate(p.matcher(cuserserviceoptdt.getChildText("sv_opt_applydate")).replaceAll(""));
							//结束日期
							bean.setEndDate(p.matcher(cuserserviceoptdt.getChildText("sv_opt_enddate")).replaceAll(""));

							retList.add(bean);

						}
						res.setReObj(retList);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 查询增值业务
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult getBizIncrement(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		List<GommonBusiness> retList = null;
		
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetincinfo_346", params);
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetincinfo_346", super.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				errDt = this.wellFormedDAO.transBossErrCode("QRY020001", "cc_cgetincinfo_346", errCode);
				if (null != errDt)
				{
					errCode = errDt.getLiErrCode();
					errDesc = errDt.getLiErrMsg();
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);

				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					List spbizList = root.getChild("content").getChildren("smscall_deal_code");
					if (null != spbizList && spbizList.size() > 0)
					{
						retList = new ArrayList<GommonBusiness>(spbizList.size());
						GommonBusiness bean = null;
						for (int i = 0; i < spbizList.size(); i++)
						{
							bean = new GommonBusiness();

							Element csmscalldt = ((Element)spbizList.get(i)).getChild("csmscalldt");

							bean.setId(p.matcher(csmscalldt.getChildText("smscall_deal_code")).replaceAll("").trim());
							String strState = p.matcher(csmscalldt.getChildText("smscall_state")).replaceAll("");
							int state = 0;
							if (null != strState && !"".equals(strState))
							{
								if ("1".equals(strState))
								{
									state = 2;
								}
								else if ("2".equals(strState))
								{
									state = 1;
								}
								else
								{
									state = Integer.parseInt(strState);
								}
							}
							else
							{
								state = 1;
							}
							bean.setState(state);
							bean.setBeginDate(p.matcher(csmscalldt.getChildText("smscall_start_date")).replaceAll(""));
							bean.setEndDate(p.matcher(csmscalldt.getChildText("smscall_end_date")).replaceAll(""));

							retList.add(bean);

						}
						res.setReObj(retList);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 查询自有业务
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult getBizSelfhood(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		List<GommonBusiness> retList = null;
		
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetspuserinfo_358", params);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetspuserinfo_358", super.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				errDt = this.wellFormedDAO.transBossErrCode("QRY020001", "cc_cgetspuserinfo_358", errCode);
				if (null != errDt)
				{
					errCode = errDt.getLiErrCode();
					errDesc = errDt.getLiErrMsg();
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);

				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					List selfplatuserreg_reg_id = root.getChild("content").getChildren("selfplatuserreg_reg_id");
					if (null != selfplatuserreg_reg_id && selfplatuserreg_reg_id.size() > 0)
					{
						retList = new ArrayList<GommonBusiness>(selfplatuserreg_reg_id.size());
						GommonBusiness bean = null;
						for (int i = 0; i < selfplatuserreg_reg_id.size(); i++)
						{
							bean = new GommonBusiness();

							Element cselfplatuserregdt = ((Element)selfplatuserreg_reg_id.get(i)).getChild("cselfplatuserregdt");

							bean.setId(p.matcher(cselfplatuserregdt.getChildText("selfplatuserreg_prd_code")).replaceAll("").trim());
							String strState = p.matcher(cselfplatuserregdt.getChildText("selfplatuserreg_status")).replaceAll("");
							int state = 0;
							if (null != strState && !"".equals(strState))
							{
								if ("1".equals(strState))
								{
									state = 2;
								}
								else if ("0".equals(strState))
								{
									state = 3;
								}
								else
								{
									state = Integer.parseInt(strState);
								}
							}
							else
							{
								state = 1;
							}
							bean.setState(state);
							bean.setBeginDate(p.matcher(cselfplatuserregdt.getChildText("selfplatuserreg_begin_date")).replaceAll(""));
							bean.setEndDate(p.matcher(cselfplatuserregdt.getChildText("selfplatuserreg_end_date")).replaceAll(""));

							retList.add(bean);

						}
						res.setReObj(retList);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 查询梦网业务
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult getBizMonternet(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		List<GommonBusiness> retList = null;
		
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqryspuserreg_65_SJB", params);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cqryspuserreg_65_SJB", super.generateCity(params)));
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				errDt = this.wellFormedDAO.transBossErrCode("QRY020001", "cc_cqryspuserreg_65_SJB", errCode);
				if (null != errDt)
				{
					errCode = errDt.getLiErrCode();
					errDesc = errDt.getLiErrMsg();
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);

				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					List spbizreg_gsm_user_id = root.getChild("content").getChildren("spbizreg_gsm_user_id");
					if (null != spbizreg_gsm_user_id && spbizreg_gsm_user_id.size() > 0)
					{
						retList = new ArrayList<GommonBusiness>(spbizreg_gsm_user_id.size());
						GommonBusiness bean = null;
						for (int i = 0; i < spbizreg_gsm_user_id.size(); i++)
						{
							bean = new GommonBusiness();

							Element cspbizregdt = ((Element)spbizreg_gsm_user_id.get(i)).getChild("cspbizregdt");

							bean.setId(p.matcher(cspbizregdt.getChildText("spbizreg_sub_biz_val")).replaceAll("").trim());
							String strState = p.matcher(cspbizregdt.getChildText("spbizreg_status")).replaceAll("");
							int state = 0;
							if (null != strState && !"".equals(strState))
							{
								if ("0".equals(strState))
								{
									state = 2;
								}
								else
								{
									state = Integer.parseInt(strState);
								}
							}
							else
							{
								state = 1;
							}
							bean.setState(state);
							bean.setBeginDate(p.matcher(cspbizregdt.getChildText("spbizreg_effect_time")).replaceAll(""));

							retList.add(bean);

						}
						res.setReObj(retList);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	public static String getTodayChar14(){
		return DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
	}
	
	/**
	 * 比对两个时间间隔
	 * @param startDateTime 开始时间
	 * @param endDateTime 结束时间
	 * @param distanceType 计算间隔类型 天d 小时h 分钟m 秒s
	 * @return
	 */
    public static String getDistanceDT(String startDateTime,String endDateTime,String distanceType){
        String strResult="";
        long lngDistancVal=0;
        try{
            SimpleDateFormat tempDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date startDate=tempDateFormat.parse(startDateTime);
            Date endDate=tempDateFormat.parse(endDateTime);
            if(distanceType.equals("d")){
            	lngDistancVal = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
            }else if(distanceType.equals("h")){
            	lngDistancVal = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60);
            }else if(distanceType.equals("m")){
            	lngDistancVal = (endDate.getTime() - startDate.getTime()) / (1000 * 60);
            }else if(distanceType.equals("s")){
            	lngDistancVal = (endDate.getTime() - startDate.getTime()) / (1000);
            }
            strResult=String.valueOf(lngDistancVal);
        }catch(Exception e){
        	strResult="0";
        }
        return strResult;
    }
    
    /**
	 * 解析报文
	 * @param tmp
	 * @return
	 */
	public Element getElement(byte[] tmp)
	{
		Element root = null;
		try
		{
			ByteArrayInputStream ins = new ByteArrayInputStream(tmp);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(ins);
			root = doc.getRootElement();
			return root;
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		return root;
	}
	
}