package com.xwtech.xwecp.service.logic.invocation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import com.xwtech.xwecp.service.logic.pojo.DEL010001Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class TransactInterRoamingInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(TransctYdcxInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

//	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;
	/*新加入无线公话无线赏花禁止办理GJTGAMY和GJTGACT业务*/
	private Set<String> wXGHSets;
	
	private Set<String> wXSHSets;

	private String GJTGAMY = "GJTGAMY";
	
	private String GJTGACT = "GJTGACT";
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	//请求获取个人产品信息失败
	private static  final String errorGetProInfo = "-345";
	/*end*/
	public TransactInterRoamingInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		// this.wellFormedDAO = (WellFormedDAO)
		// (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
		wXSHSets = new HashSet<String>();
		wXGHSets = new HashSet<String>();
		wXGHSets.add("1000100001");
		wXGHSets.add("1000100005");
		wXGHSets.add("1000100040");
		wXGHSets.add("100001");
		wXGHSets.add("100005");
		wXGHSets.add("100040");
		
		wXSHSets.add("1000100097");
		wXSHSets.add("1000100098");
		wXSHSets.add("1000100133");
		wXSHSets.add("1000100134");
		wXSHSets.add("1100006660");
		wXSHSets.add("1400100133");
		wXSHSets.add("1400100134");
		wXSHSets.add("100097");
		wXSHSets.add("100098");
		wXSHSets.add("100133");
		wXSHSets.add("100134");
		wXSHSets.add("006660");
		wXSHSets.add("100133");
		wXSHSets.add("100134");
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		DEL010001Result res = new DEL010001Result();
		String id = ""; // 业务编码
		String oprType = ""; // 操作类型
		String beginDate = ""; // 开始日期
		String endDate = ""; // 结束日期
		int vipHGreade = 0; // 大客户级别
		String innettoyears = ""; // 是否在网两年 0：否 1：是
		String svcCode = ""; // 1:台港澳 国际漫游 8: 台港澳 国际长途

		try {
			if (null != params && params.size() > 0) {
				for (RequestParameter p : params) {
					if ("id".equals(p.getParameterName())) // 业务编码
					{
						id = String.valueOf(p.getParameterValue());
						if (null != id && !"".equals(id)) {
							if (GJTGAMY.equals(id)) // 台港澳 国际漫游
							{
								svcCode = "1";
							} else if (GJTGACT.equals(id)) // 台港澳 国际长途
							{
								svcCode = "8";
							}
						}
					}
					else if ("oprType".equals(p.getParameterName())) // 操作类型
					{
						oprType = String.valueOf(p.getParameterValue());
						if (null != oprType && !"".equals(oprType)) {
							if ("1".equals(oprType) || "3".equals(oprType)) // 开通变更
							{
								if(GJTGAMY.equals(id) || GJTGAMY.equals(id))
								{
									if(notOpenGJTGA(res,accessId,config,params))
									{
										return res;
									}
								}
								oprType = "0"; // 转换参数 0
							}
						}
					}
					else if ("beginDate".equals(p.getParameterName())) // 开始日期
					{
						beginDate = String.valueOf(p.getParameterValue());
//						if (null != beginDate && !"".equals(beginDate)) {
//							beginDate = beginDate + "000000"; // 转换开始日期 yyyyMMddHHmmss
//						}
					}
					else if ("endDate".equals(p.getParameterName())) // 结束日期
					{
						endDate = String.valueOf(p.getParameterValue());
//						if (null != endDate && !"".equals(endDate)) {
//							endDate = endDate + "000000"; // 转换结束日期 yyyyMMddHHmmss
//						}
					}
					else if ("reserve1".equals(p.getParameterName())) // 大客户级别
					{
						vipHGreade = Integer.parseInt(String.valueOf(p.getParameterValue()));
					}
					else if ("reserve2".equals(p.getParameterName())) // 是否在网两年 0：否 1：是
					{
						innettoyears = String.valueOf(p.getParameterValue());
					}
				}
			}
			
			//进行账务关系判断
			String requestXml = this.bossTeletextUtil.mergeTeletext("ac_getrelbyuser_608", params);
			String responseXml = (String) this.remote.callRemote(new StringTeletext(requestXml, accessId, "ac_getrelbyuser_608", this.generateCity(params)));
			
			boolean falg = this.isPayForAnother(responseXml);
			
			// 国际功能提交服务(国际长途漫游受理)
			if(falg){
				this.transactInterLongDisOrRoaming(accessId, config, params, res, vipHGreade, innettoyears, beginDate, oprType, svcCode, endDate);
			}
			else{
				res.setErrorCode("集团代付不能开通此业务");
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	/**
	 * 禁止无线公话/商话在电子渠道开通GJTGAMY ---国际及港澳台漫游GJTGACT ---国际及港澳台长途相关功能
	 * 
	 */
	private boolean notOpenGJTGA(DEL010001Result res,String accessId, ServiceConfig config,List<RequestParameter> params)
	{
		try
		{
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetproinfo_345", params);
			logger.debug(" ====== 查询用户产品信息请求报文 ======\n" + reqXml);
			String rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetproinfo_345", this.generateCity(params)));
			logger.debug(" ====== 查询用户产品信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				String resultCode = BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR;
				if(LOGIC_SUCESS.equals(resultCode))
				{
					List userproductinfo_product_info_id = null;
					try {
						userproductinfo_product_info_id = root.getChild("content").getChildren("userproductinfo_product_info_id");
					}
					catch(Exception e)
					{
						userproductinfo_product_info_id = null;
						return false;
					}
					int size =  userproductinfo_product_info_id.size();
					if (null != userproductinfo_product_info_id && size > 0)
					{
						for (int i = 0; i <size; i++)
						{
							Element cuserproductinfodt = ((Element) userproductinfo_product_info_id.get(i)).getChild("cuserproductinfodt");
							if (null != cuserproductinfodt)
							{
								String prodctId = p.matcher(cuserproductinfodt.getChildText("userproductinfo_product_id")).replaceAll("");
								if(wXGHSets.contains(prodctId))
								{
									res.setErrorMessage("您已经办理无线公话业务，不能开通国际及港澳台漫游和国际及港澳台长途功能");
									res.setErrorCode(LOGIC_INFO);
									res.setResultCode(LOGIC_ERROR);
									return true;
								}
								else if(wXSHSets.contains(prodctId))
								{
									res.setErrorMessage("您已经办理无线商话业务，不能开通国际及港澳台漫游和国际及港澳台长途功能");
									res.setErrorCode(LOGIC_INFO);
									res.setResultCode(LOGIC_ERROR);
									return true;
								}
							}
							return false;
						}
					}
				}
				else
				{
					res.setAccessId(accessId);
					res.setErrorMessage(errDesc);
					res.setErrorCode(errorGetProInfo);
					res.setResultCode(resultCode);
					return true;
				}
			}
		}catch(Exception e)
		{
		
			logger.error(e, e);
			return true;
		}
		return true;
	}
	
	/**
	 * 是否他人代付
	 * @param responseXml
	 * @return
	 */
	private boolean isPayForAnother(String rspXml) {
		if (null != rspXml && !"".equals(rspXml))
		{
			Element root = this.config.getElement(rspXml);
			String errCode = root.getChild("response").getChildText("resp_code");
			String errDesc = root.getChild("response").getChildText("resp_desc");
			
			if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
				List acordCount = null;
				try
				{
					acordCount = root.getChild("content").getChildren("arecord_count");
				}
				catch (Exception e)
				{
					acordCount = null;
				}
				if (null != acordCount && acordCount.size() > 0) {
					for (int i = 0; i < acordCount.size(); i++)
					{
						Element accountDt = ((Element)acordCount.get(i)).getChild("caccountingrelationdt");
						if (null != accountDt)
						{
							String isDefault = accountDt.getChildText("acrelation_isdefault");
							String acrelationType = accountDt.getChildText("acrelation_type");
							String acrelationUplimit = accountDt.getChildText("acrelation_uplimit");
							String dbiDefId = accountDt.getChildText("dbi_def_id");
							//处理不同的渠道返回的acrelationType字段格式，网厅类似"-1.000000"、短厅，掌厅类似"-1"。
							if(acrelationUplimit.indexOf(".") >= 0){
								acrelationUplimit = acrelationUplimit == null ? "" : Integer.parseInt(acrelationUplimit.substring(0, acrelationUplimit.lastIndexOf("."))) + "";
							}
							
							if("0".equals(isDefault) && ("1".equals(acrelationType) || "2".equals(acrelationType)) && "-1".equals(acrelationUplimit) && "0".equals(dbiDefId)){
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * 国际功能提交服务(国际长途漫游受理)
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @param vipHGreade
	 *            大客户级别 0 否 1 是
	 * @param innettoyears
	 *            是否在网两年 0：否 1：是
	 * @param beginDate
	 *            开始日期
	 * @param oprType
	 *            状态 1：立即开 2：立即关 0：预约开/关
	 * @param svcCode
	 *            1:台港澳 国际漫游 8: 台港澳 国际长途
	 * @param endDate
	 *            结束日期
	 */
	public void transactInterLongDisOrRoaming(String accessId, ServiceConfig config, List<RequestParameter> params, DEL010001Result res, int vipHGreade, String innettoyears,
			String beginDate, String oprType, String svcCode, String endDate) {
		String reqXml = ""; // 发送报文
		String rspXml = ""; // 接收报文
		String resp_code = ""; // 返回错误编码
		String resp_desc = ""; // 反馈信息
		Element root = null; // 根节点
		RequestParameter par = null; // 参数

		try {
			// 新增参数
			par = new RequestParameter();
			par.setParameterName("list_size"); // 节点数量 固定1
			par.setParameterValue("1");
			params.add(par);
			par = new RequestParameter();
			par.setParameterName("bossproduct_paymodeflag"); // 付费方式标志 1为托收代扣
			par.setParameterValue("1");
			params.add(par);
			par = new RequestParameter();
			par.setParameterName("bossproduct_cardflag"); // 是否为大客户
			par.setParameterValue(this.isLargeCustomers(vipHGreade) ? "1" : "0");
			params.add(par);
			par = new RequestParameter();
			par.setParameterName("bossproduct_innettoyears"); // 是否在网两年 0：否 1：是
			par.setParameterValue(innettoyears);
			params.add(par);
			par = new RequestParameter();
			par.setParameterName("bossproduct_assureinfo"); // 担保信息 填空
			par.setParameterValue("");
			params.add(par);
			par = new RequestParameter();
			par.setParameterName("sv_opt_operating"); // 操作流水 填空
			par.setParameterValue("");
			params.add(par);
			par = new RequestParameter();
			par.setParameterName("sv_opt_chgdate"); // 修改时间
			par.setParameterValue(beginDate);
			params.add(par);
			par = new RequestParameter();
			par.setParameterName("sv_opt_applydate"); // 设置申请时间
			par.setParameterValue(this.config.getTodayChar14());
			params.add(par);
			par = new RequestParameter();
			par.setParameterName("sv_opt_state"); // 状态 1：立即开 2：立即关 0：预约开/关
			par.setParameterValue(oprType);
			params.add(par);
			par = new RequestParameter();
			par.setParameterName("sv_opt_startdate"); // 开始时间
			par.setParameterValue(beginDate);
			params.add(par);
			par = new RequestParameter();
			par.setParameterName("sv_opt_hissrl"); // 历史流水 填空
			par.setParameterValue("");
			params.add(par);
			par = new RequestParameter();
			par.setParameterName("sv_opt_svcode"); // 1:台港澳 国际漫游 8: 台港澳 国际长途
			par.setParameterValue(svcCode);
			params.add(par);
			par = new RequestParameter();
			par.setParameterName("sv_opt_enddate"); // 结束日期
			par.setParameterValue(endDate);
			params.add(par);
			par = new RequestParameter();
			par.setParameterName("list_size"); // 数量 固定 1
			par.setParameterValue("1");
			params.add(par);

			// 组装发送报文
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cupdnation_606", params);
			logger.debug(" ====== 发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				// 发送并接收报文
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cupdnation_606", super.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
			}

			// 解析BOSS报文
			if (null != rspXml && !"".equals(rspXml)) {
				// 解析报文 根节点
				root = this.config.getElement(rspXml);
				// 获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				// 获取错误信息
				resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				
				// 设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code, resp_desc, "cc_cupdnation_606");
			}
		} catch (Exception e) {
			logger.error(e, e);
		}

	}

	/**
	 * 判断是否为大客户
	 * 
	 * @param vipHGreade
	 * @return
	 */
	private boolean isLargeCustomers(int vipHGreade) {
		boolean flag = false;
		try {
			// 大客户字典1、3、5、7、99
			int[] vipGrades = { 1, 3, 5, 7, 99 };
			for (int i = 0; i < vipGrades.length; i++) {
				if (vipGrades[i] == vipHGreade) {
					flag = true;
				}
			}
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 设置结果信息
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @param resp_code
	 */
	public void getErrInfo(String accessId, ServiceConfig config, List<RequestParameter> params, DEL010001Result res, String resp_code, String resp_desc,String xmlName) {
		ErrorMapping errDt = null; // 错误编码解析

		try {
			// 设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code) ? "0" : "1");
			
			// 失败
			if (!"0000".equals(resp_code)) {
				// 解析错误信息
				res.setErrorCode(resp_code); // 设置错误编码
				res.setErrorMessage(resp_desc); // 设置错误信息
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
	
	/**
	 * 把日期转换为yyyyMMdd
	 * @param src
	 * @return
	 */
	private String strFormatDateStr(String src){
		return src.replaceAll("-", "");
	}
}
