package com.xwtech.xwecp.service.logic.invocation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.memcached.MemCachedKey;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.OperDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY040004Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class QueryOperDetailInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(QueryOperDetailInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;

	public QueryOperDetailInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		String beginDate = "";
		String endDate = "";
		String ddr_city = "";

		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				RequestParameter pTmp = params.get(i);
				if ("beginDate".equals(pTmp.getParameterName())) {
					beginDate = (String) pTmp.getParameterValue();
				}
				if ("endDate".equals(pTmp.getParameterName())) {
					endDate = (String) pTmp.getParameterValue();
				}
				if ("context_ddr_city".equals(pTmp.getParameterName())) {
					ddr_city = (String) pTmp.getParameterValue();
				}
			}
		}

//		Calendar cal = Calendar.getInstance();
//		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
//		String ed = sf.format(cal.getTime());
//		cal.add(Calendar.MONTH, -3);
//		String bd = sf.format(cal.getTime());
//
		params.add(new RequestParameter("bd", beginDate));
		params.add(new RequestParameter("ed", endDate));

		QRY040004Result res = new QRY040004Result();
		// 办理信息
		List<OperDetail> operDetailList = null;

		// 办理转换信息
		List<OperatorDetailExt> operatorDetailExtList = null;

		// 屏蔽信息
		List<Map> filterInfoList = null;

		// 办理信息
		List<Map> functionInfoList = null;

		// 渠道信息
		List<Map> channelInfoList = null;

		try {

			operatorDetailExtList = queryHistoryInfo(accessId, config, params, new StringBuffer(), res);

			String key1 = "OPERATOR_HISTORY_FILTER_" + ddr_city;
			Object obj1 = wellFormedDAO.getCache().get(key1);
			if (obj1 != null && obj1 instanceof List) {
				filterInfoList = (List<Map>) obj1;
				logger.debug("读取缓存---->>>>>>>>>>>>屏蔽信息！！！条数： " + filterInfoList.size());
			} else {
				List<RequestParameter> paramNew1 = copyParam(params);
				paramNew1.add(new RequestParameter("dict_type", "827"));
				filterInfoList = queryOprChannel(accessId, paramNew1, res);
				wellFormedDAO.getCache().add(key1, filterInfoList, MemCachedKey.KEY_EXPIREINSECOND);
			}
			Map filterMap = getDictMapFromList(filterInfoList);

			String key2 = "OPERATOR_HISTORY_CHANNEL_" + ddr_city;
			Object obj2 = wellFormedDAO.getCache().get(key2);
			if (obj2 != null && obj2 instanceof List) {
				channelInfoList = (List<Map>) obj2;
				logger.debug("读取缓存---->>>>>>>>>>>>渠道信息！！！条数： " + channelInfoList.size());
			} else {
				List<RequestParameter> paramNew2 = copyParam(params);
				paramNew2.add(new RequestParameter("dict_type", "10"));

				channelInfoList = queryOprChannel(accessId, paramNew2, res);
				wellFormedDAO.getCache().add(key2, channelInfoList, MemCachedKey.KEY_EXPIREINSECOND);
			}
			Map channelMap = getDictMapFromList(channelInfoList);

			String key3 = "OPERATOR_HISTORY_FUNCTION_" + ddr_city;
			Object obj3 = wellFormedDAO.getCache().get(key3);
			if (obj3 != null && obj3 instanceof List) {
				functionInfoList = (List<Map>) obj3;
				logger.debug("读取缓存---->>>>>>>>>>>>办理信息！！！条数： " + functionInfoList.size());
			} else {
				List<RequestParameter> paramNew3 = copyParam(params);
				paramNew3.add(new RequestParameter("dict_type", "8"));
				functionInfoList = queryOprChannel(accessId, paramNew3, res);
				wellFormedDAO.getCache().add(key3, functionInfoList, MemCachedKey.KEY_EXPIREINSECOND);
			}
			Map functionMap = getDictMapFromList(functionInfoList);

			// 组合操作 1.过滤办理历史信息 2.将functionId转换为办理业务名称 3.办理历史信息拼接办理渠道信息
			if (operatorDetailExtList != null && operatorDetailExtList.size() > 0) {
				Iterator it = operatorDetailExtList.iterator();
				operDetailList = new ArrayList<OperDetail>(operatorDetailExtList.size());
				while (it.hasNext()) {
					OperatorDetailExt operatorDetailExt = (OperatorDetailExt) it.next();
					if (filterMap != null && filterMap.get(operatorDetailExt.getFunctionId()) != null) {
						it.remove();
					} else {
						OperDetail operDetail = new OperDetail();
						if (functionMap != null && functionMap.get(operatorDetailExt.getFunctionId()) != null) {
							operatorDetailExt.setOprBiz((String) functionMap.get(operatorDetailExt.getFunctionId()));
						} else {
							operatorDetailExt.setOprBiz(operatorDetailExt.getRemark());
						}
						if (channelMap != null && channelMap.get(operatorDetailExt.getOprChannel()) != null) {
							operatorDetailExt.setOprChannel((String) channelMap.get(operatorDetailExt.getOprChannel()));
						}
						operDetail.setFormNum(operatorDetailExt.getFormNum());
						operDetail.setOprTime(operatorDetailExt.getOprTime());
						operDetail.setOprBiz(operatorDetailExt.getOprBiz());
						operDetail.setOprChannel(operatorDetailExt.getOprChannel());
						operDetailList.add(operDetail);
					}
				}
				
				Iterator iter = operDetailList.iterator();
				while (iter.hasNext()) {
					OperDetail operDetail = (OperDetail) iter.next();
					String remark = operDetail.getOprBiz();
					if ("综合查询".equalsIgnoreCase(remark) || "历史资料查询".equalsIgnoreCase(remark) || "按帐单查询梦网清单".equalsIgnoreCase(remark) || "精确营销接口调用".equalsIgnoreCase(remark)
							|| remark.contains("查询")) {
						iter.remove();
					}
				}

				Iterator it1 = operDetailList.iterator();
				while (it1.hasNext()) {
					OperDetail operDetail = (OperDetail) it1.next();
					String ss = operDetail.getOprTime().substring(0, 8);
					if (ss.compareTo(beginDate) < 0) {
						it1.remove();
					}
				}

				// 排序
				new SortDetail().srotDetailList(operDetailList, false);
				res.setOperDetail(operDetailList);
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 查询历史记录
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public List<OperatorDetailExt> queryHistoryInfo(String accessId, ServiceConfig config, List<RequestParameter> params, StringBuffer str, QRY040004Result res) {
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		String resp_desc = "";
		List<OperatorDetailExt> oprInfoList = null;
		OperatorDetailExt operatorDetailExt = null;
		ErrorMapping errDt = null;

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cquerydetail_309", params);
			logger.debug(" ====== 查询历史记录 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cquerydetail_309", super.generateCity(params)));
				logger.debug(" ====== 查询历史记录 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				errDt = this.wellFormedDAO.transBossErrCode("QRY040004", "cc_cquerydetail_309", resp_code);
				if (null != errDt) {
					resp_code = errDt.getLiErrCode();
					resp_desc = errDt.getLiErrMsg();
				}

				String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
				res.setResultCode(resultCode);
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);

			}
			if (null != resp_code && "0000".equals(resp_code)) {
				// 前台流水
				List dealCountList = this.config.getContentList(root, "deal_operating_srl");
				// 后台流水
				List modCountList = this.config.getContentList(root, "mod_operating_srl");

				int dealNum = 0;
				int modNum = 0;
				if (dealCountList != null && dealCountList.size() > 0) {
					dealNum = dealCountList.size();
				}
				if (modCountList != null && modCountList.size() > 0) {
					modNum = modCountList.size();
				}

				oprInfoList = new ArrayList<OperatorDetailExt>(dealNum + modNum);

				if (null != dealCountList && dealCountList.size() > 0) {
					for (int i = 0; i < dealCountList.size(); i++) {
						Element perDetailInfo = this.config.getElement((Element) dealCountList.get(i), "cdealoperatingdetaildt");
						operatorDetailExt = new OperatorDetailExt();
						operatorDetailExt.setOprTime(this.config.getChildText(perDetailInfo, "deal_operating_date"));
						operatorDetailExt.setFunctionId(this.config.getChildText(perDetailInfo, "deal_function_id"));
						//2011-02-15 修改前台流水渠道编号问题
						operatorDetailExt.setOprChannel(this.config.getChildText(perDetailInfo, "user_operating_source"));
						operatorDetailExt.setRemark(this.config.getChildText(perDetailInfo, "deal_operating_remark"));
						operatorDetailExt.setFormNum(this.config.getChildText(perDetailInfo, "deal_operating_srl"));
						oprInfoList.add(operatorDetailExt);
					}
				}

				if (null != modCountList && modCountList.size() > 0) {
					for (int i = 0; i < modCountList.size(); i++) {
						Element perDetailInfo = this.config.getElement((Element) modCountList.get(i), "cmaintenanceoperatingdetailnewdt");
						operatorDetailExt = new OperatorDetailExt();
						operatorDetailExt.setOprTime(this.config.getChildText(perDetailInfo, "mod_operating_date"));
						operatorDetailExt.setFunctionId(this.config.getChildText(perDetailInfo, "mod_function_id"));
						operatorDetailExt.setOprChannel(this.config.getChildText(perDetailInfo, "mod_operating_source"));
						operatorDetailExt.setRemark(this.config.getChildText(perDetailInfo, "mod_operating_remark"));
						operatorDetailExt.setFormNum(this.config.getChildText(perDetailInfo, "mod_operating_srl"));
						oprInfoList.add(operatorDetailExt);
					}
				}

			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return oprInfoList;
	}

	/**
	 * 查询办理渠道/过滤信息/操作信息
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public List<Map> queryOprChannel(String accessId, List<RequestParameter> params, QRY040004Result res) {
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		String resp_desc = "";
		List<Map> channelInfoList = null;
		// ChannelInfo channelInfo = null;
		ErrorMapping errDt = null;

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_get_dictlist_78", params);
			logger.debug(" ====== 查询办理渠道/过滤信息/操作信息 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_get_dictlist_78", super.generateCity(params)));
				logger.debug(" ====== 查询办理渠道/过滤信息/操作信息 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
				errDt = this.wellFormedDAO.transBossErrCode("QRY040004", "cc_get_dictlist_78", resp_code);
				if (null != errDt) {
					resp_code = errDt.getLiErrCode();
					resp_desc = errDt.getLiErrMsg();
				}

				String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
				res.setResultCode(resultCode);
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				List countList = this.config.getContentList(root, "list_size");
				if (null != countList && countList.size() > 0) {
					channelInfoList = new ArrayList<Map>(countList.size());
					for (int i = 0; i < countList.size(); i++) {
						// channelInfo = new ChannelInfo();
						//						
						// channelInfo.setChannelId(this.config.getChildText((Element)countList.get(i), "dict_code"));
						// channelInfo.setChannelName(this.config.getChildText((Element)countList.get(i), "dict_code_desc"));
						// channelInfoList.add(channelInfo);

						Map map = new HashMap(2);

						map.put("code", this.config.getChildText((Element) countList.get(i), "dict_code"));
						map.put("desc", this.config.getChildText((Element) countList.get(i), "dict_code_desc"));

						channelInfoList.add(map);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return channelInfoList;
	}

	/**
	 * @desc 将字典查询出的list转换成以字典Code为key的map
	 * @return
	 */
	private Map<String, String> getDictMapFromList(List<Map> list) {
		Map<String, String> map = new HashMap<String, String>();
		if (list != null && list.size() > 0) {
			int size = list.size();
			Map channelInfo = null;
			for (int i = 0; i < size; i++) {
				channelInfo = list.get(i);
				map.put((String) channelInfo.get("code"), (String) channelInfo.get("desc"));
			}
		}
		return map;
	}

	/**
	 * 把查询出来的受理历史按时间排序
	 * 
	 * @param detailList
	 *            受理历史列表
	 * @param isAsc
	 *            是否升序
	 * @return
	 */
	private class SortDetail implements Comparator {

		private boolean isAsc = false;

		public void srotDetailList(List<OperDetail> detailList, boolean isAsc) {
			this.isAsc = isAsc;
			if (detailList != null && detailList.size() > 0) {
				Collections.sort(detailList, this);
			}
		}

		public int compare(Object o1, Object o2) {
			int result = 0;
			String date1 = ((OperDetail) o1).getOprTime();
			String date2 = ((OperDetail) o2).getOprTime();
			if (date1 != null && date2 == null) {
				result = 1;
			} else if (date1 == null && date2 != null) {
				result = -1;
			} else if (date1 != null && date2 != null) {
				result = date1.compareTo(date2);
			}

			result = isAsc ? result : result * -1;

			return result;
		}

	}

	public class ChannelInfo {

		private String channelId;

		private String channelName;

		public String getChannelId() {
			return channelId;
		}

		public void setChannelId(String channelId) {
			this.channelId = channelId;
		}

		public String getChannelName() {
			return channelName;
		}

		public void setChannelName(String channelName) {
			this.channelName = channelName;
		}
	}

	class OperatorDetailExt {

		private String oprTime;

		private String oprBiz;

		private String oprChannel;

		private String functionId;

		private String remark;

		private String formNum;
		
		public String getFormNum() {
			return formNum;
		}

		public void setFormNum(String formNum) {
			this.formNum = formNum;
		}

		public String getOprTime() {
			return oprTime;
		}

		public void setOprTime(String oprTime) {
			this.oprTime = oprTime;
		}

		public String getOprBiz() {
			return oprBiz;
		}

		public void setOprBiz(String oprBiz) {
			this.oprBiz = oprBiz;
		}

		public String getOprChannel() {
			return oprChannel;
		}

		public void setOprChannel(String oprChannel) {
			this.oprChannel = oprChannel;
		}

		public String getFunctionId() {
			return functionId;
		}

		public void setFunctionId(String functionId) {
			this.functionId = functionId;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

	}

}
