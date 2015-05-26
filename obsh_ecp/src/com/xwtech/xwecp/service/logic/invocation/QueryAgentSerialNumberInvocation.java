package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.xwtech.xwecp.service.logic.pojo.AgentSerialNumber;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY040017Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.TeletextParseUtils;

/**
 * 代理商流水查询
 * 
 * @author wuzd
 * 
 */
public class QueryAgentSerialNumberInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(QueryOperDetailInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	public QueryAgentSerialNumberInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config,
			List<RequestParameter> params) {

		QRY040017Result res = new QRY040017Result();
		String reqXml = "";
		String rspXml = "";
		List<AgentSerialNumber> agentSerialNumberList = null;
		AgentSerialNumber agent = null;
		ErrorMapping errDt = null;

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetagentseq_532", params);
			logger.debug(" ====== 查询 代理商流水 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_agetagentseq_532",
						this.generateCity(params)));
				logger.debug(" ====== 查询 代理商流水 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY040017", "ac_agetagentseq_532", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);

				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					String seqContent = root.getChild("content").getChildText("XTABLE_AGENT_SEQ");
					
					String city = (String) this.getParameters(params, "context_ddr_city");
					String phoneNum = (String) this.getParameters(params, "phoneNum");
					
					List<Map<String, String>> seqList = null;
					//华为的格式分解
					//if(XWECPApp.NEED_CUT_CITYS.indexOf(city) != -1){
						seqList = TeletextParseUtils.parseXTABLE_HW(seqContent);
					//}
					//新大陆的格式分解
//					else{
//						seqList = TeletextParseUtils.parseXTABLE(seqContent);
//					}
//					
					
					if (seqList != null) {
						agentSerialNumberList = new ArrayList<AgentSerialNumber>(seqList.size());
						
						for (Map<String, String> map : seqList) {
							agent = new AgentSerialNumber();

							agent.setDetailBillMonth(getString(map, "月份"));
							String type = getString(map, "流水类型");
							String operateType = "";
							if ("1".equals(type)) {
								operateType = "转帐";
							} else if ("2".equals(type)) {
								operateType = "充值";
							} else if ("3".equals(type)) {
								operateType = "预存";
							}
							agent.setWfseqType(operateType);
							
							agent.setMsisdn(phoneNum);
							
							String cardAreaCode = getString(map, "充值卡充值地区码");
//							agent.setCardAreaCode("0".equals(cardAreaCode) ? "成功" : "失败");
							
							//if(XWECPApp.NEED_CUT_CITYS.indexOf(city) != -1){
								agent.setStartDate(getString(map, "充值时间").replaceAll(":", "").replaceAll(" ", ""));
								agent.setAdMsisdn(getString(map, "被充值手机号码"));
							//}
							//新大陆的格式分解
//							else{
//								agent.setStartDate(getString(map, "时间").replaceAll(":", "").replaceAll(" ", ""));
//								agent.setAdMsisdn(getString(map, "调帐手机号"));
//							}
							
							agent.setAmount(getString(map, "金额"));
							agent.setArrFlag(getString(map, "撤销标志"));
						
							agentSerialNumberList.add(agent);
						}
						res.setAgentSerialNumber(agentSerialNumberList);
					}
					else{
						if(seqContent.indexOf("100") != -1){
							res.setResultCode(LOGIC_ERROR);
							res.setErrorCode(LOGIC_ERROR);
							res.setErrorMessage(seqContent);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 解析报文
	 * 
	 * @param tmp
	 * @return
	 */
	public Element getElement(byte[] tmp) {
		Element root = null;
		try {
			ByteArrayInputStream ins = new ByteArrayInputStream(tmp);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(ins);
			root = doc.getRootElement();
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return root;
	}

}
