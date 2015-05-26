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
import com.xwtech.xwecp.service.logic.pojo.FromTransPlanBean;
import com.xwtech.xwecp.service.logic.pojo.FromTransProductBean;
import com.xwtech.xwecp.service.logic.pojo.FromTransSPBean;
import com.xwtech.xwecp.service.logic.pojo.QRY050042Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * @author 张成东
 * @mail   zhangcd@mail.xwtec.cn
 * 创建于：Jun 22, 2011
 * 描述：携号转网 查询用户原地市可转移增值产品、SP业务、营销案列表
 */
public class GetUsrTransBusiInfoInvocation  extends BaseInvocation implements ILogicalService{

private static final Logger logger = Logger.getLogger(GetUsrTransMainProductInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public GetUsrTransBusiInfoInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	public BaseServiceInvocationResult executeService(String accessId,ServiceConfig config, List<RequestParameter> params) {
		QRY050042Result result = new QRY050042Result();
		try {
			BaseResult proRes = (BaseResult)this.getProList(accessId, config, params);
			BaseResult spRes = (BaseResult)this.getSPList(accessId, config, params);
			BaseResult planRes = (BaseResult)this.getPlanList(accessId, config, params);
			result.setFromTransProductList((List<FromTransProductBean>)proRes.getReObj());
			result.setFromTransSPList((List<FromTransSPBean>)spRes.getReObj());
			result.setFromTransPlanList((List<FromTransPlanBean>)planRes.getReObj());
			

			if(LOGIC_SUCESS.equals(proRes.getResultCode()) && LOGIC_SUCESS.equals(spRes.getResultCode()) && LOGIC_SUCESS.equals(planRes.getResultCode()))
			{
				result.setResultCode(LOGIC_SUCESS);
			}else
			{
				if(!LOGIC_SUCESS.equals(proRes.getResultCode()) )
				{
					result.setResultCode(proRes.getResultCode());
				}else if(!LOGIC_SUCESS.equals(spRes.getResultCode()) )
				{
					result.setResultCode(spRes.getResultCode());
				}else if(!LOGIC_SUCESS.equals(planRes.getResultCode()) )
				{
					result.setResultCode(planRes.getResultCode());
				}
			}
	

		} catch (Exception e) {
			logger.error(e, e);
		}
		return result;
	}
	
	/**
	 * 获取增值产品列表
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	public BaseServiceInvocationResult getProList(String accessId,ServiceConfig config, List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		ArrayList<FromTransProductBean> proList = null;
		
		try {
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetusrtransprocinfo", params);
				
				logger.debug(" ====== 查询用户可转移增值产品信息 请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetusrtransprocinfo", this.generateCity(params)));
				
				logger.debug(" ====== 查询用户可转移增值产品信息 返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY050042", "cc_cgetusrtransprocinfo", errCode);
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
						List webproductopencfg_product_id = null;
						try
						{
							webproductopencfg_product_id = root.getChild("content").getChildren("cusrincproductcfgdt");
						}
						catch (Exception e)
						{
							webproductopencfg_product_id = null;
						}
						if (null != webproductopencfg_product_id && webproductopencfg_product_id.size() > 0) {
							proList = new ArrayList<FromTransProductBean>(webproductopencfg_product_id.size());
							FromTransProductBean bean = null;
							for (int i = 0; i < webproductopencfg_product_id.size(); i++)
							{
								bean = new FromTransProductBean();
								Element cwebproductopencfgdt = ((Element)webproductopencfg_product_id.get(i));
								if (null != cwebproductopencfgdt)
								{
									bean.setProInfoId(p.matcher(cwebproductopencfgdt.getChildText("usrincproductcfg_product_info_id")).replaceAll(""));
				    				bean.setProfoInPkId(p.matcher(cwebproductopencfgdt.getChildText("usrincproductcfg_product_pkgid")).replaceAll(""));
				    				bean.setProId(p.matcher(cwebproductopencfgdt.getChildText("usrincproductcfg_product_id")).replaceAll(""));
				    				bean.setProName(p.matcher(cwebproductopencfgdt.getChildText("usrincproductcfg_product_name")).replaceAll(""));
				    				bean.setStartTime(p.matcher(cwebproductopencfgdt.getChildText("productserviceopt_start_time")).replaceAll(""));
				    				bean.setEndTime(p.matcher(cwebproductopencfgdt.getChildText("productserviceopt_end_time")).replaceAll(""));
				    				bean.setTransflag(p.matcher(cwebproductopencfgdt.getChildText("transflag")).replaceAll(""));
				    				bean.setAttrFlag(p.matcher(cwebproductopencfgdt.getChildText("attrflag")).replaceAll(""));
				    				proList.add(bean);
								}
							}

							res.setReObj(proList);
						}
					}
				}

		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 获取SP业务列表
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	public BaseServiceInvocationResult getSPList(String accessId,ServiceConfig config, List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		ArrayList<FromTransSPBean> proList = null;
		
		try {
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetusertransspinfo", params);
				
				logger.debug(" ====== 查询用户可转移sp业务信息 请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetusertransspinfo", this.generateCity(params)));
				
				logger.debug(" ====== 查询用户可转移sp业务信息 返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY050042", "cc_cgetusertransspinfo", errCode);
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
						List webproductopencfg_product_id = null;
						try
						{
							webproductopencfg_product_id = root.getChild("content").getChildren("cusermoninfodt");
						}
						catch (Exception e)
						{
							webproductopencfg_product_id = null;
						}
						if (null != webproductopencfg_product_id && webproductopencfg_product_id.size() > 0) {
							proList = new ArrayList<FromTransSPBean>(webproductopencfg_product_id.size());
							FromTransSPBean bean = null;
							for (int i = 0; i < webproductopencfg_product_id.size(); i++)
							{
								bean = new FromTransSPBean();
								Element cwebproductopencfgdt = ((Element)webproductopencfg_product_id.get(i));
								if (null != cwebproductopencfgdt)
								{
									bean.setRegId(p.matcher(cwebproductopencfgdt.getChildText("usermoninfo_reg_id")).replaceAll(""));
				    				bean.setSpId(p.matcher(cwebproductopencfgdt.getChildText("usermoninfo_spid")).replaceAll(""));
				    				bean.setSpName(p.matcher(cwebproductopencfgdt.getChildText("usermoninfo_spname")).replaceAll(""));
				    				bean.setSpBizid(p.matcher(cwebproductopencfgdt.getChildText("usermoninfo_spbizid")).replaceAll(""));
				    				bean.setSpBizname(p.matcher(cwebproductopencfgdt.getChildText("usermoninfo_spbizname")).replaceAll(""));
				    				bean.setSpBiztype(p.matcher(cwebproductopencfgdt.getChildText("usermoninfo_spbiztype")).replaceAll(""));
				    				bean.setPrice(p.matcher(cwebproductopencfgdt.getChildText("usermoninfo_price")).replaceAll(""));
				    				bean.setStartTime(p.matcher(cwebproductopencfgdt.getChildText("usermoninfo_start_time")).replaceAll(""));
				    				bean.setEndTime(p.matcher(cwebproductopencfgdt.getChildText("usermoninfo_end_time")).replaceAll(""));
				    				bean.setTransflag(p.matcher(cwebproductopencfgdt.getChildText("transflag")).replaceAll(""));
				    				proList.add(bean);
								}
							}

							res.setReObj(proList);
						}
					}
				}

		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	
	/**
	 * 获取营销方案业务列表
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	public BaseServiceInvocationResult getPlanList(String accessId,ServiceConfig config, List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		ArrayList<FromTransPlanBean> proList = null;
		
		try {
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_mmsdealall", params);
				
				logger.debug(" ====== 查询用户可转移营销方案业务信息 请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_mmsdealall", this.generateCity(params)));
				
				logger.debug(" ====== 查询用户可转移营销方案业务信息 返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY050042", "cc_mmsdealall", errCode);
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
						List webproductopencfg_product_id = null;
						try
						{
							webproductopencfg_product_id = root.getChild("content").getChildren("ccustomsmsnoticedt");
						}
						catch (Exception e)
						{
							webproductopencfg_product_id = null;
						}
						if (null != webproductopencfg_product_id && webproductopencfg_product_id.size() > 0) {
							proList = new ArrayList<FromTransPlanBean>(webproductopencfg_product_id.size());
							FromTransPlanBean bean = null;
							for (int i = 0; i < webproductopencfg_product_id.size(); i++)
							{
								bean = new FromTransPlanBean();
								Element cwebproductopencfgdt = ((Element)webproductopencfg_product_id.get(i));
								if (null != cwebproductopencfgdt)
								{
									bean.setPatchBatchId(p.matcher(cwebproductopencfgdt.getChildText("customsmsnotice_plan_batch_id")).replaceAll(""));
				    				bean.setPatchGradeId(p.matcher(cwebproductopencfgdt.getChildText("customsmsnotice_plan_grade_id")).replaceAll(""));
				    				bean.setPatchGradeName(p.matcher(cwebproductopencfgdt.getChildText("customsmsnotice_plan_grade_name")).replaceAll(""));
				    				bean.setPatchName(p.matcher(cwebproductopencfgdt.getChildText("customsmsnotice_plan_batch_name")).replaceAll(""));
				    				bean.setTransflag(p.matcher(cwebproductopencfgdt.getChildText("customsmsnotice_plan_begin_time")).replaceAll(""));
				    				bean.setStartTime(p.matcher(cwebproductopencfgdt.getChildText("customsmsnotice_plan_end_time")).replaceAll(""));
				    				bean.setEndTime(p.matcher(cwebproductopencfgdt.getChildText("transflag")).replaceAll(""));
				    				proList.add(bean);
								}
							}

							res.setReObj(proList);
						}
					}
				}

		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
}
