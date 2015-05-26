package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.OperDetail;
import com.xwtech.xwecp.service.logic.pojo.OperLogDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY040033Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class QuerydetailByReceptionInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(QuerydetailByReceptionInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;

	public QuerydetailByReceptionInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		
		QRY040033Result res = new QRY040033Result();
		//信息
		List<OperLogDetail> operatorDetailExtList = null;

		
		try {

			operatorDetailExtList = queryOperLogDetail(accessId, config, params, new StringBuffer(), res);
			if(null!=operatorDetailExtList){
				res.setOperLogDetail(operatorDetailExtList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 查询历史记录详细信息
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public List<OperLogDetail> queryOperLogDetail(String accessId, ServiceConfig config, List<RequestParameter> params, StringBuffer str, QRY040033Result res) {
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		String resp_desc = "";
		List<OperLogDetail> oprInfoList = null;
		OperLogDetail operatorDetailExt = null;
		ErrorMapping errDt = null;

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cquerydetailByReception_33", params);
			logger.debug(" ====== 查询历史记录 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cquerydetailByReception_33", super.generateCity(params)));
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

				oprInfoList = new ArrayList<OperLogDetail>(dealNum + modNum);

				if (null != dealCountList && dealCountList.size() > 0) {
					for (int i = 0; i < dealCountList.size(); i++) {
						Element perDetailInfo = this.config.getElement((Element) dealCountList.get(i), "cdealoperatingdetaildt");
						operatorDetailExt = new OperLogDetail();
						operatorDetailExt.setOprId(this.config.getChildText(perDetailInfo, "spid"));
						operatorDetailExt.setOprCode(this.config.getChildText(perDetailInfo, "spzCode"));
						operatorDetailExt.setOprName(this.config.getChildText(perDetailInfo, "spzName"));
						operatorDetailExt.setBeginTime(this.config.getChildText(perDetailInfo, "beginTime"));
						operatorDetailExt.setEndTime(this.config.getChildText(perDetailInfo, "endTime"));
						operatorDetailExt.setModifiMark(this.config.getChildText(perDetailInfo, "modifiMark"));
						
						oprInfoList.add(operatorDetailExt);
					}
				}

				if (null != modCountList && modCountList.size() > 0) {
					for (int i = 0; i < modCountList.size(); i++) {
						Element perDetailInfo = this.config.getElement((Element) modCountList.get(i), "cmaintenanceoperatingdetailnewdt");
						operatorDetailExt = new OperLogDetail();
						operatorDetailExt.setOprId(this.config.getChildText(perDetailInfo, "prodid"));
						operatorDetailExt.setOprName(this.config.getChildText(perDetailInfo, "prodidname"));
						operatorDetailExt.setBeginTime(this.config.getChildText(perDetailInfo, "beginTime"));
						operatorDetailExt.setEndTime(this.config.getChildText(perDetailInfo, "endTime"));
						operatorDetailExt.setModifiMark(this.config.getChildText(perDetailInfo, "modifiMark"));
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
	public List<Map> queryOprChannel(String accessId, List<RequestParameter> params, QRY040033Result res) {
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
		private String oprId;
		
		private String oprCode;
		
		private String oprName;
		
		private String beginTime;
		
		private String endTime;
		
		private String oprType;
		
		private String remark;
		
		public String getOprId() {
			return oprId;
		}
		public void setOprId(String oprId) {
			this.oprId = oprId;
		}
		public String getOprCode() {
			return oprCode;
		}
		public void setOprCode(String oprCode) {
			this.oprCode = oprCode;
		}
		public String getOprName() {
			return oprName;
		}
		public void setOprName(String oprName) {
			this.oprName = oprName;
		}
		public String getBeginTime() {
			return beginTime;
		}
		public void setBeginTime(String beginTime) {
			this.beginTime = beginTime;
		}
		public String getEndTime() {
			return endTime;
		}
		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}
		public String getOprType() {
			return oprType;
		}
		public void setOprType(String oprType) {
			this.oprType = oprType;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		

	}

}
