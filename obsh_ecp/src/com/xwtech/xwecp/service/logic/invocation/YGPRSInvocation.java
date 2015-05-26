package com.xwtech.xwecp.service.logic.invocation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
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

public class YGPRSInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(TransctYdcxInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;
	/*end*/
	public YGPRSInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		DEL010001Result res = new DEL010001Result();
		String id = ""; // 业务编码
		String oprType = ""; // 操作类型
		String beginDate = ""; // 开始日期
		String endDate = ""; // 结束日期
		int vipHGreade = 0; // 大客户级别
		String innettoyears = ""; // 是否在网两年 0：否 1：是
		String svcCode = ""; //2400000300预约开通GPRS

		try {
			if (null != params && params.size() > 0) {
				for (RequestParameter p : params) {
					if ("id".equals(p.getParameterName())) // 业务编码
						{
							id = String.valueOf(p.getParameterValue());
							if (null != id && !"".equals(id)) {
									svcCode = "2400000300";
						}
					}else if ("oprType".equals(p.getParameterName())) // 操作类型
					{
						oprType = String.valueOf(p.getParameterValue());
						if (null != oprType && !"".equals(oprType)) {
							if ("1".equals(oprType) || "3".equals(oprType)) // 开通变更
							{
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
			
			// 国际功能提交服务(国际长途漫游受理)
				this.yGprsOpen(accessId, config, params, res, vipHGreade, innettoyears, beginDate, oprType, svcCode, endDate);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * GPRS预约开通
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
	 *            2400000300
	 * @param endDate
	 *            结束日期
	 */
	public void yGprsOpen(String accessId, ServiceConfig config, List<RequestParameter> params, DEL010001Result res, int vipHGreade, String innettoyears,
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
				
				Document doc = DocumentHelper.parseText(rspXml);
				List<Node> responseCode = doc.selectNodes("/operation_out/response");
				if(null != responseCode && responseCode.size() > 0){
					for(int i = 0;i < responseCode.size();i++){
						Node nodeCode=responseCode.get(0);
						  resp_code = nodeCode.selectSingleNode("resp_code").getText().trim();
						  resp_desc = nodeCode.selectSingleNode("resp_desc").getText().trim();
						  String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
							if(null != rspXml && !"".equals(rspXml))
							{
								if(!LOGIC_SUCESS.equals(resultCode))
								{
									res.setResultCode(resultCode);
									res.setErrorCode(resp_code);
									res.setErrorMessage(resp_desc);
								}
								else
								{
									res.setResultCode(resultCode);
									res.setErrorCode(resp_code);
									res.setErrorMessage(resp_desc);
								}
					}
					}
				}
//				// 解析报文 根节点
//				root = this.config.getElement(rspXml.trim());
//				// 获取错误编码
//				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
//				// 获取错误信息
//				resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
//				
//				// 设置结果信息
//				this.getErrInfo(accessId, config, params, res, resp_code, resp_desc, "cc_cupdnation_606");
			}
		} catch (Exception e) {
			logger.error(e, e);
		}

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
	 * 把日期转换为yyyyMMdd
	 * @param src
	 * @return
	 */
	private String strFormatDateStr(String src){
		return src.replaceAll("-", "");
	}
}
