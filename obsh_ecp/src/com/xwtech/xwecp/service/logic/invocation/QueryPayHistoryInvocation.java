package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.PayHistory;
import com.xwtech.xwecp.service.logic.pojo.QRY010008Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

public class QueryPayHistoryInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryPayHistoryInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;


	public QueryPayHistoryInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY010008Result re = new QRY010008Result();
		String rspXml = "";
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		List<PayHistory> reList = new ArrayList();
		PayHistory pDt = null;
		String money = "";
		String channalId = "";
		ErrorMapping errDt = null;

	try {
			// 发送并接收报文 账本收支流水查询
			rspXml = (String) this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext("ac_agetacctbkseqjk_515", params), accessId, "ac_agetacctbkseqjk_515", super.generateCity(params)));
			logger.info(" ====== 账本收支流水查询 rspXml ====== \n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String reCode = root.getChild("response").getChildText("resp_code");
				re.setResultCode("0000".equals(reCode) ? "0" : "1");
				if (!"0000".equals(reCode)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY010008", "ac_agetacctbkseqjk_515", reCode);
					if (null != errDt) {
						re.setErrorCode(errDt.getLiErrCode());
						re.setErrorMessage(errDt.getLiErrMsg());
					}
				}
                //此处值为1的是支出记录，不需要显示给客户，请进行屏蔽。
				String paymentType="";
				//此处不为0000不显示，1为撤销
				String acctbkpayseq_status = "";
				if (null != reCode && "0000".equals(reCode)) {
					Map m= this.wellFormedDAO.getChannelPay();
					List arecord_count = root.getChild("content").getChildren("arecord_count");
					if (null != arecord_count && arecord_count.size() > 0) {
						for (int i = 0; i < arecord_count.size(); i++) {
							Element acctbkpayseq_dt = ((Element) arecord_count.get(i)).getChild("acctbkpayseq_dt");
							if (null != acctbkpayseq_dt) {
								// 可以显示的充值渠道
								if (null != acctbkpayseq_dt) {
									paymentType = acctbkpayseq_dt.getChildText("acctbkpayseq_payment_type");
									acctbkpayseq_status = acctbkpayseq_dt.getChildText("acctbkpayseq_status");
									if(null!=paymentType && !"1".equals(paymentType) 
											&& ("0".equals(acctbkpayseq_status)|| "0000".equals(acctbkpayseq_status)))
									{
										pDt = new PayHistory(); 
										// 缴费时间
										pDt.setPayTime(p.matcher(acctbkpayseq_dt.getChildText("acctbkpayseq_payment_time")).replaceAll(""));
										money = p.matcher(acctbkpayseq_dt.getChildText("acctbkpayseq_amount")).replaceAll("");
										channalId = p.matcher(acctbkpayseq_dt.getChildText("acctbkpayseq_channel_id")).replaceAll("");
										if (null != money && !"".equals(money)) {
											money = String.valueOf((int) Double.parseDouble(money));
										}
 
										// 缴费金额
										pDt.setPayMoney(money);
										// 状态 无
										pDt.setState("");
										// 付费方式 0：预付费 1：后付费
										pDt.setPayType(0);
										// 帐单月份 无
										pDt.setAccountMonth("");
										//缴费方式
										
										if(null != m && null != m.get(channalId)){
										pDt.setPayMode(m.get(channalId).toString());
										reList.add(pDt);
										}
									  }
									}
								}
							}
						}
					}
				}
			
			//按照日期降序
			Collections.sort(reList, new Comparator<PayHistory>() {
				public int compare(PayHistory o1, PayHistory o2) {
					return Integer.valueOf(o2.getPayTime().substring(0, 8)) - Integer.valueOf(o1.getPayTime().substring(0, 8));
				}
			});
			re.setPayHistory(reList);	
		
		} catch (Exception e) {
			logger.error(e, e);
		}

		return re;
	}
}
