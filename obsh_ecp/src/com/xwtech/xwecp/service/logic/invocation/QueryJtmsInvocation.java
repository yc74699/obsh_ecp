package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;

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
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY050010Result;
import com.xwtech.xwecp.service.logic.pojo.TrffInfo;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 交通秘书查询
 * 
 * @author yuantao 2010-01-22
 */
public class QueryJtmsInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(TransctYdcxInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;

	public QueryJtmsInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY050010Result res = new QRY050010Result();

		try {
			// 查询交通秘书套餐
			this.getTrffInfo(accessId, config, params, res);
			if (null != res.getTrffInfo() && res.getTrffInfo().size() > 0) {
				this.changeBusiCode(res);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}

		return res;
	}

	/**
	 * 替换业务编码
	 * 
	 * @param res
	 */
	public void changeBusiCode(QRY050010Result res) {
		TrffInfo infoDt = null;
		List<TrffInfo> list = null;

		try {
			if (null != res.getTrffInfo() && res.getTrffInfo().size() > 0) {
				
				// 根据业务编码获取其子业务信息 交通秘书套餐
				List<BossParmDT> bList = this.wellFormedDAO.getSubBossParmList("JTMSTC");
				
				// 存在子业务
				if (null != bList && bList.size() > 0) {
					list = new ArrayList();
					for (BossParmDT dt : bList) {
						boolean flag = true; 
						
						// 业务标识
						for (TrffInfo info : res.getTrffInfo()) {
							if (dt.getParm1().equals(info.getPrdCode())) {
								flag = false;
								info.setPrdCode(dt.getBusiNum());
								list.add(info);
							}
						}
						
						// 未开通
						if (flag) {
							infoDt = new TrffInfo();
							// 状态 未开通
							infoDt.setStatus("1");
							infoDt.setPrdCode(dt.getBusiNum());
							list.add(infoDt);
						}
					}
				}
			}
			res.setTrffInfo(list);
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	/**
	 * 查询交通秘书套餐
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	@SuppressWarnings("unchecked")
	public void getTrffInfo(String accessId, ServiceConfig config, List<RequestParameter> params, QRY050010Result res) {
		String reqXml = ""; // 发送报文
		String rspXml = ""; // 接收报文
		String resp_code = ""; // 返回错误编码
		TrffInfo dt = null; // 交通秘书套餐信息
		List<TrffInfo> list = new ArrayList<TrffInfo>(); // 套餐列表
		RequestParameter par = null; // 参数
		Element root = null; // 根节点

		try {
			// 新增参数
			par = new RequestParameter();
			par.setParameterName("bossproduct_flag"); // 标记
			par.setParameterValue("2");
			params.add(par);
			par = new RequestParameter();
			par.setParameterName("trafficseccfg_biz_code");
			par.setParameterValue("18");
			params.add(par);

			// 组装发送报文
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgettrffinfo_367", params);
			logger.debug(" ====== 发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				
				// 发送并接收报文
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgettrffinfo_367", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
			}

			// 解析BOSS报文
			if (null != rspXml && !"".equals(rspXml)) {
				
				// 解析报文 根节点
				root = this.config.getElement(rspXml);
				
				// 获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				
				// 设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code, "cc_cgettrffinfo_367");
			}
			
			// 成功
			if (null != resp_code && "0000".equals(resp_code)) {
				
				// 解析报文
				List<Element> srlList = this.config.getContentList(root, "trafficsecuser_operating_srl");
				if (null != srlList && srlList.size() > 0) {
					for (Element srlDt : srlList) {
						Element e = this.config.getElement(srlDt, "ctrafficsecuserdt");
						if (null != e) {
							TrffInfo info = new TrffInfo();
							
							//业务编码
							info.setPrdCode(this.config.getChildText(e, "trafficsecuser_prd_code"));
							
							// 状态 开通
							info.setStatus("2");
							
							// 开始日期
							info.setBeginDate(this.config.getChildText(e, "trafficsecuser_begin_date"));
							
							// 结束日期
							info.setEndDate(this.config.getChildText(e, "trafficsecuser_end_date"));
							
							// 车牌号码
							info.setCarId(this.config.getChildText(e, "trafficsecuser_car_id"));
							
							// 驾驶证号1
							info.setDriver1(this.config.getChildText(e, "trafficsecuser_driver_id_1"));
							
							// 驾驶证号2
							info.setDriver2(this.config.getChildText(e, "trafficsecuser_driver_id_2"));
							
							// 车型 1 大型车 2小型车 6 外籍车
							info.setCarType(this.config.getChildText(e, "trafficsecuser_car_type"));
							
							// 序列号
							info.setSeqId(this.config.getChildText(e, "trafficsecuser_prd_code"));
							
							list.add(info);
						}
					}
				}
			}
			res.setTrffInfo(list);
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
	public void getErrInfo(String accessId, ServiceConfig config, List<RequestParameter> params, QRY050010Result res, String resp_code, String xmlName) {
		ErrorMapping errDt = null; // 错误编码解析

		try {
			// 设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code) ? "0" : "1");

			// 失败
			if (!"0000".equals(resp_code)) {
				// 解析错误信息
				errDt = this.wellFormedDAO.transBossErrCode("QRY050010", xmlName, resp_code);
				if (null != errDt) {
					res.setErrorCode(errDt.getLiErrCode()); // 设置错误编码
					res.setErrorMessage(errDt.getLiErrMsg()); // 设置错误信息
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
}
