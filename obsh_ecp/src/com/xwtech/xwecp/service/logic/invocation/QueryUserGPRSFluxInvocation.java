package com.xwtech.xwecp.service.logic.invocation;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY040041Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class QueryUserGPRSFluxInvocation extends BaseInvocation implements
		ILogicalService {

	private static final Logger logger = Logger
			.getLogger(QueryUserGPRSFluxInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;

	public QueryUserGPRSFluxInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx
				.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx
				.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {

		QRY040041Result res = new QRY040041Result();

		String date = (String) getParameters(params, "date");

		String idType = (String) getParameters(params, "idType");

		try {

			Calendar c = Calendar.getInstance();
			int currentMonth = c.get(Calendar.MONTH) + 1;// 当前系统月份
			int minMonth = currentMonth - 4;// 系统可查询的最小月份
			int inputMonth = Integer.parseInt(date.substring(4, 6));// 输入月份
			
			if (inputMonth >= minMonth) {
				if (date.length() == 6) {
					RequestParameter requestPar = new RequestParameter(
							"dayNumber", "31");
					params.add(requestPar);

					// 使用情况(已经使用的流量/时长)
					String gprsFlux = queryGPRSDetail(accessId, params, res);
					if (idType.equals("0")) {
						double flux = Double.parseDouble((String) gprsFlux);
						java.text.DecimalFormat df = new java.text.DecimalFormat(
								"#0.00");
						gprsFlux = df.format(flux / 1024 / 1024);
						res.setTotalFee(gprsFlux);
					}
				} else if (date.length() == 8) {
					int inputDay = Integer.parseInt(date.substring(6, 8));// 输入日
					
					RequestParameter requestPar = new RequestParameter(
							"dayNumber", String.valueOf(inputDay));
					params.add(requestPar);

					// 使用情况(已经使用的流量/时长)
					int gprsFlux =Integer.parseInt(queryGPRSDetail(accessId, params, res));
										
					if (idType.equals("0")) {
						double flux = Double.parseDouble((String.valueOf(gprsFlux)));
						java.text.DecimalFormat df = new java.text.DecimalFormat(
								"#0.00");
						res.setTotalFee(df.format(flux / 1024 / 1024));
					}
				} else {
					res.setResultCode("1");
					res.setErrorMessage("请输入正确的参数格式：YYYYMM 或 YYYYMMDD ");
				}
			} else {
				res.setResultCode("1");
				res.setErrorMessage("仅支持 本月和前四个月（"+currentMonth+"月到"+(currentMonth-4)+"月）的GPRS流量查询");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * GPRS流量查询
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 */
	public String queryGPRSDetail(String accessId,
			List<RequestParameter> params, QRY040041Result res) {
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		String gprsFlux = "0";
		String errCode = "";
		ErrorMapping errDt = null;

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetgprsflux_717",
					params);
			logger.debug(" ====== GPRS流量查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(
						reqXml, accessId, "ac_agetgprsflux_717", super
								.generateCity(params)));
				logger.debug(" ====== GPRS流量查询 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(
						root, "response"), "resp_code");
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				errCode = this.config.getChildText(this.config.getElement(root,
						"content"), "error_code");
				gprsFlux = this.config.getChildText(this.config.getElement(
						root, "content"), "gprsbill_total_fee");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				res.setErrorCode(errCode);
				return gprsFlux;
			} else {
				String resp_desc = this.config.getChildText(this.config
						.getElement(root, "response"), "resp_desc");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return gprsFlux;
	}
}
