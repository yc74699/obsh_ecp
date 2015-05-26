package com.xwtech.xwecp.service.logic.invocation;

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
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY010034Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
/**
 * 缴费充值大于100记录查询
 * @author 
 *
 */
public class QueryPayListInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryPayListInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;


	public QueryPayListInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
	}
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {

		QRY010034Result re = new QRY010034Result();
		RequestParameter requestParam = this.getParameter(params,"typeMoney");
		Double typeMoney=Double.parseDouble(String.valueOf(requestParam.getParameterValue()));
		typeMoney=typeMoney*10;
		String rspXml = "";
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		String channalId = "";
		ErrorMapping errDt = null;
		String money="0.0";

		try
		{
			// 发送并接收报文 账本收支流水查询
			rspXml = (String) this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext("ac_agetacctbkseqjk_515", params), accessId, "ac_agetacctbkseqjk_515", super.generateCity(params)));
			logger.info(" ====== 账本收支流水查询 rspXml ====== \n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String reCode = root.getChild("response").getChildText("resp_code");
				re.setResultCode("0000".equals(reCode) ? "0" : "1");
				if (!"0000".equals(reCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010034", "ac_agetacctbkseqjk_515", reCode);
					if (null != errDt)
					{
						re.setErrorCode(errDt.getLiErrCode());
						re.setErrorMessage(errDt.getLiErrMsg());
					}
				}
                //此处值为1的是支出记录，不需要显示给客户，请进行屏蔽。
				String paymentType="";
				//此处不为0000不显示，1为撤销
				String acctbkpayseq_status = "";
				if (null != reCode && "0000".equals(reCode))
				{
					Map m= this.wellFormedDAO.getChannelPay();
					List arecord_count = root.getChild("content").getChildren("arecord_count");
					if (null != arecord_count && arecord_count.size() > 0)
					{
//						Map<String,String> mapResult =new HashMap<String,String>();
						for (int i = 0; i < arecord_count.size(); i++)
						{
							Element acctbkpayseq_dt = ((Element) arecord_count.get(i)).getChild("acctbkpayseq_dt");
								// 可以显示的充值渠道
								if (null != acctbkpayseq_dt)
								{
									paymentType = acctbkpayseq_dt.getChildText("acctbkpayseq_payment_type");
									acctbkpayseq_status = acctbkpayseq_dt.getChildText("acctbkpayseq_status");
									if(null!=paymentType && !"1".equals(paymentType) 
											&& ("0".equals(acctbkpayseq_status)|| "0000".equals(acctbkpayseq_status)))
									{
										// 缴费时间
										channalId = p.matcher(acctbkpayseq_dt.getChildText("acctbkpayseq_channel_id")).replaceAll("");
										if(!"148".equals(channalId)&&!"149".equals(channalId)&&!"150".equals(channalId)&&!"151".equals(channalId)&&!"152".equals(channalId)&&!"153".equals(channalId)&&!"154".equals(channalId))
										{
											money = p.matcher(acctbkpayseq_dt.getChildText("acctbkpayseq_amount")).replaceAll("");
											if (null != money && !"".equals(money))
											{
												money = String.valueOf((int) Double.parseDouble(money));
											}
										}
										if(Double.parseDouble(money)>=typeMoney)
										{
										// 缴费金额大于100 跳出循环
											re.setTypeReuslt("1");
											break;
											
										}
										else
										{
											re.setTypeReuslt("0");
										}
									  }
									}
								}
							}
						}
					}
		} catch (Exception e) {
			logger.error(e, e);
		}

		return re;
	}
}
